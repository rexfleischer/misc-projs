/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.transaction.SimpleTransaction;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Serializer;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author REx
 */
public class FileManager_InMemory extends Observable implements FileManager
{
    private HashMap<Long, Object> keyedFiles;
    
    private HashMap<String, Object> namedFiles;
    
    private Long count;
    
    public FileManager_InMemory()
    {
        keyedFiles  = new HashMap<Long, Object>();
        namedFiles  = new HashMap<String, Object>();
        count       = 0l;
    }

    @Override
    public long incFileCount() 
    {
        long result = 0;
        synchronized(count)
        {
            count++;
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
        return keyedFiles.get(id);
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException
    {
        keyedFiles.put(id, data);
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = incFileCount();
        keyedFiles.put(id, data);
        return id;
    }

    @Override
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        keyedFiles.remove(id);
    }

    @Override
    public Object loadNamedFile(String name, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        return namedFiles.get(name);
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        namedFiles.put(name, data);
    }

    @Override
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        namedFiles.remove(name);
    }
    
}
