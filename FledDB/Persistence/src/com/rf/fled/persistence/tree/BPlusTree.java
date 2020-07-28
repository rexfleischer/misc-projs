/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.util.ByteSerializer;
import com.rf.fled.persistence.filemanager.IFileManager;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.filemanager.FileManagerContext;
import com.rf.fled.persistence.filemanager.FileManagerFactory;
import com.rf.fled.persistence.transaction.ITransactionalRecord;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author REx
 */
public class BPlusTree implements IPersistence, ITransactionalRecord, Externalizable
{
    public static final Integer NULL_PAGE = 0;
    
    public static final Integer DEFAULT_RECORD_COUNT = 16;
    
    protected ISerializer<byte[]> valueSerailizer;
    
    protected ISerializer<byte[]> pageSerializer;
    
    protected IFileManager fileManager;
    
    protected FileManagerContext fileManagerContext;
    
    protected String context;
    
    protected int maxRecords;
    
    protected int height;
    
    protected long count;

    protected long root;
    
    private ReadWriteLock locking;
    
    @Override
    public Lock getReadLock()
    {
        return locking.readLock();
    }
    
    @Override
    public Lock getWriteLock()
    {
        return locking.writeLock();
    }
    
    private void initLocking()
    {
        if (locking == null)
        {
            locking = new ReentrantReadWriteLock(true);
        }
    }
    
    public static BPlusTree createBPlusTree(
            String context,
            int maxRecords,
            IFileManager fileManager,
            FileManagerContext fileManagerContext,
            ISerializer<byte[]> valueSerailizer,
            ISerializer<byte[]> pageSerializer) 
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
        result.fileManagerContext = fileManagerContext;
        result.valueSerailizer  = valueSerailizer;
        result.pageSerializer   = pageSerializer;
        result.fileManager      = fileManager;
        result.maxRecords       = maxRecords;
        result.root             = NULL_PAGE;
        result.count            = 0;
        result.height           = 0;
        result.context          = context;
        result.initLocking();
        
        result.fileManager.saveNamedFile(result.context, result, null);
        return result;
    }
    
    public BPlusTree()
    {
        initLocking();
    }
    
    /**
     * for deep copy
     * @param origin 
     */
    private BPlusTree(BPlusTree origin, IFileManager manager)
    {
        fileManagerContext = origin.fileManagerContext;
        valueSerailizer  = origin.valueSerailizer;
        pageSerializer   = origin.pageSerializer;
        fileManager      = manager;
        maxRecords       = origin.maxRecords;
        root             = origin.root;
        count            = origin.count;
        height           = origin.height;
        context          = origin.context;
        initLocking();
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
    public IBrowser<KeyValuePair<Long, Object>> browse(long id)
            throws FledPersistenceException 
    {
        getReadLock().lock();
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                // basically the tree is empty
                getReadLock().unlock();
                return null;
            }
            return rootPage.browse(id);
        }
        catch(Exception ex)
        {
            getReadLock().unlock();
            throw new FledPersistenceException(
                    "error while initiating a browse with id=" + id, ex);
        }
    }

    @Override
    public IBrowser<KeyValuePair<Long, Object>> browse()
            throws FledPersistenceException 
    {
        getReadLock().lock();
        try
        {
            BPlusPage rootPage = getRoot();
            if (rootPage == null)
            {
                // basically the tree is empty
                getReadLock().unlock();
                return null;
            }
            return rootPage.browse();
        }
        catch(Exception ex)
        {
            getReadLock().unlock();
            throw new FledPersistenceException(
                    "error while initiating a browse", ex);
        }
    }

    @Override
    public Object select(long id) 
            throws FledPersistenceException 
    {
        getReadLock().lock();
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
            throw new FledPersistenceException(
                    "error during a select for id=" + id, ex);
        }
        finally
        {
            getReadLock().unlock();
        }
    }

    @Override
    public boolean update(long id, Object record) 
            throws FledPersistenceException 
    {
        getWriteLock().lock();
        try
        {
            BPlusPage rootPage = getRoot();
            
            if (rootPage == null)
            {
                return false;
            }
            else
            {
                return rootPage.update(id, record);
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "error occurred while updating an object with id=" + id, ex);
        }
        finally
        {
            getWriteLock().unlock();
        }
    }

    @Override
    public Object insert(long id, Object record, boolean replace) 
            throws FledPersistenceException 
    {
        getWriteLock().lock();
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
                    fileManagerContext.counter = fileManager.getFileCount();
                    fileManager.saveNamedFile(context, this, null);
                }
                
                return result.existing;
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "error occurred while inserting an object with id=" + id, ex);
        }
        finally
        {
            getWriteLock().unlock();
        }
    }

    @Override
    public Object delete(long id) 
            throws FledPersistenceException 
    {
        getWriteLock().lock();
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
                fileManager.saveNamedFile(context, this, null);
            }
            
            return result.removedValue;
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "error while delete of object with id=" + id, ex);
        }
        finally
        {
            getWriteLock().unlock();
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
        page.commitMeta();
        fileManager.updateFile(page.thisId, page, pageSerializer);
    }

    void deletePage(BPlusPage child) 
            throws FledPersistenceException 
    {
        fileManager.deleteFile(child.thisId);
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
            catch(ClassNotFoundException ex)
            {
                throw new IOException(ex);
            }
        }
        else
        {
            return this.valueSerailizer.deserialize(data);
        }
    }

    @Override
    public void truncate() 
            throws FledPersistenceException 
    {
        getWriteLock().lock();
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
            throw new FledPersistenceException(
                    "error during truncate", ex);
        }
        finally
        {
            getWriteLock().unlock();
        }
    }

    @Override
    public void drop() 
            throws FledPersistenceException 
    {
        this.truncate();
        fileManager.deleteNamedFile(context);
    }

    @Override
    public ITransactionalRecord transaction() 
    {
        return new BPlusTree(this, null);
    }

    @Override
    public void setFileManager(IFileManager fileManager) 
    {
        this.fileManager = fileManager;
    }

    @Override
    public IFileManager getFileManager() 
    {
        return fileManager;
    }
    
    @Override
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException 
    {
        valueSerailizer = (ISerializer<byte[]>) in.readObject();
        pageSerializer  = (ISerializer<byte[]>) in.readObject();
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
