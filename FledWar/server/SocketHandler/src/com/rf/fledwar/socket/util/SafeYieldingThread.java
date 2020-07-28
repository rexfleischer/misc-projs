/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.util;

import java.util.Objects;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public abstract class SafeYieldingThread extends Thread
{
    protected abstract void doSingleIteration() throws Exception;
    
    protected boolean stop;
    
    private Logger logger;
    
    public SafeYieldingThread(Logger logger)
    {
        this.logger = Objects.requireNonNull(logger, "logger");
        stop = false;
    }
    
    public synchronized void signalStop()
    {
        stop = true;
    }
    
    private synchronized boolean isStopped()
    {
        return stop;
    }

    @Override
    public final void run() 
    {
        logger.info(String.format("%s: thread started",
                                  Thread.currentThread().getName()));
        while(!isStopped())
        {
            try
            {
                doSingleIteration();
            }
            catch(Exception ex)
            {
                logger.fatal("caught unhandled exception", ex);
                return;
            }
            
            /**
             * do not remove this yield.
             * 
             * when an application has many threads, its very important
             * for all non-essential threads to yield... and every thread
             * that uses this will be non-essential being that it's not
             * running the server or OS...
             * 
             * the benchmarks with the ServerSocketListenerTest i've ran on 
             * my home pc were like this:
             * OS: windows 7 (SP 1)
             * RAM: 12 GB
             * Processor: Intel(R) Core(TM) i7-3770 CPU @ 3.4GHz
             * 
             * first benchmark: 10 clients (total of 40 threads)
             * without yield: takes around 1.5 seconds
             * with yield: takes around 0.5 seconds
             * 
             * second benchmark: 100 clients (total of 400 threads)
             * without yield: takes around 40-60 seconds 
             * with yield: takes around 1.5-2 seconds....
             * 
             * third benchmark: 500 clients (total of 2000 threads)
             * without yield: would not finish (stopped at 7 minutes)...
             * with yield: around 12 seconds.
             * 
             * fun benchmark: 1000 clients (total of 4000 threads!!!!)
             * without yield: didnt try..
             * with yield: between 20-30 seconds
             * 
             * 
             * 
             * so, for anyone thinking that it may be a good idea to remove
             * this based on the number of threads, or anything crazy like 
             * that... just dont do it... i've already went down that
             * road and this is the best way.
             * 
             */
            Thread.yield();
        }
    }
    
}
