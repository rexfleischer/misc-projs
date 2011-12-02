/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import com.rf.fled.exceptions.FledTransactionException;
import com.rf.fled.language.LanguageStatements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author REx
 */
public class FileTransaction implements FileManager
{
    private FileManager parent;
    
    private HashMap<String, RecordFile> originFiles;
    
    private HashMap<String, RecordFile> toUpdate;
    
    private HashMap<String, String> toDelete;
    
    public FileTransaction(FileManager parent)
    {
        this.parent     = parent;
        this.toUpdate   = new HashMap<String, RecordFile>();
        this.toDelete   = new HashMap<String, String>();
        this.originFiles= new HashMap<String, RecordFile>();
    }

    @Override
    public FileTransaction beginTransaction() 
            throws IOException 
    {
        throw new UnsupportedOperationException(
                "this is already a transaction");
    }

    @Override
    public String buildFileName(String id) 
            throws IOException 
    {
        throw new UnsupportedOperationException(
                "transactions do not support this");
    }

    @Override
    public void deleteFile(String id) 
            throws IOException 
    {
        String filename = parent.buildFileName(id);
        if (this.toUpdate.containsKey(filename))
        {
            this.toUpdate.remove(filename);
        }
        if (!this.toDelete.containsKey(filename))
        {
            this.toDelete.put(filename, id);
        }
    }

    @Override
    public void saveRecordFile(RecordFile file)
            throws IOException
    {
        this.toUpdate.put(parent.buildFileName(file.getId()), file);
    }

    @Override
    public RecordFile loadRecordFile(String id) 
            throws IOException
    {
        RecordFile origin = parent.loadRecordFile(id);
        RecordFile result = origin.getCopy();
        
        originFiles.put(parent.buildFileName(id), origin);
        
        return result;
    }
    
    public void commit() throws IOException, FledTransactionException
    {
        try
        {
            Iterator<String> it = null;
            
            {
                it = toUpdate.keySet().iterator();
                while(it.hasNext())
                {
                    String id = it.next();
                    parent.saveRecordFile(toUpdate.get(id));
                }
            }
            
            {
                it = toDelete.keySet().iterator();
                while(it.hasNext())
                {
                    parent.deleteFile(toDelete.get(it.next()));
                }
            }
        }
        catch(Exception ex)
        {
            // if an error happens, then rollback everything that
            // has been saved or deleted
            Iterator<String> it = originFiles.keySet().iterator();
            while(it.hasNext())
            {
                String id = it.next();
                parent.saveRecordFile(originFiles.get(id));
            }
            // @TODO statement
            throw new FledTransactionException(LanguageStatements.NONE, ex);
        }
    }
    
    public void rollback()
    {
        this.toUpdate   = new HashMap<String, RecordFile>();
        this.toDelete   = new HashMap<String, String>();
        this.originFiles= new HashMap<String, RecordFile>();
    }
}
