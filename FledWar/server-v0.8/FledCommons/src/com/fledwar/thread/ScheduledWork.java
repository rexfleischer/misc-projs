///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.thread;
//
//import java.util.concurrent.ScheduledFuture;
//import org.apache.log4j.Logger;
//
///**
// *
// * @author REx
// */
//public abstract class ScheduledWork implements Runnable, CancelCallback
//{
//    private ScheduledFuture future;
//    
//    public abstract Logger getLogger();
//    
//    public final void setScheduledFuture(ScheduledFuture future)
//    {
//        if (this.future != null)
//        {
//            throw new IllegalStateException("future already set");
//        }
//        this.future = future;
//    }
//    
//    @Override
//    public final void forwardCancelCallback() throws Exception
//    {
//        getLogger().warn("forward cancel called");
//        cancelWork();
//    }
//    
//    public void cancelWork() throws Exception
//    {
//        if (future != null)
//        {
//            future.cancel(false);
//        }
//    }
//    
//}
