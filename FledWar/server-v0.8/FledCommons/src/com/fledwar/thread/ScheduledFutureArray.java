///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.thread;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import org.apache.log4j.Logger;
//
///**
// *
// * @author REx
// */
//public class ScheduledFutureArray
//{
//    private static final Logger logger = Logger.getLogger(
//            ScheduledFutureArray.class);
//    
//    private List<WrappedScheduledFuture> futures;
//
//    public ScheduledFutureArray()
//    {
//        this.futures = Collections.synchronizedList(
//                new ArrayList<WrappedScheduledFuture>());
//    }
//    
//    public ScheduledFuture wrap(ScheduledFuture future)
//    {
//        WrappedScheduledFuture result = new WrappedScheduledFuture(
//                future, null);
//        futures.add(result);
//        return result;
//    }
//    
//    public ScheduledFuture wrap(ScheduledFuture future, 
//                                CancelCallback callback)
//    {
//        WrappedScheduledFuture result = new WrappedScheduledFuture(
//                future, callback);
//        futures.add(result);
//        return result;
//    }
//    
//    public void cancelAll()
//    {
//        while(!futures.isEmpty())
//        {
//            futures.remove(0).forwardCancel();
//        }
//    }
//    
//    class WrappedScheduledFuture implements ScheduledFuture
//    {
//        private ScheduledFuture wrapping;
//        
//        private CancelCallback callback;
//
//        WrappedScheduledFuture(ScheduledFuture wrapping, 
//                               CancelCallback callback)
//        {
//            this.wrapping = wrapping;
//            this.callback = callback;
//        }
//
//        @Override
//        public long getDelay(TimeUnit unit)
//        {
//            return wrapping.getDelay(unit);
//        }
//
//        @Override
//        public int compareTo(Object o)
//        {
//            if (o instanceof WrappedScheduledFuture)
//            {
//                return wrapping.compareTo(((WrappedScheduledFuture) o).wrapping);
//            }
//            else
//            {
//                return wrapping.compareTo(o);
//            }
//        }
//        
//        public void forwardCancel()
//        {
//            wrapping.cancel(false);
//            if (callback != null)
//            {
//                try
//                {
//                    callback.forwardCancelCallback();
//                }
//                catch(Exception ex)
//                {
//                    logger.error("unhandled exception in "
//                                 + "forward cancel callback", 
//                                 ex);
//                }
//            }
//        }
//
//        @Override
//        public boolean cancel(boolean mayInterruptIfRunning)
//        {
//            futures.remove(this);
//            return wrapping.cancel(mayInterruptIfRunning);
//        }
//
//        @Override
//        public boolean isCancelled()
//        {
//            return wrapping.isCancelled();
//        }
//
//        @Override
//        public boolean isDone()
//        {
//            return wrapping.isDone();
//        }
//
//        @Override
//        public Object get() throws InterruptedException, ExecutionException
//        {
//            return wrapping.get();
//        }
//
//        @Override
//        public Object get(long timeout, TimeUnit unit) 
//                throws InterruptedException, ExecutionException, TimeoutException
//        {
//            return wrapping.get(timeout, unit);
//        }
//        
//        @Override
//        public String toString()
//        {
//            return wrapping.toString();
//        }
//    }
//}
