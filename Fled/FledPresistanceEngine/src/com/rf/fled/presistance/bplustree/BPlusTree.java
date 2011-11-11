/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.exceptions.FledStateException;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.interfaces.Browser;
import com.rf.fled.presistance.Presistance;
import com.rf.fled.presistance.Serializer;
import com.rf.fled.util.StreamSerializer;
import com.rf.fled.util.Pair;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author REx
 */
public class BPlusTree implements Presistance, Externalizable
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
     * total number of records in the tree
     */
    private long recordCount;
    
    /**
     * this is the number of records per page that are allowed. this
     * is for leaf and non-leaf
     */
    private int recordsPerPage;
    public int getRecordsPerPage(){ return recordsPerPage; }
    
    /**
     * this keeps track of the BPlusPage ids
     */
    private long pageCountAt;
    public long incrementPageCount(){ pageCountAt++; return pageCountAt; }
    
    /**
     * the serializer for bring object into and out of a ByteBuffer
     */
    private Serializer valueSerializer;

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
    public Object insert(long id, Object record, boolean replace)
            throws FledPresistanceException 
    {
        try
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
                InsertResult result = rootPage.insert(id, record, replace);
                boolean treeNeedsUpdate = false;
                
                // 
                if (result.overflowPage != null)
                {
                    BPlusPage newRoot = new BPlusPage(this, rootPage, result.overflowPage);
                    root = newRoot.getThisBuckedId();
                    pageManager.savePage(newRoot);
                    order++;
                    treeNeedsUpdate = true;
                }
                
                // if there is no existing value, then that means it was 
                // a new insert and we want to count that.
                if (result.existing == null)
                {
                    recordCount++;
                    treeNeedsUpdate = true;
                }

                if (treeNeedsUpdate)
                {
                    // @TODO make tree serializer
                    StreamSerializer.serialize(null, this);
                }
                return result.existing;
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE, ex);
        }
    }

    @Override
    public Object delete(long id) 
            throws FledPresistanceException
    {
        try
        {
            BPlusPage rootPage = getRootPage();
            if (rootPage == null)
            {
                // @TODO statement
                throw new FledStateException(LanguageStatements.NONE);
            }
            
            boolean thisNeedsUpdate = false;
            DeleteResult result = rootPage.delete(id);
            
            if (result.underflow && rootPage.compacityUsed() < 2)
            {
                thisNeedsUpdate = true;
                
                if (rootPage.compacityUsed() == 1)
                {
                    root = pageManager
                            .getPage(rootPage.getChildId(0))
                            .getThisBuckedId();
                    pageManager.deletePage(rootPage.getThisBuckedId());
                }
                else
                {
                    root = -1;
                    pageManager.deletePage(rootPage.getThisBuckedId());
                }
            }
            
            if (result.removedValue != null)
            {
                thisNeedsUpdate = true;
                
                recordCount--;
            }
            
            if (thisNeedsUpdate)
            {
                // @TODO make tree serializer
                StreamSerializer.serialize(null, this);
            }
            
            return result.removedValue;
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE, ex);
        }
    }
    
    private BPlusPage getRootPage() 
            throws FledPresistanceException
    {
        return pageManager.getPage(root);
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws  IOException, 
                    ClassNotFoundException 
    {
        valueSerializer = (Serializer) in.readObject();
        root            = in.readLong();
        order           = in.readInt();
        recordCount     = in.readLong();
        recordsPerPage  = in.readInt();
        pageCountAt     = in.readLong();
    }

    @Override
    public void writeExternal(ObjectOutput out) 
            throws IOException 
    {
        out.writeObject(valueSerializer);
        out.writeLong(root);
        out.writeInt(order);
        out.writeLong(recordCount);
        out.writeInt(recordsPerPage);
        out.writeLong(pageCountAt);
    }
    
    
}
