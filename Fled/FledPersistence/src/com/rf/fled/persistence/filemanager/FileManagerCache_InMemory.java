/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.util.SoftHashMap;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Serializer;

/**
 *
 * @author REx
 */
public class FileManagerCache_InMemory implements FileManager
{
    private SoftHashMap cache;
    
    private FileManager parent;
    
    private final Object LOCK;
    
    public FileManagerCache_InMemory(FileManager parent)
    {
        this.parent = parent;
        this.cache  = new SoftHashMap();
        this.LOCK   = new Object();
    }

    @Override
    public Object loadFile(long id, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            cache.get(id);
        }
        if (result == null)
        {
            // we dont want to syncronize a file read here
            result = parent.loadFile(id, serializer);
            synchronized(LOCK)
            {
                cache.put(id, result);
            }
        }
        return result;
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = parent.saveFile(data, serializer);
        synchronized(LOCK)
        {
            cache.put(id, data);
        }
        return id;
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        parent.updateFile(id, data, serializer);
        synchronized(LOCK)
        {
            cache.put(id, data);
        }
    }

    @Override
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        parent.deleteFile(id);
        synchronized(LOCK)
        {
            cache.remove(id);
        }
    }

    @Override
    public void updateParentFile(Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException
    {
        parent.updateParentFile(data, serializer);
    }

    @Override
    public FileManager beginTransaction() 
            throws FledTransactionException 
    {
        return new FileManagerCache_InMemory(parent.beginTransaction());
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        parent.commit();
    }

    @Override
    public void rollback()
            throws FledTransactionException
    {
        parent.rollback();
    }

    @Override
    public long incFileCount() 
    {
        return parent.incFileCount();
    }

    @Override
    public long getFileCount() 
    {
        return parent.getFileCount();
    }

    @Override
    public String getDirectory() 
    {
        return parent.getDirectory();
    }
}
