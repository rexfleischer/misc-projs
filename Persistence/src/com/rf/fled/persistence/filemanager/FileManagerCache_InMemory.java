/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.util.SoftHashMap;

/**
 *
 * @author REx
 */
public class FileManagerCache_InMemory implements IFileManager
{
    private SoftHashMap cache;
    
    private IFileManager parent;
    
    private final Object LOCK;
    
    public FileManagerCache_InMemory(IFileManager parent)
    {
        this.parent = parent;
        this.cache  = new SoftHashMap();
        this.LOCK   = new Object();
    }

    @Override
    public Object loadFile(long id, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = cache.get(id);
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
    public long saveFile(Object data, ISerializer<byte[]> serializer) 
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
    public void updateFile(long id, Object data, ISerializer<byte[]> serializer) 
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
    public void saveNamedFile(String name, Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        parent.saveNamedFile(name, data, serializer);
        synchronized(LOCK)
        {
            cache.put(name, data);
        }
    }

    @Override
    public Object loadNamedFile(String name, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = cache.get(name);
        }
        if (result == null)
        {
            // we dont want to syncronize a file read here
            result = parent.loadNamedFile(name, serializer);
            synchronized(LOCK)
            {
                cache.put(name, result);
            }
        }
        return result;
    }

    @Override
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            cache.remove(name);
        }
        parent.deleteNamedFile(name);
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
