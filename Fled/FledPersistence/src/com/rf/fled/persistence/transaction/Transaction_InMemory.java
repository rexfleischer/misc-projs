/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.localization.LanguageStatements;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

/**
 *
 * @author REx
 */
public class Transaction_InMemory implements Persistence, Externalizable
{
    private KeyValuePair<Persistence, FileManager> parent;
    
    private HashMap<Thread, KeyValuePair<Persistence, FileManager>> transactions;
    
    private KeyValuePair<Persistence, FileManager> getTransaction()
    {
        KeyValuePair<Persistence, FileManager> result = 
                transactions.get(Thread.currentThread());
        if (result == null)
        {
            result = parent;
        }
        return result;
    }
    
    public Transaction_InMemory(FileManager manager, Persistence parent)
    {
        this.parent = new KeyValuePair<Persistence, FileManager>(parent, manager);
        transactions = new HashMap<Thread, KeyValuePair<Persistence, FileManager>>();
    }

    @Override
    public void beginTransaction()
            throws FledTransactionException 
    {
        if (transactions.containsKey(Thread.currentThread()))
        {
            // @TODO statement
            throw new FledTransactionException(
                    LanguageStatements.NONE.toString());
        }
        FileManager transaction = parent.getValue().beginTransaction();
        Persistence presistance = (Persistence) 
                ((Transactional) parent.getKey()).deepCopy(transaction);

        transactions.put(Thread.currentThread(), 
                new KeyValuePair<Persistence, FileManager>(presistance, transaction));
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        KeyValuePair<Persistence, FileManager> committing = 
                transactions.remove(Thread.currentThread());
        committing.getValue().commit();
        parent.setKey((Persistence)
                ((Transactional) committing.getKey())
                .deepCopy(parent.getValue()));
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        transactions.remove(Thread.currentThread()).getValue().rollback();
    }

    @Override
    public Browser<KeyValuePair<Long, Object>> browse(long id)
            throws FledPersistenceException 
    {
        return getTransaction().getKey().browse(id);
    }

    @Override
    public Browser<KeyValuePair<Long, Object>> browse() 
            throws FledPersistenceException 
    {
        return getTransaction().getKey().browse();
    }

    @Override
    public Object delete(long id) 
            throws FledPersistenceException 
    {
        return getTransaction().getKey().delete(id);
    }

    @Override
    public Object insert(long id, Object record, boolean replace) 
            throws FledPersistenceException 
    {
        return getTransaction().getKey().insert(id, record, replace);
    }

    @Override
    public String getContext() 
            throws FledPersistenceException 
    {
        return getTransaction().getKey().getContext();
    }

    @Override
    public Object select(long id) 
            throws FledPersistenceException 
    {
        return getTransaction().getKey().select(id);
    }

    @Override
    public long size() 
            throws FledPersistenceException 
    {
        return getTransaction().getKey().size();
    }

    @Override
    public void truncate() 
            throws FledTransactionException 
    {
        getTransaction().getKey().truncate();
    }

    @Override
    public void writeExternal(ObjectOutput out)
            throws IOException 
    {
        out.writeObject(parent.getKey());
    }

    @Override
    public void readExternal(ObjectInput in) 
            throws IOException, ClassNotFoundException
    {
        Persistence persistence = (Persistence) in.readObject();
        parent = new KeyValuePair<Persistence, FileManager>(persistence, 
                ((Transactional) persistence).getFileManager());
        transactions = new HashMap<Thread, KeyValuePair<Persistence, FileManager>>();
    }
}
