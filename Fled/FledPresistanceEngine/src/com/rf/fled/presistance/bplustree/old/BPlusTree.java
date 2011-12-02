package com.rf.fled.presistance.bplustree.old;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fled.presistance.bplustree;
//
//import com.rf.fled.exceptions.FledPresistanceException;
//import com.rf.fled.exceptions.FledStateException;
//import com.rf.fled.language.LanguageStatements;
//import com.rf.fled.interfaces.Browser;
//import com.rf.fled.interfaces.Presistance;
//import com.rf.fled.interfaces.Serializer;
//import com.rf.fled.presistance.FileManager;
//import com.rf.fled.util.Pair;
//import java.io.Externalizable;
//import java.io.IOException;
//import java.io.ObjectInput;
//import java.io.ObjectOutput;
//import java.nio.ByteBuffer;
//
///**
// *
// * @author REx
// */
//public class BPlusTree implements Presistance, Externalizable
//{
//    public static final String EXTENSION = "btree";
//    
//    /**
//     * the manager of all of the pages on the file system
//     */
//    private FileManager fileManager;
//    public FileManager getFileManager() { return fileManager; }
//    
//    /**
//     * the first pages id
//     */
//    private long root;
//    
//    /**
//     * this represents how many 'layers' there are of pages before
//     * it will get to a leaf node.
//     */
//    private int order;
//    
//    /**
//     * total number of records in the tree
//     */
//    private long recordCount;
//    public long getCount(){ return recordCount; }
//    
//    /**
//     * 
//     */
//    private String btreeName;
//    public String getBTreeName(){ return btreeName; }
//    
//    /**
//     * this is the number of records per page that are allowed. this
//     * is for leaf and non-leaf
//     */
//    private int recordsPerPage;
//    public int getRecordsPerPage(){ return recordsPerPage; }
//    
//    /**
//     * this keeps track of the BPlusPage ids
//     */
//    private long pageCountAt;
//    public long incrementPageCount(){ pageCountAt++; return pageCountAt; }
//    
//    /**
//     * the serializer for bring object into and out of a ByteBuffer
//     */
//    private Serializer<byte[]> valueSerializer;
//    public Serializer<byte[]> getValueSerializer(){ return valueSerializer; }
//    
//    private Serializer<byte[]> pageSerializer;
//    public Serializer<byte[]> getPageSerializer(){ return pageSerializer; }
//    
//    /**
//     * for serialization
//     */
//    public BPlusTree()
//    {
//        
//    }
//    
//    //public static BPlusTree 
//    
//    /**
//     * creation of a new BPlusTree
//     * @param workingDirectory 
//     */
//    public BPlusTree(
//            FileManager fileManager,
//            Serializer<byte[]> valueSerializer, 
//            String btreeName,
//            int recordsPerPage)
//    {
//        if (fileManager == null)
//        {
//            throw new NullPointerException("pageManager");
//        }
//        if (valueSerializer == null)
//        {
//            throw new NullPointerException("valueSerializer");
//        }
//        if (btreeName == null || btreeName.isEmpty())
//        {
//            throw new NullPointerException("btreeName");
//        }
//        if (recordsPerPage < 3)
//        {
//            throw new IllegalArgumentException(
//                    "recordsPerPage cannot be less than 3");
//        }
//        this.valueSerializer    = valueSerializer;
//        this.pageSerializer     = new BPlusPage.BPlusPageSerializer(this);
//        this.fileManager        = fileManager;
//        this.root               = BPlusPage.NULL_BUCKET;
//        this.recordsPerPage     = recordsPerPage;
//        this.order              = 0;
//        this.recordCount        = 0;
//        this.pageCountAt        = 0;
//        this.btreeName          = btreeName;
//    }
//
//    @Override
//    public Object select(long id) 
//            throws FledPresistanceException
//    {
//        BPlusPage rootPage = getRootPage();
//        if (rootPage == null)
//        {
//            // basically means there are no records
//            return null;
//        }
//        return rootPage.select(id);
//    }
//
//    @Override
//    public Browser<Pair<Long, Object>> browse(long id) 
//            throws FledPresistanceException
//    {
//        BPlusPage rootPage = getRootPage();
//        if (rootPage == null)
//        {
//            // basically means there are no records
//            return null;
//        }
//        
//        // get the browser
//        Browser<Pair<Long, Object>> browser = rootPage.browse(id);
//        
//        if (browser.valid())
//        {
//            return browser;
//        }
//        else
//        {
//            return null;
//        }
//    }
//
//    @Override
//    public Browser<Pair<Long, Object>> browse() 
//            throws FledPresistanceException
//    {
//        BPlusPage rootPage = getRootPage();
//        if (rootPage == null)
//        {
//            // basically means there are no records
//            return null;
//        }
//        
//        // get the browser
//        Browser<Pair<Long, Object>> browser = rootPage.browse();
//        
//        if (browser.valid())
//        {
//            return browser;
//        }
//        else
//        {
//            return null;
//        }
//    }
//
//    @Override
//    public Object insert(long id, Object record, boolean replace)
//            throws FledPresistanceException 
//    {
//        try
//        {
//            if (record == null)
//            {
//                throw new NullPointerException("record");
//            }
//
//            BPlusPage rootPage = getRootPage();
//
//            if (rootPage == null)
//            {
//                // this means the tree is empty and we are going 
//                // to do a first insert
//                BPlusPage newRoot = new BPlusPage(this, id, record);
//
//                // first record
//                recordCount = 1;
//                
//                root = newRoot.getThisBuckedId();
//
//                // save the file to disk
//                pageManager.savePage(
//                        buildPageId(newRoot.getThisBuckedId()), 
//                        newRoot, 
//                        pageSerializer);
//                
//                // save the tree to disk
//                pageManager.savePage(buildTreeId(), this, null);
//
//                // no object was overridden
//                return null;
//            }
//            else
//            {
//                InsertResult result = rootPage.insert(id, record, replace);
//                boolean treeNeedsUpdate = false;
//                
//                // 
//                if (result.overflowPage != null)
//                {
//                    BPlusPage newRoot = new BPlusPage(this, rootPage, result.overflowPage);
//                    root = newRoot.getThisBuckedId();
//                    pageManager.savePage(
//                            buildPageId(newRoot.getThisBuckedId()), 
//                            newRoot, 
//                            pageSerializer);
//                    order++;
//                    treeNeedsUpdate = true;
//                }
//                
//                // if there is no existing value, then that means it was 
//                // a new insert and we want to count that.
//                if (result.existing == null)
//                {
//                    recordCount++;
//                    treeNeedsUpdate = true;
//                }
//
//                if (treeNeedsUpdate)
//                {
//                    // @TODO make tree serializer
//                    pageManager.savePage(buildTreeId(), this, null);
//                }
//                return result.existing;
//            }
//        }
//        catch(Exception ex)
//        {
//            // @TODO statement
//            throw new FledPresistanceException(LanguageStatements.NONE, ex);
//        }
//    }
//
//    @Override
//    public Object delete(long id) 
//            throws FledPresistanceException
//    {
//        try
//        {
//            BPlusPage rootPage = getRootPage();
//            if (rootPage == null)
//            {
//                // @TODO statement
//                throw new FledStateException(LanguageStatements.NONE);
//            }
//            
//            boolean thisNeedsUpdate = false;
//            DeleteResult result = rootPage.delete(id);
//            
//            if (result.underflow && rootPage.compacityUsed() < 2)
//            {
//                thisNeedsUpdate = true;
//                
//                if (rootPage.compacityUsed() == 1)
//                {
//                    root = rootPage.getChildId(0);
//                    pageManager.deletePage(
//                            buildPageId(rootPage.getThisBuckedId()));
//                }
//                else
//                {
//                    root = -1;
//                    pageManager.deletePage(
//                            buildPageId(rootPage.getThisBuckedId()));
//                }
//            }
//            
//            if (result.removedValue != null)
//            {
//                thisNeedsUpdate = true;
//                
//                recordCount--;
//            }
//            
//            if (thisNeedsUpdate)
//            {
//                // @TODO make tree serializer
//                pageManager.savePage(buildTreeId(), this, null);
//            }
//            
//            return result.removedValue;
//        }
//        catch(Exception ex)
//        {
//            // @TODO statement
//            throw new FledPresistanceException(LanguageStatements.NONE, ex);
//        }
//    }
//    
//    private BPlusPage getRootPage() 
//            throws FledPresistanceException
//    {
//        if (root == BPlusPage.NULL_BUCKET)
//        {
//            return null;
//        }
//        return (BPlusPage) pageManager.getPage(buildPageId(root), pageSerializer);
//    }
//    
//    protected String buildPageId(long id)
//    {
//        return btreeName + String.valueOf(id) + "." + BPlusPage.EXTENSION;
//    }
//    
//    private String buildTreeId()
//    {
//        return btreeName + "." + BPlusTree.EXTENSION;
//    }
//
//    @Override
//    public void readExternal(ObjectInput in) 
//            throws  IOException, 
//                    ClassNotFoundException 
//    {
//        valueSerializer = (Serializer) in.readObject();
//        root            = in.readLong();
//        order           = in.readInt();
//        recordCount     = in.readLong();
//        recordsPerPage  = in.readInt();
//        pageCountAt     = in.readLong();
//        btreeName       = (String) in.readObject();
//    }
//
//    @Override
//    public void writeExternal(ObjectOutput out) 
//            throws IOException 
//    {
//        out.writeObject(valueSerializer);
//        out.writeLong(root);
//        out.writeInt(order);
//        out.writeLong(recordCount);
//        out.writeInt(recordsPerPage);
//        out.writeLong(pageCountAt);
//        out.writeObject(btreeName);
//    }
//}
