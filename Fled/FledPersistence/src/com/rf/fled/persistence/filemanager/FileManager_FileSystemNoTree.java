/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.transaction.SimpleTransaction;
import com.rf.fled.persistence.fileio.ByteSerializer;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.localization.LanguageStatements;
import com.rf.fled.persistence.localization.Languages;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;

/**
 *
 * @author REx
 */
public class FileManager_FileSystemNoTree extends Observable implements FileManager
{
    private String directory;
    
    private String context;
    
    private final Object LOCK;
    
    private long fileCount;
    
    public FileManager_FileSystemNoTree(
            String directory, String context, long fileCount)
    {
        this.directory  = directory;
        this.fileCount  = fileCount;
        this.context    = context;
        LOCK = new Object();
    }
    
    public FileManager_FileSystemNoTree(
            String directory, long fileCount)
    {
        this.directory  = directory;
        this.fileCount  = fileCount;
        this.context    = null;
        LOCK = new Object();
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
        throw new UnsupportedOperationException("this is not a transaction");
    }
    
    @Override
    public void rollback()
            throws FledTransactionException
    {
        throw new UnsupportedOperationException("this is not a transaction");
    }

    @Override
    public Object loadFile(long id, Serializer<byte[]> serializer) 
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
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
        return result;
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
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
                            LanguageStatements.NONE.toString());
                }

                writeFile0(filename, data, serializer);
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
        return id;
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer) 
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
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
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
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public Object loadNamedFile(String name, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        Object result = null;
        try
        {
            synchronized(LOCK)
            {
                result = loadFile0(buildFileName(name), serializer);
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
        return result;
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        try
        {
            synchronized(LOCK)
            {
                writeFile0(buildFileName(name), data, serializer);
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        try
        {
            synchronized(LOCK)
            {
                deleteFile0(buildFileName(name));
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }
    
    @Override
    public long incFileCount()
    { 
        synchronized(LOCK)
        { 
            fileCount++;
            if (context != null)
            {
                FileManagerUpdate update = new FileManagerUpdate();
                update.updateType = FileManagerUpdateType.RECORD_COUNT_AT;
                update.context = context;
                update.info = fileCount;
                notifyObservers(update);
            }
            return fileCount; 
        }
    }

    protected String buildFileName(long id) 
    {
        return directory + "/" + id + "." + FileManager.EXTENSION;
    }
    
    protected String buildFileName(String name)
    {
        return directory + "/" + name + "." + FileManager.EXTENSION;
    }
    
    private void deleteFile0(String filename) throws IOException
    {
        Files.deleteIfExists(Paths.get(filename));
    }
    
    private void writeFile0(String filename, Object data, Serializer<byte[]> serializer)
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
                // @TODO statement
                throw new IOException(Languages.sts(LanguageStatements.NONE));
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
    
    private Object loadFile0(String filename, Serializer<byte[]> serializer)
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
