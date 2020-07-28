/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.filemanager.IFileManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public final class TransactionChild_InMemory implements ITransactionalFileManager
{
    private IFileManager parent;

    private HashMap<Long, KeyValuePair<Object, ISerializer<byte[]>>> idOrigins;

    private HashMap<Long, KeyValuePair<Object, ISerializer<byte[]>>> idUpdates;

    private ArrayList<Long> idDeletes;

    private HashMap<String, KeyValuePair<Object, ISerializer<byte[]>>> namedOrigins;

    private HashMap<String, KeyValuePair<Object, ISerializer<byte[]>>> namedUpdates;

    private ArrayList<String> namedDeletes;

    public TransactionChild_InMemory(IFileManager parent) 
    {
        this.parent = parent;
        try 
        {
            rollback();
        } 
        catch (FledTransactionException ex) 
        {
            throw new IllegalArgumentException(ex);
        }
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
    public long incFileCount() 
    {
        return parent.incFileCount();
    }

    @Override
    public Object loadFile(long id, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (idUpdates.containsKey(id))
        {
            return idUpdates.get(id).getKey();
        }
        if (idDeletes.contains(id))
        {
            return null;
        }
        if (idOrigins.containsKey(id))
        {
            return idOrigins.get(id).getKey();
        }
        Object data = parent.loadFile(id, serializer);
        if (!(data instanceof ITransactionalRecord))
        {
            throw new FledPersistenceException(
                    "objects must be instances of ITransactionalRecord to "
                    + "put into a transaction (instanceof: " + data + ")");
        }
        ITransactionalRecord origin = (ITransactionalRecord) data;
        idOrigins.put(id, new KeyValuePair<Object, ISerializer<byte[]>>(origin, serializer));
        ITransactionalRecord copy = origin.transaction();
        return copy;
    }

    @Override
    public void updateFile(long id, Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (idDeletes.contains(id))
        {
            idDeletes.remove(id);
        }
        idUpdates.put(id, new KeyValuePair<Object,ISerializer<byte[]>>(data, serializer));
    }

    @Override
    public long saveFile(Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        long id = parent.incFileCount();
        idUpdates.put(id, new KeyValuePair<Object,ISerializer<byte[]>>(data, serializer));
        return id;
    }

    @Override
    public void deleteFile(long id) 
            throws FledPersistenceException 
    {
        if (idUpdates.containsKey(id))
        {
            idUpdates.remove(id);
        }
        if (!idDeletes.contains(id))
        {
            idDeletes.add(id);
        }
    }

    @Override
    public void saveNamedFile(String name, Object data, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (namedDeletes.contains(name))
        {
            namedDeletes.remove(name);
        }
        namedUpdates.put(name, new KeyValuePair<Object,ISerializer<byte[]>>(data, serializer));
    }

    @Override
    public Object loadNamedFile(String name, ISerializer<byte[]> serializer) 
            throws FledPersistenceException 
    {
        if (namedUpdates.containsKey(name))
        {
            return namedUpdates.get(name).getKey();
        }
        if (namedDeletes.contains(name))
        {
            return null;
        }
        if (namedOrigins.containsKey(name))
        {
            return namedOrigins.get(name).getKey();
        }
        Object data = parent.loadNamedFile(name, serializer);
        if (!(data instanceof ITransactionalRecord))
        {
            throw new FledPersistenceException(
                    "objects must be instances of ITransactionalRecord to "
                    + "put into a transaction (instanceof: " + data + ")");
        }
        ITransactionalRecord origin = (ITransactionalRecord) data;
        namedOrigins.put(name, new KeyValuePair<Object, ISerializer<byte[]>>(origin, serializer));
        ITransactionalRecord copy = origin.transaction();
        return copy;
    }

    @Override
    public void deleteNamedFile(String name)
            throws FledPersistenceException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        /**
         * i'm taking a risk here that all the files that are deleted are
         * files that were first loaded. 
         */
        ArrayList<Long> ided = new ArrayList<Long>();
        ArrayList<String> strings = new ArrayList<String>();
        try
        {
            if (!idUpdates.isEmpty())
            {
                Iterator<Long> it = idUpdates.keySet().iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    ided.add(id);
                    KeyValuePair<Object, ISerializer<byte[]>> pair = idUpdates.get(id);
                    parent.updateFile(id, pair.getKey(), pair.getValue());
                }
            }

            if (!idDeletes.isEmpty())
            {
                Iterator<Long> it = idDeletes.iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    ided.add(id);
                    parent.deleteFile(id);
                }
            }

            if (!namedUpdates.isEmpty())
            {
                Iterator<String> it = namedUpdates.keySet().iterator();
                while(it.hasNext())
                {
                    String name = it.next();
                    strings.add(name);
                    KeyValuePair<Object, ISerializer<byte[]>> pair = namedUpdates.get(name);
                    parent.saveNamedFile(name, pair.getKey(), pair.getValue());
                }
            }
            
            if (!namedDeletes.isEmpty())
            {
                Iterator<String> it = namedDeletes.iterator();
                while(it.hasNext())
                {
                    String name = it.next();
                    strings.add(name);
                    parent.deleteNamedFile(name);
                }
            }
        }
        catch(Exception ex)
        {
            // if an error happens, then rollback everything that
            // has been saved
            if (!ided.isEmpty())
            {
                Iterator<Long> it = ided.iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    KeyValuePair<Object, ISerializer<byte[]>> pair = idOrigins.get(id);
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
            
            if (!strings.isEmpty())
            {
                Iterator<String> it = strings.iterator();
                while(it.hasNext())
                {
                    String id = it.next();
                    KeyValuePair<Object, ISerializer<byte[]>> pair = namedOrigins.get(id);
                    if (pair != null)
                    {
                        try 
                        {
                            parent.saveNamedFile(id, pair.getKey(), pair.getValue());
                        } 
                        catch (FledPersistenceException ex1){ }
                    }
                }
            }

            throw new FledTransactionException(
                    "an error occurred during a transaction", ex);
        }
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        idOrigins       = new HashMap<Long, KeyValuePair<Object, ISerializer<byte[]>>>();
        idUpdates       = new HashMap<Long, KeyValuePair<Object, ISerializer<byte[]>>>();
        idDeletes       = new ArrayList<Long>();
        namedOrigins    = new HashMap<String, KeyValuePair<Object, ISerializer<byte[]>>>();
        namedUpdates    = new HashMap<String, KeyValuePair<Object, ISerializer<byte[]>>>();
        namedDeletes    = new ArrayList<String>();
    }
}
