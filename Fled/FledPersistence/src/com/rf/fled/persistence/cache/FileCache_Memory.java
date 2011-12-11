/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.cache;

import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Serializer;

/**
 *
 * @author REx
 */
public class FileCache_Memory implements FileManager
{
    private SoftHashMap cache;
    
    private FileManager parent;
    
    private final Object LOCK;
    
    public FileCache_Memory(FileManager parent)
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
    public Object loadNamedFile(String name, Serializer<byte[]> serializer)
            throws FledPersistenceException
    {
        Object result = null;
        synchronized(LOCK)
        {
            cache.get(name);
        }
        if (result == null)
        {
            result = parent.loadNamedFile(name, serializer);
            synchronized(LOCK)
            {
                cache.put(name, result);
            }
        }
        return result;
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        parent.saveNamedFile(name, data, serializer);
        synchronized(LOCK)
        {
            cache.put(name, data);
        }
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
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        parent.deleteNamedFile(name);
        synchronized(LOCK)
        {
            cache.remove(name);
        }
    }

    @Override
    public FileManager beginTransaction() 
            throws FledTransactionException 
    {
        return new FileCache_Memory(parent.beginTransaction());
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
}
