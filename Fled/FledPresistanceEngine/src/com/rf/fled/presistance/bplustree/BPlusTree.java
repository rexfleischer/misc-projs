/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.presistance.Browser;
import com.rf.fled.presistance.Presistance;
import com.rf.fled.presistance.Serializer;
import com.rf.fled.util.Pair;

/**
 *
 * @author REx
 */
public class BPlusTree implements Presistance
{
    /**
     * the manager of all of the pages on the file system
     */
    private BPlusPageManager pageManager;
    
    /**
     * this is where a locking mechanism is supposed to go 
     * when i actually get around to doing it.
     */
    // private RangedReadWriteLock gate;
    
    /**
     * the first pages id
     */
    private long root;
    
    /**
     * this represents how many 'layers' there are of pages before
     * it will get to a leaf node.
     */
    private int order;
    
    /**
     * total number of records in the tree
     */
    private int recordCount;
    
    private volatile Object LOCK = new Object();
    
    /**
     * the serializer for bring object into and out of a ByteBuffer
     */
    private Serializer serializer;

    @Override
    public Object select(long id) 
            throws FledPresistanceException
    {
        return pageManager.getPage(root).select(id);
    }

    @Override
    public Browser<Pair<Long, Object>> browse(long id) 
            throws FledPresistanceException
    {
        BPlusPage rootPage = pageManager.getPage(root);
        if (rootPage == null)
        {
            return null;
        }
        
        Browser<Pair<Long, Object>> browser = rootPage.browse(id);
        Pair<Long, Object> pair = new Pair<Long, Object>();
        
        if (browser.curr(pair))
        {
            return browser;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void update(long id, Object record) 
            throws FledPresistanceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long insert(Object record)
            throws FledPresistanceException 
    {
        long newId = -1;
        
        
        
        synchronized(LOCK)
        {
            recordCount++;
        }
        return newId;
    }

    @Override
    public void delete(long id) 
            throws FledPresistanceException
    {
        long newId = -1;
        
        
        
        synchronized(LOCK)
        {
            recordCount--;
        }
    }
    
    @Override
    public void beginTransaction() 
            throws FledPresistanceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commit() 
            throws FledPresistanceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollback() 
            throws FledPresistanceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
