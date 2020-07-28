///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.thread;
//
//import com.fledwar.configuration.Configuration;
//
///**
// *
// * @author REx
// */
//public abstract class ServerThread extends Thread
//{
//    public static final int DONT_SLEEP_SIGNAL = -1;
//    
//    protected abstract void doSingleIteration();
//    
//    protected abstract void doInit();
//    
//    protected abstract void doFinish();
//    
//    private boolean stop;
//    
//    private int sleeptime;
//    
//    protected Configuration config;
//    
//    protected ServerThread(Configuration config)
//    {
//        this.stop       = false;
//        this.sleeptime  = DONT_SLEEP_SIGNAL;
//        this.config     = config;
//    }
//    
//    protected ServerThread()
//    {
//        this.stop       = false;
//        this.sleeptime  = DONT_SLEEP_SIGNAL;
//        this.config     = null;
//    }
//    
//    public final synchronized void setDontSleep()
//    {
//        this.sleeptime = DONT_SLEEP_SIGNAL;
//    }
//    
//    public final synchronized void setSleepTime(int time)
//    {
//        this.sleeptime = time;
//    }
//    
//    private synchronized boolean doSleep()
//    {
//        return this.sleeptime != DONT_SLEEP_SIGNAL;
//    }
//    
//    private synchronized int sleepTime()
//    {
//        return this.sleeptime;
//    }
//    
//    public final synchronized void signalStop()
//    {
//        stop = true;
//    }
//    
//    public final synchronized boolean isStopped()
//    {
//        return stop;
//    }
//    
//    @Override
//    public final void run()
//    {
//        doInit();
//        
//        while(!isStopped())
//        {
//            doSingleIteration();
//            
//            if (doSleep())
//            {
//                try
//                {
//                    Thread.sleep(sleepTime());
//                }
//                catch(InterruptedException ex) { }
//            }
//        }
//        
//        doFinish();
//    }
//}
