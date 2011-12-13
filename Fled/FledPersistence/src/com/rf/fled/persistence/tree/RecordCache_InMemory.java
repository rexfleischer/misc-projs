/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.util.SoftHashMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author REx
 */
public class RecordCache_InMemory implements Persistence, Externalizable
{
    private Persistence parent;
    
    private SoftHashMap cache = new SoftHashMap();;
    
    private final Object LOCK = new Object();
    
    public RecordCache_InMemory(Persistence parent)
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
    public Browser<KeyValuePair<Long, Object>> browse(long id) 
            throws FledPersistenceException 
    {
        return parent.browse(id);
    }

    @Override
    public Browser<KeyValuePair<Long, Object>> browse()
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
            throws FledTransactionException 
    {
        parent.truncate();
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws IOException, ClassNotFoundException 
    {
        parent = (Persistence) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) 
            throws IOException 
    {
        out.writeObject(parent);
    }
}
