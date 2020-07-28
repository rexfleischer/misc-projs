/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.thread;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rfleischer
 */
public class ThreadManager
{
    public static final String THREAD_COUNT = "count";
    
    private List<BackgroundThread> pool;
    
    public ThreadManager(Class<? extends BackgroundThread> managing, 
                         Map<String, String> config)
        throws ThreadManagerException
    {
        try
        {
            int count = Integer.parseInt(config.get(THREAD_COUNT));
            
            pool = new ArrayList<>(count);
            
            Constructor<? extends BackgroundThread> constructor = managing.getConstructor();
            
            for(int i = 0; i < count; i++)
            {
                BackgroundThread instance = constructor.newInstance();
                instance.configure(config);
                pool.add(instance);
            }
        }
        catch(Throwable ex)
        {
            throw new ThreadManagerException(
                    "could not start background threads", 
                    ex);
        }
    }
    
    public ThreadManager start()
    {
        for(BackgroundThread thread : pool)
        {
            thread.start();
        }
        return this;
    }
    
    public void stop() throws InterruptedException 
    {
        for(BackgroundThread thread : pool)
        {
            thread.signalStop();
        }
        
        for(BackgroundThread thread : pool)
        {
            thread.join();
        }
    }
    
    public List<Thread.State> status()
    {
        ArrayList<Thread.State> result = new ArrayList<>(pool.size());
        
        for(BackgroundThread thread : pool)
        {
            result.add(thread.getState());
        }
        
        return result;
    }
    
}
