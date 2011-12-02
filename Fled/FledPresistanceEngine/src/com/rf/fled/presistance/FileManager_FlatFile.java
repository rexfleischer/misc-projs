/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import com.rf.fled.config.FledConfigOption;
import com.rf.fled.config.FledProperties;
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
public class FileManager_FlatFile implements FileManager
{
    private String directory;
    
    private Map<String, WeakReference<RecordFile>> openfiles;
    
    private Map<String, WeakReference<byte[]>> unknownfiles;
    
    private final Object LOCK;
    
    private FledProperties properties;
    
    public FileManager_FlatFile(FledProperties properties, String directory)
    {
        this.properties = properties;
        this.directory  = directory;
        this.LOCK       = new Object();
        this.openfiles  = new HashMap<String, WeakReference<RecordFile>>();
        this.unknownfiles = new HashMap<String, WeakReference<byte[]>>();
    }

    @Override
    public FileTransaction beginTransaction() 
            throws IOException 
    {
        return new FileTransaction(this);
    }
    
    @Override
    public RecordFile loadRecordFile(String id) 
            throws IOException
    {
        RecordFile result = null;
        String filename = buildFileName(id);
        synchronized(LOCK)
        {
            WeakReference<RecordFile> weak = openfiles.get(filename);
            if (weak != null)
            {
                result = weak.get();
            }
            
            if (result == null)
            {
                File file = new File(filename);
                if (!file.exists())
                {
                    throw new FileNotFoundException();
                }
                byte[] bytes = new byte[(int)file.length()];
                FileInputStream stream = new FileInputStream(file);
                stream.read(bytes);
                result = RecordFileFactory.valueOf(
                        properties.getProperty(
                            FledConfigOption.RECORD_FILE_DEFAULT.toString()))
                        .getExistingInstance(properties, filename);
                openfiles.put(id, new WeakReference<RecordFile>(result));
            }
        }
        return result;
    }

    @Override
    public void saveRecordFile(RecordFile data) 
            throws IOException 
    {
        synchronized(LOCK)
        {
            (new FileOutputStream(buildFileName(data.getId()))).write(data.getBytes());
        }
    }

    @Override
    public void deleteFile(String id) 
            throws IOException 
    {
        synchronized(LOCK)
        {
            (new File(buildFileName(id))).delete();
        }
    }

    @Override
    public String buildFileName(String id) 
            throws IOException 
    {
        return directory + "/" + id;
    }

    @Override
    public byte[] loadUnknownFile(String id) 
            throws IOException 
    {
        byte[] result = null;
        String filename = buildFileName(id);
        synchronized(LOCK)
        {
            WeakReference<RecordFile> weak = openfiles.get(filename);
            if (weak != null)
            {
                result = weak.get();
            }
            
            if (result == null)
            {
                File file = new File(filename);
                if (!file.exists())
                {
                    throw new FileNotFoundException();
                }
                byte[] bytes = new byte[(int)file.length()];
                FileInputStream stream = new FileInputStream(file);
                stream.read(bytes);
                result = RecordFileFactory.valueOf(
                        properties.getProperty(
                            FledConfigOption.RECORD_FILE_DEFAULT.toString()))
                        .getExistingInstance(properties, filename);
                openfiles.put(id, new WeakReference<RecordFile>(result));
            }
        }
        return result;
    }

    @Override
    public void saveUnknownFile(String id, byte[] data) 
            throws IOException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}