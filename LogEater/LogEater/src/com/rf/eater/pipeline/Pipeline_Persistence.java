/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

import com.rf.eater.persistence.Persistence;
import com.rf.eater.persistence.PersistenceException;
import com.rf.eater.persistence.PersistenceFactory;
import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.consumer.WorkIterator;
import com.rf.eater.thread.WorkerThreadException;
import com.rf.eater.util.SynchronizedQueue;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class Pipeline_Persistence implements WorkFactory
{
    private class ObjectUsagePair
    {
        final Object LOCK = new Object();
        boolean used;
        Persistence persistence;
    }
    
    private final Object LOCK;
    
    private ArrayList<ObjectUsagePair> persistences;
    
    private PersistenceFactory factory;
    
    private SynchronizedQueue<Map<String, String>> input;
    
    private Map<String, String> connection;
    
    Pipeline_Persistence(SynchronizedQueue<Map<String, String>> input)
    {
        this.LOCK       = new Object();
        this.input      = input;
        persistences    = new ArrayList<ObjectUsagePair>();
    }
    
    protected Persistence getPersistence(int index)
    {
        return persistences.get(index).persistence;
    }
    
    protected void setPersistenceFactory(
            PersistenceFactory factory,
            Map<String, String> connection)
    {
        if (factory == null)
        {
            throw new NullPointerException("factory");
        }
        if (connection == null)
        {
            throw new NullPointerException("connection");
        }
        if (this.factory != null)
        {
            throw new IllegalStateException("a persistence factory is already set");
        }
        this.factory    = factory;
        this.connection = connection;
    }

    @Override
    public int howMuchMore ()
    {
        return input.numberOfElements();
    }

    @Override
    public String workType ()
    {
        return "persistence";
    }

    @Override
    public WorkIterator getIterator () throws WorkerThreadException
    {
        if (this.factory == null)
        {
            throw new IllegalStateException("a persistence factory has not been set");
        }
        synchronized(LOCK)
        {
            /**
             * first, loop through the open connections and see
             * if there is one not in use that can be reused
             */
            for(ObjectUsagePair pair : persistences)
            {
                synchronized(pair.LOCK)
                {
                    if (!pair.used)
                    {
                        /**
                         * yay!
                         */
                        pair.used = true;
                        return new ThisWorkIterator(pair);
                    }
                }
            }
            /**
             * if we get here, then we need to create a new
             * connection and then add it to the persistences
             */
            ObjectUsagePair newPair = new ObjectUsagePair();
            newPair.used = true;
            try
            {
                newPair.persistence = factory.getNewConnection(connection);
            }
            catch (PersistenceException ex)
            {
                throw new WorkerThreadException(
                        "could not create new persistence tier", ex);
            }
            persistences.add(newPair);
            return new ThisWorkIterator(newPair);
        }
    }
    
    public class ThisWorkIterator implements WorkIterator
    {
        private ObjectUsagePair pair;
        
        private ThisWorkIterator(ObjectUsagePair pair)
        {
            this.pair = pair;
        }

        @Override
        public boolean doWork ()
                throws WorkerThreadException
        {
            try
            {
                Map<String, String> output = input.pop();
                pair.persistence.persist(output);
                return input.numberOfElements() != 0;
            }
            catch(PersistenceException ex)
            {
                throw new WorkerThreadException(
                        "error while trying to persist data", ex);
            }
        }

        @Override
        public String workType ()
        {
            return Pipeline_Persistence.this.workType();
        }

        @Override
        public void close () throws WorkerThreadException
        {
            synchronized(pair.LOCK)
            {
                pair.used = false;
            }
        }
    }
}
