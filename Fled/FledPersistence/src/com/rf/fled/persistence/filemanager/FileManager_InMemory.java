/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.transaction.SimpleTransaction;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Serializer;
import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author REx
 */
public class FileManager_InMemory extends Observable implements FileManager
{
    private HashMap<Object, Object> files;
    
    private String context;
    
    private Long count;
    
    private final Object LOCK;
    
    public FileManager_InMemory(String context)
    {
        this.files  = new HashMap<Object, Object>();
        this.count  = 0l;
        this.context= context;
        this.LOCK   = new Object();
    }
    
    public FileManager_InMemory()
    {
        this(null);
    }

    @Override
    public long incFileCount() 
    {
        long result = 0;
        synchronized(LOCK)
        {
            count++;
            if (context != null)
            {
                FileManagerUpdate update = new FileManagerUpdate();
                update.updateType = FileManagerUpdateType.RECORD_COUNT_AT;
                update.context = context;
                update.info = count;
                notifyObservers(update);
            }
            result = count;
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
    public Object loadNamedFile(String name, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = files.get(name);
        }
        return result;
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            files.put(name, data);
        }
    }

    @Override
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            files.remove(name);
        }
    }
    
}
