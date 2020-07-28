package com.rf.pipeline.pipes;

import com.rf.pipeline.PipelineWorkException;
import com.rf.pipeline.WorkIterator;
import com.rf.pipeline.queue.WorkQueue;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class PipeWorkFactory_1i1o implements PipeWorkFactory
{
    
    private class ObjectUsagePair
    {
        final Object LOCK = new Object();
        boolean used;
        WorkIterator iterator;
    }
    
    private int workCounter;
    
    private final Object COUNTER;
    
    private final Object LOCK;
    
    private String inputContext;
    
    private String[] outputContexts;
    
    private String type;
    
    private LinkedList<ObjectUsagePair> pairs;
    
    private WorkQueue input;
    
    private Map<String, WorkQueue> outputs;
    
    private Map<String, Object> globals;
    
    private Map<String, Object> routed;
    
    private String clazz;
    
    private Constructor constructor;
    
    private int counter;
    
    public PipeWorkFactory_1i1o(String clazz, 
                                String type, 
                                WorkQueue input,
                                String inputContext,
                                Map<String, WorkQueue> outputs,
                                Map<String, String> outputContexts,
                                Map<String, Object> globals) 
            throws  ClassNotFoundException, 
                    NoSuchMethodException, 
                    InstantiationException, 
                    IllegalAccessException, 
                    IllegalArgumentException, 
                    InvocationTargetException, 
                    PipelineWorkException
    {
        this.workCounter    = 0;
        this.COUNTER        = new Object();
        this.LOCK           = new Object();
        this.pairs          = new LinkedList<ObjectUsagePair>();
        this.input          = input;
        this.outputs        = outputs;
        this.type           = type;
        this.clazz          = clazz;
        this.counter        = 1;
        this.globals        = globals;
        this.routed         = new HashMap<String, Object>();
        this.inputContext   = inputContext;
        
        Collection<String> collection = outputContexts.values();
        this.outputContexts = new String[collection.size()];
        this.outputContexts = collection.toArray(this.outputContexts);
        
        constructor = Class.forName(clazz).getConstructor();
        ObjectUsagePair pair = factory();
        pair.used = false;
    }

    @Override
    public WorkIterator getIterator() throws PipelineWorkException
    {
        synchronized(LOCK)
        {
            /**
             * first, loop through the open connections and see
             * if there is one not in use that can be reused
             */
            for(ObjectUsagePair pair : pairs)
            {
                synchronized(pair.LOCK)
                {
                    if (!pair.used)
                    {
                        /**
                         * yay!
                         */
                        pair.used = true;
                        return pair.iterator;
                    }
                }
            }
            try
            {
                return factory().iterator;
            }
            catch (Exception ex)
            {
                throw new PipelineWorkException(
                        "error while factoring an instance of " + clazz, ex);
            }
        }
    }
    
    @Override
    public void pause(WorkIterator it) throws PipelineWorkException
    {
        synchronized(LOCK)
        {
            for(ObjectUsagePair pair : pairs)
            {
                synchronized(pair.LOCK)
                {
                    if (pair.iterator.getId() == it.getId())
                    {
                        /**
                         * yay!
                         */
                        pair.used = false;
                        return;
                    }
                }
            }
        }
        throw new PipelineWorkException("could not find WorkIterator for pause");
    }

    @Override
    public String inputContext()
    {
        return inputContext;
    }

    @Override
    public String[] outputContexts()
    {
        return outputContexts;
    }
    
    @Override
    public void setObject(String key, Object object)
    {
        routed.put(key, object);
    }
    
    @Override
    public Object getObject(String key)
    {
        return routed.get(key);
    }
    
    @Override
    public int howMuchMore()
            throws PipelineWorkException
    {
        if (input != null)
        {
            return input.numberOfElements();
        }
        return pairs.getFirst().iterator.estimateWorkLeft();
    }

    @Override
    public String getContext()
    {
        return type;
    }

    @Override
    public int workDone() throws PipelineWorkException
    {
        int result = 0;
        synchronized(COUNTER)
        {
            result = workCounter;
            workCounter = 0;
        }
        return result;
    }

    @Override
    public void incrementWork() throws PipelineWorkException
    {
        synchronized(COUNTER)
        {
            workCounter++;
        }
    }
    
    private ObjectUsagePair factory() 
            throws  InstantiationException, 
                    IllegalAccessException, 
                    IllegalArgumentException, 
                    InvocationTargetException,
                    PipelineWorkException
    {
        Object instance = constructor.newInstance();
        if (!(instance instanceof WorkIterator))
        {
            throw new PipelineWorkException(clazz + " must extend WorkIterator");
        }
        ObjectUsagePair pair = new ObjectUsagePair();
        pair.used = true;
        pair.iterator = (WorkIterator) instance;
        pair.iterator.setup(counter++, this, input, outputs, routed, globals);
        pair.iterator.init(type);
        pairs.add(pair);
        return pair;
    }
}
