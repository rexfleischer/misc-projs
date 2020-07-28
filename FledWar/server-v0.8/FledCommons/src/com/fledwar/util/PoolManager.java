/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public abstract class PoolManager<T extends PoolManager.Poolable>
{
    
    protected abstract Poolable factory(Object ... extraparams)
            throws PoolManagerException;
    
    public interface Poolable
    {
        public void setToActive();
        
        public boolean isActive();
        
        public void finish();
    }
    
    private static Logger logger = Logger.getLogger(PoolManager.class);
    
    private final List<Poolable> pool;
    
    private final int min;
    
    private long lastGC;
    
    private long gcthreashold;
    
    public PoolManager(int min, long gcthreashold)
            throws PoolManagerException
    {
        this(min, gcthreashold, false);
    }
    
    /**
     * 
     * @param config
     * @throws PoolManagerException 
     */
    public PoolManager(int min, long gcthreashold, boolean fillpool)
            throws PoolManagerException
    {
        this.pool = new ArrayList<>();
        this.min = min;
        this.gcthreashold = gcthreashold;
        
        if (fillpool)
        {
            for(int i = 0; i < this.min; i++)
            {
                pool.add(factory());
            }
        }
    }
    
    /**
     * 
     * @return 
     */
    public List<Poolable> getPool()
    {
        return pool;
    }
    
    /**
     * 
     * @return 
     */
    public synchronized long getLastGCTime()
    {
        return lastGC;
    }
    
    /**
     * 
     * @param factoryIfNull
     * @return 
     */
    public synchronized T getUnused(boolean factoryIfBusy)
            throws PoolManagerException
    {
        Poolable result = null;
        for(int i = 0, n = pool.size(); i < n && result == null; i++)
        {
            Poolable element = pool.get(i);
            if (!element.isActive())
            {
                element.setToActive();
                result = element;
            }
        }
        
        if (result != null)
        {
            gcPoolCheck();
        }
        else
        {
            result = factory();
            pool.add(result);
        }
        
        return (T) result;
    }
    
    /**
     * 
     * @param extraparams
     * @throws PoolManagerException 
     */
    public synchronized void addToPool(Object ... extraparams)
            throws PoolManagerException
    {
        Poolable element = factory(extraparams);
        pool.add(element);
    }
    
    /**
     * 
     * @throws PoolManagerException 
     */
    public synchronized void gcPoolCheck()
            throws PoolManagerException
    {
        if (System.currentTimeMillis() < (lastGC + gcthreashold))
        {
            // no gc needed
            return;
        }
        
        logger.debug("GCing object pool");
        Iterator<Poolable> it = pool.iterator();
        while(it.hasNext())
        {
            Poolable element = it.next();
            if (!element.isActive())
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug(String.format("removing element from pool: %s", 
                                               element));
                }
                it.remove();
            }
        }
        
        lastGC = System.currentTimeMillis();
    }
    
    /**
     * 
     * @throws PoolManagerException 
     */
    public synchronized void shutdown()
            throws PoolManagerException
    {
        for(Poolable element : pool)
        {
            element.finish();
        }
    }
}
