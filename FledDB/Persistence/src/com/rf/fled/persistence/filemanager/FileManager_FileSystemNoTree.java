/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.util.ByteSerializer;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.ISerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author REx
 */
public class FileManager_FileSystemNoTree implements IFileManager
{
    private String directory;
    
    private String context;
    
    private final Object LOCK;
    
    private long counter;

    public FileManager_FileSystemNoTree(
            String directory, 
            String context,
            long counter) 
    {
        this.directory  = directory;
        this.counter    = counter;
        this.context    = context;
        this.LOCK       = new Object();
    }

    @Override
    public long getFileCount() 
    {
        return counter;
    }

    @Override
    public String getDirectory() 
    {
        return directory;
    }

    @Override
    public Object loadFile(long id, ISerializer<byte[]> serializer) 
            throws FledPersistenceException
    {
        Object result = null;
        try
        {
            synchronized(LOCK)
            {
                result = loadFile0(buildFileName(id), serializer);
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an exception occurred during the loading file with id=" + id, 
                    ex);
        }
        return result;
    }

    @Override
    public long saveFile(Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = -1;
        try
        {
            synchronized(LOCK)
            {
                id = incFileCount();
                String filename = buildFileName(id);
                if ((new File(filename)).exists())
                {
                    // @TODO statement
                    throw new FledPersistenceException(
                            "file already exists: id is out of sync");
                }

                writeFile0(filename, data, serializer);
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an error occurred while trying to save a new file", 
                    ex);
        }
        return id;
    }

    @Override
    public void updateFile(long id, Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException
    {
        try
        {
            synchronized(LOCK)
            {
                String filename = buildFileName(id);
                writeFile0(filename, data, serializer);
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an error occurred while trying to save file with id=" + id, 
                    ex);
        }
    }

    @Override
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        try
        {
            synchronized(LOCK)
            {
                deleteFile0(buildFileName(id));
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an error occurred while trying to delete a file with id=" + id, 
                    ex);
        }
    }

    @Override
    public void saveNamedFile(String name, Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        try
        {
            synchronized(LOCK)
            {
                writeFile0(buildNamedFileName(name), data, serializer);
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an error occurred while trying to update the parent file", 
                    ex);
        }
    }

    @Override
    public Object loadNamedFile(String name, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object result = null;
        try
        {
            synchronized(LOCK)
            {
                result = loadFile0(buildNamedFileName(name), serializer);
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an exception occurred during the loading file with name=" + name, 
                    ex);
        }
        return result;
    }

    @Override
    public void deleteNamedFile(String name)
            throws FledPersistenceException 
    {
        try
        {
            synchronized(LOCK)
            {
                deleteFile0(buildNamedFileName(name));
            }
        }
        catch(Exception ex)
        {
            throw new FledPersistenceException(
                    "an error occurred while trying to delete a file with id=" + name, 
                    ex);
        }
    }
    
    @Override
    public long incFileCount()
    { 
        synchronized(LOCK)
        { 
            counter++;
            return counter; 
        }
    }

    protected String buildFileName(long id) 
    {
        return directory + "/" + context + "." + id + "." + IFileManager.EXTENSION;
    }
    
    protected String buildNamedFileName(String name)
    {
        return directory + "/" + context + "." + name + "." + IFileManager.EXTENSION;
    }
    
    private void deleteFile0(String filename) throws IOException
    {
        Files.deleteIfExists(Paths.get(filename));
    }
    
    private void writeFile0(String filename, Object data, ISerializer<byte[]> serializer)
            throws IOException
            
    {
        byte[] bytes = null;
        if (serializer == null)
        {
            bytes = ByteSerializer.serialize(data);
        }
        else
        {
            bytes = serializer.serialize(data);
        }
        File file = new File(filename);
        if (!file.exists())
        {
            if (!file.createNewFile())
            {
                throw new IOException("count not create file");
            }
        }
        FileOutputStream stream = new FileOutputStream(filename);
        try 
        {
            stream.write(bytes);
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch(Exception ex) { }
            }
        }
    }
    
    private Object loadFile0(String filename, ISerializer<byte[]> serializer)
            throws IOException, ClassNotFoundException
    {
        Object result = null;
        File file = new File(filename);
        if (!file.exists())
        {
            return null;
        }
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream stream = new FileInputStream(file);
        try
        {
            stream.read(bytes);
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch(Exception ex) { }
            }
        }
        if (serializer == null)
        {
            result = ByteSerializer.deserialize(bytes);
        }
        else
        {
            result = serializer.deserialize(bytes);
        }
        return result;
    }
}
