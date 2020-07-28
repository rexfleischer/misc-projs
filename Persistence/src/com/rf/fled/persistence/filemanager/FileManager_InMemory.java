/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.util.ByteSerializer;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author REx
 */
public class FileManager_InMemory implements IFileManager
{
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
    public Object loadFile(long id, ISerializer<byte[]> serializer)
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
//            result = files.get(id);
            try 
            {
                if (serializer == null)
                {
                    result = ByteSerializer.deserialize((byte[]) files.get(id));
                }
                else
                {
                    result = serializer.deserialize((byte[]) files.get(id));
                }
            } 
            catch (IOException ex) 
            {
                throw new FledPersistenceException("errro", ex);
            }
            catch (ClassNotFoundException ex)
            {
                throw new FledPersistenceException("errro", ex);
            }
        }
        return result;
    }

    @Override
    public void updateFile(long id, Object data, ISerializer<byte[]> serializer)
            throws FledPersistenceException
    {
        synchronized(LOCK)
        {
//            files.put(id, data);
            try
            {
                if (serializer == null)
                {
                    files.put(id, ByteSerializer.serialize(data));
                }
                else
                {
                    files.put(id, serializer.serialize(data));
                }
            }
            catch(Exception ex)
            {
                throw new FledPersistenceException("updateFile", ex);
            }
        }
    }

    @Override
    public long saveFile(Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = incFileCount();
        synchronized(LOCK)
        {
//            files.put(id, data);
            try
            {
                if (serializer == null)
                {
                    files.put(id, ByteSerializer.serialize(data));
                }
                else
                {
                    files.put(id, serializer.serialize(data));
                }
            }
            catch(Exception ex)
            {
                throw new FledPersistenceException("saveFile", ex);
            }
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
    public void saveNamedFile(String name, Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            files.put(name, data);
        }
    }

    @Override
    public Object loadNamedFile(String name, ISerializer<byte[]> serializer) 
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
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        synchronized(LOCK)
        {
            files.remove(name);
        }
    }
}
