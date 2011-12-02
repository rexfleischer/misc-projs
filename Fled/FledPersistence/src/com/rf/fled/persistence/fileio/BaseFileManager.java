/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.fileio;

import com.rf.fled.config.FledProperties;
import com.rf.fled.exceptions.FledIOException;
import com.rf.fled.interfaces.Serializer;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.persistence.ByteSerializer;
import com.rf.fled.persistence.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class BaseFileManager implements FileManager
{
    private Map<String, WeakReference<Object>> openfiles;
    
    private FledProperties properties;
    
    private String directory;
    
    private final Object LOCK;
    
    private long fileCount;
    
    public BaseFileManager(FledProperties properties, int fileCount, String directory)
    {
        this.openfiles  = new HashMap<String, WeakReference<Object>>();
        this.properties = properties;
        this.directory  = directory;
        this.fileCount  = fileCount;
        LOCK = new Object();
    }

    @Override
    public FileManager beginTransaction() 
            throws FledIOException
    {
        return new SimpleTransaction(this);
    }
    
    @Override
    public void commit()
            throws FledIOException
    {
        throw new UnsupportedOperationException("this is not a transaction");
    }
    
    @Override
    public void rollback()
            throws FledIOException
    {
        throw new UnsupportedOperationException("this is not a transaction");
    }

    @Override
    public Object loadFile(long id, Serializer<byte[]> serializer) 
            throws FledIOException
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = loadFile0(buildFileName(id), serializer);
        }
        return result;
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledIOException 
    {
        long id = -1;
        synchronized(LOCK)
        {
            id = incFileCount();
            String filename = buildFileName(id);
            if ((new File(filename)).exists())
            {
                // @TODO statement
                throw new FledIOException(LanguageStatements.NONE);
            }
            
            writeFile0(filename, data, serializer);
        }
        return id;
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer) 
            throws FledIOException
    {
        synchronized(LOCK)
        {
            String filename = buildFileName(id);
            writeFile0(filename, data, serializer);
        }
    }

    @Override
    public void deleteFile(long id) 
            throws FledIOException 
    {
        synchronized(LOCK)
        {
            (new File(buildFileName(id))).delete();
        }
    }

    @Override
    public Object loadNamedFile(String name, Serializer<byte[]> serializer) 
            throws FledIOException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = loadFile0(buildNamedFileName(name), serializer);
        }
        return result;
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledIOException 
    {
        synchronized(LOCK)
        {
            writeFile0(buildNamedFileName(name), data, serializer);
        }
    }

    @Override
    public void deleteNamedFile(String name) 
            throws FledIOException 
    {
        synchronized(LOCK)
        {
            (new File(buildNamedFileName(name))).delete();
        }
    }
    
    @Override
    public long incFileCount()
    { 
        synchronized(LOCK)
        { 
            fileCount++; 
            return fileCount; 
        }
    }

    protected String buildFileName(long id) 
            throws FledIOException 
    {
        return directory + "/" + id + "." + FileManager.EXTENSION;
    }
    
    protected String buildNamedFileName(String name)
    {
        return directory + "/" + name + "." + FileManager.EXTENSION;
    }
    
    private void writeFile0(String filename, Object data, Serializer<byte[]> serializer)
            throws FledIOException
    {
        byte[] bytes = null;
        if (serializer == null)
        {
            try 
            {
                bytes = ByteSerializer.serialize(data);
            }  
            catch (IOException ex)  
            {
                // @TODO statement
                throw new FledIOException(LanguageStatements.NONE, ex);
            }
        }
        else
        {
            try
            {
                bytes = serializer.serialize(data);
            } 
            catch (IOException ex)
            {
                // @TODO statement
                throw new FledIOException(LanguageStatements.NONE, ex);
            }
        }
        File file = new File(filename);
        if (!file.exists())
        {
            try 
            {
                if (!file.createNewFile())
                {
                    // @TODO statement
                    throw new FledIOException(LanguageStatements.NONE);
                }
            } 
            catch (IOException ex) 
            {
                // @TODO statement
                throw new FledIOException(LanguageStatements.NONE, ex);
            }
        }
        FileOutputStream stream = null;
        try 
        {
            stream = new FileOutputStream(filename);
            stream.write(bytes);
        } 
        catch (FileNotFoundException ex) 
        {
            // @TODO statement
            throw new FledIOException(LanguageStatements.NONE, ex);
        } 
        catch (IOException ex) 
        {
            // @TODO statement
            throw new FledIOException(LanguageStatements.NONE, ex);
        }
    }
    
    private Object loadFile0(String filename, Serializer<byte[]> serializer)
            throws FledIOException
    {
        Object result = null;
        WeakReference<Object> weak = openfiles.get(filename);
        if (weak != null)
        {
            result = weak.get();
        }

        if (result == null)
        {
            File file = new File(filename);
            if (!file.exists())
            {
                return null;
            }
            byte[] bytes = new byte[(int)file.length()];
            FileInputStream stream = null;
            try
            {
                stream = new FileInputStream(file);
            }
            catch(Exception ex)
            {
                return null;
            }
            try
            {
                stream.read(bytes);
            }
            catch(IOException ex)
            {
                // @TODO statement
                throw new FledIOException(LanguageStatements.NONE, ex);
            }
            if (serializer == null)
            {
                try
                {
                    result = ByteSerializer.deserialize(bytes);
                }
                catch (IOException ex) 
                {
                    // @TODO statement
                    throw new FledIOException(LanguageStatements.NONE, ex);
                } 
                catch (ClassNotFoundException ex) 
                {
                    // @TODO statement
                    throw new FledIOException(LanguageStatements.NONE, ex);
                }
            }
            else
            {
                try 
                {
                    result = serializer.deserialize(bytes);
                } 
                catch (IOException ex) 
                {
                    // @TODO statement
                    throw new FledIOException(LanguageStatements.NONE, ex);
                }
            }
            openfiles.put(filename, new WeakReference<Object>(result));
        }
        return result;
    }
}
