/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.fileio.ByteSerializer;
import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.transaction.Transactionable;
import com.rf.fled.persistence.localization.LanguageStatements;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author REx
 */
public class BPlusTree implements Persistence, Transactionable, Externalizable
{
    public static final Integer NULL_PAGE = 0;
    
    public static final Integer DEFAULT_RECORD_COUNT = 16;
    
    protected Serializer<byte[]> valueSerailizer;
    
    protected Serializer<byte[]> pageSerializer;
    
    protected FileManager fileManager;
    
    protected int maxRecords;
    
    protected int height;
    
    private String context;
    
    private long count;

    private long root;
    
    public static BPlusTree createBPlusTree(
            FileManager fileManager,
            String treeName)
            throws FledPersistenceException
    {
        return createBPlusTree(fileManager, treeName, DEFAULT_RECORD_COUNT, null, null);
    }
    
    public static BPlusTree createBPlusTree(
            FileManager fileManager,
            String treeName,
            int recordsPerPage,
            Serializer<byte[]> valueSerailizer,
            Serializer<byte[]> pageSerializer)
            throws FledPersistenceException
    {
        if (fileManager == null)
        {
            throw new NullPointerException("fileManager");
        }
        if (treeName == null || treeName.isEmpty())
        {
            throw new NullPointerException("treeName");
        }
        if (recordsPerPage < 4)
        {
            throw new IllegalArgumentException(
                    "recordsPerPage cannot be less than 4");
        }
        if (pageSerializer == null)
        {
            pageSerializer =  new BPlusPage.BPlusPageSerializer();
        }
        if (valueSerailizer == null)
        {
            valueSerailizer =  new BPlusTree.DefaultValueSerializer();
        }
        BPlusTree result =  new BPlusTree();
        result.valueSerailizer  = valueSerailizer;
        result.pageSerializer   = pageSerializer;
        result.fileManager      = fileManager;
        result.maxRecords       = recordsPerPage;
        result.context          = treeName;
        result.count    = 0;
        result.root     = 0;
        
        fileManager.saveNamedFile(treeName, result, null);
        return result;
    }
    
    public static BPlusTree loadBPlusTree(
            FileManager fileManager, 
            String treeName) 
            throws FledPersistenceException
    {
        BPlusTree result = (BPlusTree) fileManager.loadNamedFile(treeName, null);
        result.fileManager = fileManager;
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
        return context;
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
                
                fileManager.saveNamedFile(context, this, null);
                
                // didnt replace anything
                return null;
            }
            else
            {
                InsertResult result = rootPage.insert(id, record, replace);
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
                    fileManager.saveNamedFile(context, this, null);
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
            
            DeleteResult result = rootPage.delete(id);
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
                fileManager.saveNamedFile(context, this, null);
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
    public Transactionable deepCopy(FileManager newManager) 
    {
        BPlusTree result = new BPlusTree();
        result.valueSerailizer  = valueSerailizer;
        result.pageSerializer   = pageSerializer;
        result.fileManager      = newManager;
        result.maxRecords       = maxRecords;
        result.height           = height;
        result.context          = context;
        result.count            = count;
        result.root             = root;
        return result;
    }
    
    @Override
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException 
    {
        context         = (String) in.readObject();
        valueSerailizer = (Serializer<byte[]>) in.readObject();
        pageSerializer  = (Serializer<byte[]>) in.readObject();
        count           = in.readLong();
        root            = in.readLong();
        maxRecords      = in.readInt();
        height          = in.readInt();
    }

    @Override
    public void writeExternal(ObjectOutput out)
            throws IOException 
    {
        out.writeObject(context);
        out.writeObject(valueSerailizer);
        out.writeObject(pageSerializer);
        out.writeLong(count);
        out.writeLong(root);
        out.writeInt(maxRecords);
        out.writeInt(height);
    }
    
    public static class DefaultValueSerializer implements Serializer<byte[]>
    {
        @Override
        public byte[] serialize(Object data)
            throws IOException
        {
            ObjectOutputStream out = null;
            ByteArrayOutputStream bytes = null;
            try
            {
                bytes = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bytes);
                out.writeObject(data);
            }
            finally
            {
                if (out != null)
                    out.close();
                if (bytes != null)
                    bytes.close();
            }   
            return bytes.toByteArray();
        }

        @Override
        public Object deserialize(byte[] data) 
                throws IOException
        {
            Object result = null;
            ObjectInputStream in = null;
            ByteArrayInputStream input = null;
            try
            {
                input = new ByteArrayInputStream(data);
                in = new ObjectInputStream(input);
                result = in.readObject();
            }
            catch(ClassNotFoundException ex)
            {
                throw new IOException("class not found");
            }
            finally
            {
                if (in != null)
                    in.close();
                if (input != null)
                    input.close();
            }
            return result;
        }
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
