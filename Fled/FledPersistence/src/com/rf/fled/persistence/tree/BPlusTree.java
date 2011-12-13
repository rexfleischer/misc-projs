/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.util.ByteSerializer;
import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.filemanager.FileManagerContext;
import com.rf.fled.persistence.filemanager.FileManagerFactory;
import com.rf.fled.persistence.transaction.Transactional;
import com.rf.fled.persistence.localization.LanguageStatements;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author REx
 */
public class BPlusTree implements Persistence, Transactional, Externalizable
{
    public static final Integer NULL_PAGE = 0;
    
    public static final Integer DEFAULT_RECORD_COUNT = 16;
    
    protected Serializer<byte[]> valueSerailizer;
    
    protected Serializer<byte[]> pageSerializer;
    
    protected FileManager fileManager;
    
    protected FileManagerContext fileManagerContext;
    
    protected int maxRecords;
    
    protected int height;
    
    protected long count;

    protected long root;
    
    public static BPlusTree createBPlusTree(
            int maxRecords,
            FileManager fileManager,
            FileManagerContext fileManagerContext,
            Serializer<byte[]> valueSerailizer,
            Serializer<byte[]> pageSerializer) 
            throws FledPersistenceException
    {
        if (fileManager == null)
        {
            throw new NullPointerException("fileManager");
        }
        if (fileManagerContext == null)
        {
            throw new NullPointerException("fileManagerContext");
        }
        if (maxRecords < 4)
        {
            throw new IllegalArgumentException(
                    "maxRecords cannot be less than 4");
        }
        if (valueSerailizer == null)
        {
            valueSerailizer =  new BPlusDefaultValueSerializer();
        }
        if (pageSerializer == null)
        {
            pageSerializer =  new BPlusDefaultPageSerializer();
        }
        BPlusTree result =  new BPlusTree();
        result.valueSerailizer  = valueSerailizer;
        result.pageSerializer   = pageSerializer;
        result.fileManager      = fileManager;
        result.maxRecords       = maxRecords;
        result.count    = 0;
        result.root     = 0;
        
        result.fileManager.updateParentFile(result, null);
        return result;
    }
    
    public BPlusTree()
    {
        
    }
    
    @Override
    public long size()
            throws FledPersistenceException
    {
        return count;
    }

    @Override
    public String getContext() 
            throws FledPersistenceException 
    {
        return fileManagerContext.context;
    }

    @Override
    public Browser<KeyValuePair<Long, Object>> browse(long id)
            throws FledPersistenceException 
    {
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                // basically the tree is empty
                return null;
            }
            return rootPage.browse(id);
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public Browser<KeyValuePair<Long, Object>> browse()
            throws FledPersistenceException 
    {
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                // basically the tree is empty
                return null;
            }
            return rootPage.browse();
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public Object select(long id) 
            throws FledPersistenceException 
    {
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                // basically the tree is empty
                return null;
            }
            return rootPage.select(id);
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public Object insert(long id, Object record, boolean replace) 
            throws FledPersistenceException 
    {
        try
        {
            BPlusPage rootPage = getRoot();
            
            if (rootPage == null)
            {
                rootPage = new BPlusPage(this, id, record);
                root = rootPage.getThisId();
                count = 1;
                
                fileManager.updateParentFile(this, null);
                
                // didnt replace anything
                return null;
            }
            else
            {
                BPlusInsertResult result = rootPage.insert(id, record, replace);
                boolean dirty = false;
                
                if (result.overflowPage != null)
                {
                    // this means that root page split and a new one needs
                    // to be put in its place
                    BPlusPage newRoot = new BPlusPage(
                            this, rootPage, result.overflowPage);
                    root = newRoot.getThisId();
                    height++;
                    dirty = true;
                }
                
                if (result.existing == null)
                {
                    count++;
                    dirty = true;
                }
                
                if (dirty)
                {
                    fileManager.updateParentFile(this, null);
                }
                
                return result.existing;
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public Object delete(long id) 
            throws FledPersistenceException 
    {
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                // basically, the tree is empty
                return null;
            }
            
            BPlusDeleteResult result = rootPage.delete(id);
            boolean dirty = false;
            
            if (result.underflow && rootPage.size() < 2)
            {
                dirty = true;
                if (rootPage.size() == 1)
                {
                    if (!rootPage.isLeaf)
                    {
                        root = rootPage.getChildId(0);
                        deletePage(rootPage);
                    }
                }
                else
                {
                    root = NULL_PAGE;
                    deletePage(rootPage);
                }
                height--;
            }
            
            if (result.removedValue != null)
            {
                count--;
                dirty = true;
            }
            
            if (dirty)
            {
                fileManager.updateParentFile(this, null);
            }
            
            return result.removedValue;
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public FileManager getFileManager() 
    {
        return fileManager;
    }

    @Override
    public void beginTransaction() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException();
    }
    
    void savePage(BPlusPage page) throws FledPersistenceException
    {
        fileManager.updateFile(page.thisId, page, pageSerializer);
    }

    void deletePage(BPlusPage child) 
            throws FledPersistenceException 
    {
        fileManager.deleteFile(child.thisId);
    }
    
    byte[] serializeValue(Object value) throws IOException
    {
        if (this.valueSerailizer == null)
        {
            return ByteSerializer.serialize(value);
        }
        else
        {
            return this.valueSerailizer.serialize(value);
        }
    }
    
    Object deserializeValue(byte[] data) 
            throws IOException
    {
        if (this.valueSerailizer == null)
        {
            try
            {
                return ByteSerializer.deserialize(data);
            }
            catch(Exception ex)
            {
                throw new IOException(ex);
            }
        }
        else
        {
            return this.valueSerailizer.deserialize(data);
        }
    }

    BPlusPage loadPage(long id) 
            throws FledPersistenceException 
    {
        BPlusPage page = (BPlusPage) fileManager.loadFile(id, pageSerializer);
        if (page == null)
        {
            return null;
        }
        page.bplustree = this;
        page.thisId = id;
        return page;
    }
    
    protected BPlusPage getRoot() 
            throws FledPersistenceException
    {
        if (root == BPlusTree.NULL_PAGE)
        {
            return null;
        }
        return loadPage(root);
    }

    @Override
    public void truncate() 
            throws FledTransactionException 
    {
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                return;
            }
            rootPage.truncate(height);
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledTransactionException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public Transactional deepCopy(FileManager newManager) 
    {
        BPlusTree result = new BPlusTree();
        result.valueSerailizer  = valueSerailizer;
        result.pageSerializer   = pageSerializer;
        result.fileManager      = newManager;
        result.maxRecords       = maxRecords;
        result.height           = height;
        result.count            = count;
        result.root             = root;
        return result;
    }
    
    @Override
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException 
    {
        valueSerailizer = (Serializer<byte[]>) in.readObject();
        pageSerializer  = (Serializer<byte[]>) in.readObject();
        count           = in.readLong();
        root            = in.readLong();
        maxRecords      = in.readInt();
        height          = in.readInt();
        
        FileManagerContext fmContext = (FileManagerContext) in.readObject();
        fileManager = FileManagerFactory.create(fmContext, null);
    }

    @Override
    public void writeExternal(ObjectOutput out)
            throws IOException 
    {
        out.writeObject(valueSerailizer);
        out.writeObject(pageSerializer);
        out.writeLong(count);
        out.writeLong(root);
        out.writeInt(maxRecords);
        out.writeInt(height);
        out.writeObject(fileManagerContext);
    }
    
    public void assertOrder(int tolerance) throws Exception
    {
        BPlusPage rootPage = getRoot();
        if (rootPage != null)
        {
            rootPage.assertOrder(null, tolerance);
        }
    }
    
    public void dump() throws Exception
    {
        BPlusPage rootPage = getRoot();
        if (rootPage != null)
        {
            rootPage.dumpRecursive(0);
        }
    }
    
    public void assertValues() throws Exception
    {
        BPlusPage rootPage = getRoot();
        if (rootPage != null)
        {
            rootPage.assertValues();
        } 
    }
}
