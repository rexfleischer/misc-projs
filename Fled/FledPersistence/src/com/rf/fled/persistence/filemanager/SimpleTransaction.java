/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.interfaces.Serializer;
import com.rf.fled.persistence.Transactionable;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.FledPresistanceException;
import com.rf.fled.util.Pair;
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
    
    private HashMap<Long, Pair<Object, Serializer<byte[]>>> originIds;

    private HashMap<String, Pair<Object, Serializer<byte[]>>> originNamed;

    private HashMap<Long, Pair<Object, Serializer<byte[]>>> toUpdateIds;

    private HashMap<String, Pair<Object, Serializer<byte[]>>> toUpdateNamed;

    private ArrayList<Long> toDeleteIds;

    private ArrayList<String> toDeleteNamed;

    SimpleTransaction(FileManager parent) 
    {
        this.parent     = parent;
        originIds       = new HashMap<Long, Pair<Object, Serializer<byte[]>>>();
        originNamed     = new HashMap<String, Pair<Object, Serializer<byte[]>>>();
        toUpdateIds     = new HashMap<Long, Pair<Object, Serializer<byte[]>>>();
        toUpdateNamed   = new HashMap<String, Pair<Object, Serializer<byte[]>>>();
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
            throws FledPresistanceException 
    {
        if (toUpdateIds.containsKey(id))
        {
            return toUpdateIds.get(id).getLeft();
        }
        if (toDeleteIds.contains(id))
        {
            return null;
        }
        if (originIds.containsKey(id))
        {
            return originIds.get(id);
        }
        Object data = parent.loadFile(id, serializer);
        if (!(data instanceof Transactionable))
        {
            // @TODO statement
            throw new FledPresistanceException(LanguageStatements.NONE);
        }
        Transactionable origin = (Transactionable) data;
        originIds.put(id, new Pair<Object, Serializer<byte[]>>(origin, serializer));

        return origin.deepCopy(this);
    }

    @Override
    public Object loadNamedFile(String name, Serializer<byte[]> serializer) 
            throws FledPresistanceException 
    {
        if (toUpdateNamed.containsKey(name))
        {
            return toUpdateNamed.get(name).getLeft();
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
            throw new FledPresistanceException(LanguageStatements.NONE);
        }
        Transactionable origin = (Transactionable) data;
        originNamed.put(name, new Pair<Object, Serializer<byte[]>>(origin, serializer));

        return origin.deepCopy(parent);
    }

    @Override
    public void updateFile(long id, Object data, Serializer<byte[]> serializer) 
            throws FledPresistanceException 
    {
        if (toDeleteIds.contains(id))
        {
            toDeleteIds.remove(id);
        }
        toUpdateIds.put(id, new Pair<Object,Serializer<byte[]>>(data, serializer));
    }

    @Override
    public long saveFile(Object data, Serializer<byte[]> serializer) 
            throws FledPresistanceException 
    {
        long id = parent.incFileCount();
        toUpdateIds.put(id, new Pair<Object,Serializer<byte[]>>(data, serializer));
        return id;
    }

    @Override
    public void saveNamedFile(String name, Object data, Serializer<byte[]> serializer) 
            throws FledPresistanceException 
    {
        if (toDeleteNamed.contains(name))
        {
            toDeleteNamed.remove(name);
        }
        toUpdateNamed.put(name, new Pair<Object,Serializer<byte[]>>(data, serializer));
    }

    @Override
    public void deleteFile(long id) 
            throws FledPresistanceException 
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
            throws FledPresistanceException 
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
                    Pair<Object, Serializer<byte[]>> pair = toUpdateNamed.get(id);
                    parent.saveNamedFile(id, pair.getLeft(), pair.getRight());
                }
            }

            {
                Iterator<Long> it = toUpdateIds.keySet().iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    ided.add(id);
                    Pair<Object, Serializer<byte[]>> pair = toUpdateIds.get(id);
                    parent.updateFile(id, pair.getLeft(), pair.getRight());
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
                    Pair<Object, Serializer<byte[]>> pair = originNamed.get(name);
                    if (pair != null)
                    {
                        try 
                        {
                            parent.saveNamedFile(name, pair.getLeft(), pair.getRight());
                        } 
                        catch (FledPresistanceException ex1) { }
                    }
                }
            }

            {
                Iterator<Long> it = originIds.keySet().iterator();
                while(it.hasNext())
                {
                    Long id = it.next();
                    Pair<Object, Serializer<byte[]>> pair = originIds.get(id);
                    if (pair != null)
                    {
                        try 
                        {
                            parent.updateFile(id, pair.getLeft(), pair.getRight());
                        } 
                        catch (FledPresistanceException ex1){ }
                    }
                }
                // @TODO statement
                throw new FledTransactionException(LanguageStatements.NONE, ex);
            }
        }
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        originIds       = new HashMap<Long, Pair<Object, Serializer<byte[]>>>();
        originNamed     = new HashMap<String, Pair<Object, Serializer<byte[]>>>();
        toUpdateIds     = new HashMap<Long, Pair<Object, Serializer<byte[]>>>();
        toUpdateNamed   = new HashMap<String, Pair<Object, Serializer<byte[]>>>();
        toDeleteIds     = new ArrayList<Long>();
        toDeleteNamed   = new ArrayList<String>();
    }
}
