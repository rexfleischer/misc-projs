///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.eater.pipeline;
//
//import com.rf.eater.consumer.WorkFactory;
//import com.rf.eater.consumer.WorkIterator;
//import com.rf.eater.thread.WorkerThreadException;
//import java.util.LinkedList;
//
///**
// *
// * @author REx
// */
//public abstract class PipelineWorkFactory<_Ty, _Iy, _Oy> implements WorkFactory
//{
//    private class ObjectUsagePair
//    {
//        final Object LOCK = new Object();
//        boolean used;
//        _Ty object;
//    }
//    
//    protected abstract boolean doWork(_Ty object, _Iy input, _Oy output);
//    
//    private final Object LOCK;
//    
//    private String type;
//    
//    private LinkedList<ObjectUsagePair> pairs;
//    
//    private _Iy input;
//    
//    private _Oy output;
//    
//    PipelineWorkFactory(String type, _Iy input, _Oy output)
//    {
//        this.LOCK   = new Object();
//        this.pairs  = new LinkedList<ObjectUsagePair>();
//        this.input  = input;
//        this.output = output;
//        this.type   = type;
//    }
//
//    @Override
//    public WorkIterator getIterator() throws WorkerThreadException
//    {
//        synchronized(LOCK)
//        {
//            /**
//             * first, loop through the open connections and see
//             * if there is one not in use that can be reused
//             */
//            for(ObjectUsagePair pair : pairs)
//            {
//                synchronized(pair.LOCK)
//                {
//                    if (!pair.used)
//                    {
//                        /**
//                         * yay!
//                         */
//                        pair.used = true;
//                        return new ThisWorkIterator(pair);
//                    }
//                }
//            }
//            /**
//             * if we get here, then we need to create a new
//             * connection and then add it to the persistences
//             */
//            ObjectUsagePair newPair = new ObjectUsagePair();
//            newPair.used = true;
//            try
//            {
//                newPair.persistence = factory.getNewConnection(connection);
//            }
//            catch (PersistenceException ex)
//            {
//                throw new WorkerThreadException(
//                        "could not create new persistence tier", ex);
//            }
//            persistences.add(newPair);
//            return new ThisWorkIterator(newPair);
//        }
//    }
//    
//    @Override
//    public int howMuchMore()
//    {
//        return -1;
//    }
//
//    @Override
//    public String workType()
//    {
//        return type;
//    }
//    
//    
//    public class ThisWorkIterator implements WorkIterator
//    {
//        private ObjectUsagePair pair;
//        
//        private ThisWorkIterator(ObjectUsagePair pair)
//        {
//            this.pair = pair;
//        }
//
//        @Override
//        public boolean doWork()
//                throws WorkerThreadException
//        {
//            return PipelineWorkFactory.this.doWork(pair.object, input, output);
//        }
//
//        @Override
//        public String workType ()
//        {
//            return PipelineWorkFactory.this.workType();
//        }
//
//        @Override
//        public void close () throws WorkerThreadException
//        {
//            synchronized(pair.LOCK)
//            {
//                pair.used = false;
//            }
//        }
//    }
//}
