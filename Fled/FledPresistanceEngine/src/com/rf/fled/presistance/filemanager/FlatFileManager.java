/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.filemanager;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.interfaces.Serializer;
import com.rf.fled.language.LanguageStatements;
import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class FlatFileManager
{
    private final Object LOCK;
    
    private String workingDir;
    
    private Map<String, WeakReference<Object>> openPages;
    
    public FlatFileManager(String workingDir)
    {
        this.workingDir = workingDir;
        this.openPages  = new HashMap<String, WeakReference<Object>>();
        this.LOCK       = new Object();
    }
    
    public void savePage(String id, Object page, Serializer<ByteBuffer> serializer)
            throws FledPresistanceException
    {
        try
        {
            synchronized(LOCK)
            {
                if (serializer != null)
                {
                    ByteBuffer buffer = serializer.serialize(page);
                    ByteBufferSerializer.serialize(workingDir + "/" + id, buffer);
                }
                else
                {
                    ByteBufferSerializer.serialize(workingDir + "/" + id, page);
                }
                openPages.put(id, new WeakReference<Object>(page));
            }
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE, ex);
        }
    }
    
    public Object getPage(String id, Serializer<ByteBuffer> serializer)
            throws FledPresistanceException
    {
        try
        {
            Object result = null;
            synchronized(LOCK)
            {
                // first just try to get it from the cache
                WeakReference<Object> reference = openPages.get(id);
                
                if (reference != null)
                {
                    result = reference.get();
                }

                // if the reference has been cleared already then we have to 
                // try to open it... if it exists
                if (result == null)
                {
                    File file = new File(workingDir + "/" + id);
                    if (!file.exists())
                    {
                        return null;
                    }
                    if (serializer != null)
                    {
                        result = serializer.deserialize(
                                 ByteBufferSerializer.deserialize(
                                 file.getAbsolutePath()));
                    }
                    else
                    {
                        result = ByteBufferSerializer.deserializeObject(
                                 file.getAbsolutePath());
                    }
                    openPages.put(id, new WeakReference<Object>(result));
                }
            }
            return result;
        }
        catch(Exception ex)
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE, ex);
        }
    }
    
    public void deletePage(String id)
    {
        synchronized(LOCK)
        {
            if (openPages.containsKey(id))
            {
                openPages.remove(id);
            }
            (new File(workingDir + "/" + id)).delete();
        }
    }
}