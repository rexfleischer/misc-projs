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
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public class BPlusTree implements Presistance, Serializer
{
    /**
     * the manager of all of the pages on the file system
     */
    private BPlusPageManager pageManager;
    public BPlusPageManager getPageManager() { return pageManager; }
    
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
     * this keeps track of the BPlusPage ids
     */
    private long pageCountAt;
    public long incrementPageCount(){ pageCountAt++; return pageCountAt; }
    
    /**
     * total number of records in the tree
     */
    private int recordCount;
    
    /**
     * the serializer for bring object into and out of a ByteBuffer
     */
    private Serializer serializer;
    
    /**
     * this is the number of records per page that are allowed. this
     * is for leaf and non-leaf
     */
    private int recordsPerPage;
    public int getRecordsPerPage(){ return recordsPerPage; }

    @Override
    public Object select(long id) 
            throws FledPresistanceException
    {
        BPlusPage rootPage = getRootPage();
        if (rootPage == null)
        {
            // basically means there are no records
            return null;
        }
        return rootPage.select(id);
    }

    @Override
    public Browser<Pair<Long, Object>> browse(long id) 
            throws FledPresistanceException
    {
        BPlusPage rootPage = getRootPage();
        if (rootPage == null)
        {
            // basically means there are no records
            return null;
        }
        
        // get the browser
        Browser<Pair<Long, Object>> browser = rootPage.browse(id);
        
        if (browser.valid())
        {
            return browser;
        }
        else
        {
            return null;
        }
    }

    @Override
    public Browser<Pair<Long, Object>> browse() 
            throws FledPresistanceException
    {
        BPlusPage rootPage = getRootPage();
        if (rootPage == null)
        {
            // basically means there are no records
            return null;
        }
        
        // get the browser
        Browser<Pair<Long, Object>> browser = rootPage.browse();
        
        if (browser.valid())
        {
            return browser;
        }
        else
        {
            return null;
        }
    }

    @Override
    public Object insert(long id, Object record)
            throws FledPresistanceException 
    {
        if (record == null)
        {
            throw new NullPointerException("record");
        }
        
        BPlusPage rootPage = getRootPage();
        
        if (rootPage == null)
        {
            // this means the tree is empty and we are going 
            // to do a first insert
            BPlusPage newRoot = new BPlusPage(this, id, record);
            
            // first record
            recordCount = 1;
            
            // save the file to file
            pageManager.savePage(newRoot);
            
            // no object was overridden
            return null;
        }
        else
        {
            
        }
        
        return null;
    }

    @Override
    public void delete(long id) 
            throws FledPresistanceException
    {
        
        
        recordCount--;
    }
    
    private BPlusPage getRootPage() 
            throws FledPresistanceException
    {
        return pageManager.getPage(root);
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
    
    
    @Override
    public Object deserialize(ByteBuffer buffer) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ByteBuffer serialize(Object obj) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
