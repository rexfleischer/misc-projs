/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.cache;

import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Serializer;

/**
 *
 * @author REx
 */
public class FileCache_Memory implements FileManager
{
    private SoftHashMap idCache;
    
    private SoftHashMap nameCache;
    
    private FileManager parent;
    
    public FileCache_Memory(FileManager parent)
    {
        this.parent = parent;
    }

    @Override
    public Object loadFile(long id, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object cache = idCache.get(id);
        if (cache == null)
        {
            cache = parent.loadFile(id, serializer);
            idCache.put(id, cache);
        }
        return cache;
    }

    @Override
    public Object loadNamedFile(String name, Serializer<byte[]> serializer)
            throws FledPersistenceException
    {
        Object cache = nameCache.get(name);
        if (cache == null)
        {
            cache = parent.loadNamedFile(name, serializer);
            idCache.put(name, cache);
        }
        return cache;
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        parent.saveNamedFile(name, data, serializer);
        nameCache.put(name, data);
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = parent.saveFile(data, serializer);
        idCache.put(id, data);
        return id;
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        parent.updateFile(id, data, serializer);
        idCache.put(id, data);
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
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        parent.deleteFile(id);
        idCache.remove(id);
    }

    @Override
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        parent.deleteNamedFile(name);
        nameCache.remove(name);
    }

    @Override
    public long incFileCount() 
    {
        return parent.incFileCount();
    }
}
