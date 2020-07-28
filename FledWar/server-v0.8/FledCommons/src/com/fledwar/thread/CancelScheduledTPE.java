///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.thread;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// *
// * @author REx
// */
//public class CancelScheduledTPE
//{
//    private final Object FUTURE_ARRAYS_MUTEX;
//    
//    private Map<String, ScheduledFutureArray> future_arrays;
//    
//    private ScheduledThreadPoolExecutor executor;
//
//    public CancelScheduledTPE(int corePoolSize)
//    {
//        executor = new ScheduledThreadPoolExecutor(corePoolSize);
//        future_arrays = new HashMap<>();
//        
//        FUTURE_ARRAYS_MUTEX = new Object();
//    }
//    
//    public ScheduledThreadPoolExecutor getThreadPoolExecutor()
//    {
//        return executor;
//    }
//    
//    public ScheduledFuture<?> scheduleWithFixedDelay(String group,
//                                                     Runnable command,
//                                                     CancelCallback callback,
//                                                     long initial_delay,
//                                                     long ms_delay)
//    {
//        ScheduledFutureArray dest_group;
//        synchronized(FUTURE_ARRAYS_MUTEX)
//        {
//            dest_group = future_arrays.get(group);
//            if (dest_group == null)
//            {
//                dest_group = new ScheduledFutureArray();
//                future_arrays.put(group, dest_group);
//            }
//        }
//        
//        ScheduledFuture future = executor.scheduleWithFixedDelay(
//                command, initial_delay, ms_delay, TimeUnit.MILLISECONDS);
//        return dest_group.wrap(future, callback);
//    }
//    
//    public void scheduleWithFixedDelay(String group,
//                                       ScheduledWork work,
//                                       long initial_delay,
//                                       long ms_delay)
//    {
//        ScheduledFuture future = scheduleWithFixedDelay(group, 
//                                                        work, 
//                                                        work, 
//                                                        initial_delay, 
//                                                        ms_delay);
//        work.setScheduledFuture(future);
//    }
//    
//    public void cancelGroup(String group)
//    {
//        /**
//         * @TODO: need to make this able to shutdown the individual 
//         */
//        
//        ScheduledFutureArray dest_group;
//        synchronized(FUTURE_ARRAYS_MUTEX)
//        {
//            dest_group = future_arrays.remove(group);
//        }
//        
//        if (dest_group != null)
//        {
//            dest_group.cancelAll();
//        }
//    }
//
//    public void shutdown()
//    {
//        synchronized(FUTURE_ARRAYS_MUTEX)
//        {
//            for(ScheduledFutureArray array : future_arrays.values())
//            {
//                array.cancelAll();
//            }
//            executor.shutdown();
//        }
//    }
//}
