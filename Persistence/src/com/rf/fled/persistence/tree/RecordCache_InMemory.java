/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.util.SoftHashMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author REx
 */
public class RecordCache_InMemory implements IPersistence, Externalizable
{
    private IPersistence parent;
    
    private SoftHashMap cache = new SoftHashMap();
    
    private final Object LOCK = new Object();
    
    public RecordCache_InMemory(IPersistence parent)
    {
        this.parent = parent;
    }
    
    protected RecordCache_InMemory()
    {
        
    }

    @Override
    public void beginTransaction() 
            throws FledTransactionException 
    {
        parent.beginTransaction();
    }

    @Override
    public IBrowser<KeyValuePair<Long, Object>> browse(long id) 
            throws FledPersistenceException 
    {
        return parent.browse(id);
    }

    @Override
    public IBrowser<KeyValuePair<Long, Object>> browse()
            throws FledPersistenceException 
    {
        return parent.browse();
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        parent.commit();
    }

    @Override
    public boolean update(long id, Object record) 
            throws FledPersistenceException 
    {
        boolean result = parent.update(id, record);
        if (result)
        {
            synchronized(LOCK)
            {
                cache.put(id, record);
            }
        }
        return result;
    }

    @Override
    public Object delete(long id) 
            throws FledPersistenceException 
    {
        Object result = parent.delete(id);
        synchronized(LOCK)
        {
            cache.remove(id);
        }
        return result;
    }

    @Override
    public String getContext() 
            throws FledPersistenceException 
    {
        return parent.getContext();
    }

    @Override
    public Object insert(long id, Object record, boolean replace) 
            throws FledPersistenceException 
    {
        Object result = parent.insert(id, record, replace);
        synchronized(LOCK)
        {
            cache.put(id, result);
        }
        return result;
    }

    @Override
    public void rollback() 
            throws FledTransactionException 
    {
        parent.rollback();
    }

    @Override
    public Object select(long id) 
            throws FledPersistenceException 
    {
        Object result = null;
        synchronized(LOCK)
        {
            result = cache.get(id);
        }
        if (result == null)
        {
            result = parent.select(id);
            synchronized(LOCK)
            {
                cache.put(id, result);
            }
        }
        return result;
    }

    @Override
    public long size() 
            throws FledPersistenceException
    {
        return parent.size();
    }

    @Override
    public void truncate() 
            throws FledPersistenceException 
    {
        parent.truncate();
        synchronized(LOCK)
        {
            cache.clear();
        }
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws IOException, ClassNotFoundException 
    {
        parent = (IPersistence) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) 
            throws IOException 
    {
        out.writeObject(parent);
    }

    @Override
    public void drop() 
            throws FledPersistenceException 
    {
        parent.drop();
        synchronized(LOCK)
        {
            cache.clear();
        }
    }
}
