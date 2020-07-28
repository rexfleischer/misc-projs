/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class ThreadManagerRegistry
{
    public enum RegistryState
    {
        NEW,
        
        STARTED,
        
        ERROR,
    }
    
    private static ArrayList<Throwable> throwables = new ArrayList<>();
    
    private static RegistryState state = RegistryState.NEW;
    
    private static final Map<Class, ThreadManager> managers = new HashMap<>();
    
    public synchronized static void register(Class<? extends BackgroundThread> clazz,
                                             Map<String, String> config)
            throws ThreadManagerException
    {
        if (state != RegistryState.NEW)
        {
            throw new IllegalStateException("thread managers already started");
        }
        if (managers.containsKey(clazz))
        {
            throw new IllegalStateException(
                    String.format("thread manager already registered [class:%s]", 
                                  clazz));
        }
        
        ThreadManager manager = new ThreadManager(clazz, config);
        managers.put(clazz, manager);
    }
    
    public synchronized static ThreadManager getThreadManager(Class clazz)
    {
        return managers.get(clazz);
    }
    
    public synchronized static void start()
    {
        if (state != RegistryState.NEW)
        {
            throw new IllegalStateException(
                    "can only call start() with a registry state of NEW");
        }
        for(ThreadManager manager : managers.values())
        {
            manager.start();
        }
        state = RegistryState.STARTED;
    }
    
    public synchronized static void stop()
    {
        for(ThreadManager manager : managers.values())
        {
            try
            {
                manager.stop();
            }
            catch(InterruptedException ex)
            {
                registerThrowable(ex);
            }
        }
        
        if (state != RegistryState.ERROR)
        {
            state = RegistryState.NEW;
        }
        
        managers.clear();
    }
    
    public synchronized static RegistryState getRegistryState()
    {
        return state;
    }
    
    public synchronized static Map<Class, List<Thread.State>> status()
    {
        Map<Class, List<Thread.State>> result = new HashMap<>();
        Iterator<Class> it = result.keySet().iterator();
        while(it.hasNext())
        {
            Class clazz = it.next();
            List<Thread.State> managerstate = managers.get(clazz).status();
            result.put(clazz, managerstate);
        }
        return result;
    }
    
    public synchronized static void clearThrowables()
    {
        throwables.clear();
        state = RegistryState.STARTED;
    }
    
    public synchronized static List<Throwable> getThrowables()
    {
        return throwables;
    }
    
    synchronized static void registerThrowable(Throwable throwable)
    {
        throwables.add(throwable);
        state = RegistryState.ERROR;
    }
}
