/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Serializer;
import java.util.HashMap;

/**
 *
 * @author REx
 */
public class FileManager_InMemory implements FileManager
{
    public static final String PARENT_NAME = "parent";
    
    private HashMap<Object, Object> files;
    
    private Long counter;
    
    private final Object LOCK;

    public FileManager_InMemory() 
    {
        // nothing to do with context
        this.files  = new HashMap<Object, Object>();
        this.counter  = 0l;
        this.LOCK   = new Object();
    }

    @Override
    public long getFileCount() 
    {
        return counter;
    }

    @Override
    public String getDirectory() 
    {
        return "";
    }

    @Override
    public long incFileCount() 
    {
        long result = 0;
        synchronized(LOCK)
        {
            counter++;
            result = counter;
        }
        return result;
    }

    @Override
    public FileManager beginTransaction()
            throws FledTransactionException 
    {
        return new SimpleTransaction(this);
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

    @Override
    public Object loadFile(long id, Serializer<byte[]> serializer)
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = files.get(id);
        }
        return result;
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException
    {
        synchronized(LOCK)
        {
            files.put(id, data);
        }
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = incFileCount();
        synchronized(LOCK)
        {
            files.put(id, data);
        }
        return id;
    }

    @Override
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            files.remove(id);
        }
    }

    @Override
    public void updateParentFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            files.put(PARENT_NAME, data);
        }
    }
}
