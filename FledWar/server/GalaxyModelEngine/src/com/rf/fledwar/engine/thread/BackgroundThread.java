/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.thread;

import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public abstract class BackgroundThread extends Thread
{
    public static final String SLEEP_TIME = "sleeptime";
    
    protected static Logger logger = Logger.getLogger(BackgroundThread.class);
    
    protected abstract void doSingleIteration() throws Exception;
    
    private boolean _stop;
    
    private int _sleeptime;
    
    public BackgroundThread()
    {
        _stop = false;
        _sleeptime = -1;
    }
    
    public synchronized void signalStop()
    {
        _stop = true;
    }
    
    protected synchronized boolean readStop()
    {
        return _stop;
    }
    
    public synchronized void setSleepTime(int sleeptime)
    {
        this._sleeptime = sleeptime;
    }
    
    public synchronized int getSleepTime()
    {
        return _sleeptime;
    }
    
    public void configure(Map<String, String> config)
    {
        String sleepcheck = config.get(SLEEP_TIME);
        if (sleepcheck != null && !sleepcheck.isEmpty())
        {
            _sleeptime = Integer.parseInt(sleepcheck);
        }
    }

    @Override
    public final void run() 
    {
        logger.info(String.format("%s: thread started",
                                  Thread.currentThread().getName()));
        while(!readStop())
        {
            try
            {
                doSingleIteration();
            }
            catch(Exception ex)
            {
                logger.error("unhandled exception", ex);
                break;
            }
            
            int sleeptime = getSleepTime();
            
            if (sleeptime != -1)
            {
                try
                {
                    Thread.sleep(sleeptime);
                }
                catch(InterruptedException ex) {}
            }
        }
        logger.info(String.format("%s: thread stopped",
                                  Thread.currentThread().getName()));
    }
}
