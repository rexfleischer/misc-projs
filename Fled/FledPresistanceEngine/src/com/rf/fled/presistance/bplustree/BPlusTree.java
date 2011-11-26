/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.exceptions.FledTransactionException;
import com.rf.fled.interfaces.Browser;
import com.rf.fled.interfaces.Presistance;
import com.rf.fled.interfaces.Serializer;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.presistance.FileManager;
import com.rf.fled.presistance.RecordFile;
import com.rf.fled.util.Pair;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author REx
 */
public class BPlusTree implements Presistance
{
    public static final String EXTENSION = "btree";
    
    /**
     * the manager of all of the pages on the file system
     */
    private FileManager fileManager;
    public FileManager getFileManager() { return fileManager; }
    
    /**
     * the transactions that are currently active
     */
    private Map<Thread, FileManager> transactions;
    
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
    public long getCount(){ return recordCount; }
    
    /**
     * 
     */
    private String btreeName;
    public String getBTreeName(){ return btreeName; }
    
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
     * 
     */
    private Serializer<byte[]> valueSerializer;
    public Serializer<byte[]> getValueSerializer(){ return valueSerializer; }
    

    @Override
    public Browser<Pair<Long, Object>> browse()
            throws FledPresistanceException 
    {
        try
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
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE, ex);
        }
    }

    @Override
    public Browser<Pair<Long, Object>> browse(long id) 
            throws FledPresistanceException 
    {
        try
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
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE, ex);
        }
    }

    @Override
    public Object select(long id) 
            throws FledPresistanceException 
    {
        try
        {
            BPlusPage rootPage = getRootPage();
            if (rootPage == null)
            {
                return null;
            }
            return rootPage.select(id);
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
        throw new UnsupportedOperationException("Not supported yet.");
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
                
                root = newRoot.getThisBuckedId();

                // save the file to disk
                pageManager.savePage(
                        buildPageId(newRoot.getThisBuckedId()), 
                        newRoot, 
                        pageSerializer);
                
                // save the tree to disk
                fileManager.saveRecordFile(newRoot.getRecordFile());

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
                    pageManager.savePage(
                            buildPageId(newRoot.getThisBuckedId()), 
                            newRoot, 
                            pageSerializer);
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
                    pageManager.savePage(buildTreeId(), this, null);
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
    public void beginTransaction() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollback() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private BPlusPage getRootPage() throws IOException
    {
        RecordFile file = fileManager.loadRecordFile(buildBPageId(root));
        return new BPlusPage(file);
    }
    
    protected String buildBPageId(long id)
    {
        return btreeName + "." + id + "." + BPlusPage.EXTENSION;
    }
    
    protected String buildBTreeId()
    {
        return btreeName + "." + BPlusTree.EXTENSION;
    }
}
