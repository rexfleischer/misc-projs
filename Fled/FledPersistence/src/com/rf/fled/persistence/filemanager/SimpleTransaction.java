/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.localization.LanguageStatements;
import com.rf.fled.persistence.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author REx
 */
public class SimpleTransaction implements FileManager
{
    private FileManager parent;
    
    private Object treeParent;
    
    private Serializer<byte[]> treeParentSerializer;
    
    private Object treeOriginParent;
    
    private HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>> originIds;

    private HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>> toUpdateIds;

    private ArrayList<Long> toDeleteIds;

    public SimpleTransaction(FileManager parent) 
    {
        this.parent     = parent;
        originIds       = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        toUpdateIds     = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        toDeleteIds     = new ArrayList<Long>();
    }

    @Override
    public long getFileCount() 
    {
        return parent.getFileCount();
    }

    @Override
    public String getDirectory() 
    {
        return parent.getDirectory();
    }

    @Override
    public FileManager beginTransaction() 
            throws FledTransactionException
    {
        throw new UnsupportedOperationException("this is already a transaction");
    }

    @Override
    public long incFileCount() 
    {
        return parent.incFileCount();
    }

    @Override
    public Object loadFile(long id, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (toUpdateIds.containsKey(id))
        {
            return toUpdateIds.get(id).getKey();
        }
        if (toDeleteIds.contains(id))
        {
            return null;
        }
        if (originIds.containsKey(id))
        {
            return originIds.get(id).getKey();
        }
        Object data = parent.loadFile(id, serializer);
        if (!(data instanceof Transactional))
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString());
        }
        Transactional origin = (Transactional) data;
        originIds.put(id, new KeyValuePair<Object, Serializer<byte[]>>(origin, serializer));

        return origin.deepCopy(this);
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (toDeleteIds.contains(id))
        {
            toDeleteIds.remove(id);
        }
        toUpdateIds.put(id, new KeyValuePair<Object,Serializer<byte[]>>(data, serializer));
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = parent.incFileCount();
        toUpdateIds.put(id, new KeyValuePair<Object,Serializer<byte[]>>(data, serializer));
        return id;
    }

    @Override
    public void updateParentFile(Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        treeParent = data;
        treeParentSerializer = serializer;
    }

    @Override
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        if (toUpdateIds.containsKey(id))
        {
            toUpdateIds.remove(id);
        }
        if (!toDeleteIds.contains(id))
        {
            toDeleteIds.add(id);
        }
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        /**
         * i'm taking a risk here that all the files that are deleted are
         * files that were first loaded. 
         */
        ArrayList<String> named = new ArrayList<String>();
        ArrayList<Long> ided = new ArrayList<Long>();
        try
        {
            {
                Iterator<Long> it = toUpdateIds.keySet().iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    ided.add(id);
                    KeyValuePair<Object, Serializer<byte[]>> pair = toUpdateIds.get(id);
                    parent.updateFile(id, pair.getKey(), pair.getValue());
                }
            }

            {
                Iterator<Long> it = toDeleteIds.iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    ided.add(id);
                    parent.deleteFile(id);
                }
            }
            
            if (treeParent != null)
            {
                parent.updateParentFile(treeParent, treeParentSerializer);
            }
        }
        catch(Exception ex)
        {
            // if an error happens, then rollback everything that
            // has been saved
            {
                Iterator<Long> it = originIds.keySet().iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    KeyValuePair<Object, Serializer<byte[]>> pair = originIds.get(id);
                    if (pair != null)
                    {
                        try 
                        {
                            parent.updateFile(id, pair.getKey(), pair.getValue());
                        } 
                        catch (FledPersistenceException ex1){ }
                    }
                }
            }
            
            if (treeParent != null)
            {
                try 
                {
                    parent.updateParentFile(treeParent, treeParentSerializer);
                } 
                catch (FledPersistenceException ex1) 
                {
                    throw new FledTransactionException(
                        LanguageStatements.NONE.toString(), ex1);
                }
            }
            
            // @TODO statement
            throw new FledTransactionException(
                    LanguageStatements.NONE.toString(), ex);
        }
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        originIds       = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        toUpdateIds     = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        toDeleteIds     = new ArrayList<Long>();
    }
}
