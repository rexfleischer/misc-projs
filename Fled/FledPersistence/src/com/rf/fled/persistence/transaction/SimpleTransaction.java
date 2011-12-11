/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Serializer;
import com.rf.fled.persistence.localization.LanguageStatements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class SimpleTransaction implements FileManager
{
    private FileManager parent;
    
    private HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>> originIds;

    private HashMap<String, KeyValuePair<Object, Serializer<byte[]>>> originNamed;

    private HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>> toUpdateIds;

    private HashMap<String, KeyValuePair<Object, Serializer<byte[]>>> toUpdateNamed;

    private ArrayList<Long> toDeleteIds;

    private ArrayList<String> toDeleteNamed;

    public SimpleTransaction(FileManager parent) 
    {
        this.parent     = parent;
        originIds       = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        originNamed     = new HashMap<String, KeyValuePair<Object, Serializer<byte[]>>>();
        toUpdateIds     = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        toUpdateNamed   = new HashMap<String, KeyValuePair<Object, Serializer<byte[]>>>();
        toDeleteIds     = new ArrayList<Long>();
        toDeleteNamed   = new ArrayList<String>();
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
        if (!(data instanceof Transactionable))
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString());
        }
        Transactionable origin = (Transactionable) data;
        originIds.put(id, new KeyValuePair<Object, Serializer<byte[]>>(origin, serializer));

        return origin.deepCopy(this);
    }

    @Override
    public Object loadNamedFile(String name, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (toUpdateNamed.containsKey(name))
        {
            return toUpdateNamed.get(name).getKey();
        }
        if (toDeleteNamed.contains(name))
        {
            return null;
        }
        if (originNamed.containsKey(name))
        {
            return originNamed.get(name);
        }
        Object data = parent.loadNamedFile(name, serializer);
        if (!(data instanceof Transactionable))
        {
            // @TODO statement
            throw new FledPersistenceException(
                    LanguageStatements.NONE.toString());
        }
        Transactionable origin = (Transactionable) data;
        originNamed.put(name, new KeyValuePair<Object, Serializer<byte[]>>(origin, serializer));

        return origin.deepCopy(parent);
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
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (toDeleteNamed.contains(name))
        {
            toDeleteNamed.remove(name);
        }
        toUpdateNamed.put(name, new KeyValuePair<Object,Serializer<byte[]>>(data, serializer));
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
    public void deleteNamedFile(String name) 
            throws FledPersistenceException 
    {
        if (toUpdateNamed.containsKey(name))
        {
            toUpdateNamed.remove(name);
        }
        if (!toDeleteNamed.contains(name))
        {
            toDeleteNamed.add(name);
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
                Iterator<String> it = toUpdateNamed.keySet().iterator();
                while(it.hasNext())
                {
                    String id = it.next();
                    named.add(id);
                    KeyValuePair<Object, Serializer<byte[]>> pair = toUpdateNamed.get(id);
                    parent.saveNamedFile(id, pair.getKey(), pair.getValue());
                }
            }

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
                Iterator<String> it = toDeleteNamed.iterator();
                while(it.hasNext())
                {
                    String id = it.next();
                    named.add(id);
                    parent.deleteNamedFile(id);
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
        }
        catch(Exception ex)
        {
            // if an error happens, then rollback everything that
            // has been saved
            {
                Iterator<String> it = named.iterator();
                while(it.hasNext())
                {
                    String name = it.next();
                    KeyValuePair<Object, Serializer<byte[]>> pair = originNamed.get(name);
                    if (pair != null)
                    {
                        try 
                        {
                            parent.saveNamedFile(name, pair.getKey(), pair.getValue());
                        } 
                        catch (FledPersistenceException ex1) { }
                    }
                }
            }

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
                // @TODO statement
                throw new FledTransactionException(
                        LanguageStatements.NONE.toString(), ex);
            }
        }
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        originIds       = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        originNamed     = new HashMap<String, KeyValuePair<Object, Serializer<byte[]>>>();
        toUpdateIds     = new HashMap<Long, KeyValuePair<Object, Serializer<byte[]>>>();
        toUpdateNamed   = new HashMap<String, KeyValuePair<Object, Serializer<byte[]>>>();
        toDeleteIds     = new ArrayList<Long>();
        toDeleteNamed   = new ArrayList<String>();
    }
}
