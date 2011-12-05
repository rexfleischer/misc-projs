/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledPresistanceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.language.LanguageStatements;
import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.Transactionable;
import com.rf.fled.util.Pair;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class TransactionPersistence implements Persistence
{
    private Pair<Persistence, FileManager> parent;
    
    private Map<Thread, Pair<Persistence, FileManager>> transactions;
    
    private Pair<Persistence, FileManager> getTransaction()
    {
        Pair<Persistence, FileManager> result = 
                transactions.get(Thread.currentThread());
        if (result == null)
        {
            result = parent;
        }
        return result;
    }
    
    public TransactionPersistence(FileManager manager, Persistence parent)
    {
        this.parent = new Pair<Persistence, FileManager>(parent, manager);
        transactions = new HashMap<Thread, Pair<Persistence, FileManager>>();
    }

    @Override
    public void beginTransaction()
            throws FledTransactionException 
    {
        if (transactions.containsKey(Thread.currentThread()))
        {
            // @TODO statement
            throw new FledTransactionException(LanguageStatements.NONE);
        }
        FileManager transaction = parent.getRight().beginTransaction();
        Persistence presistance = (Persistence) 
                ((Transactionable) parent.getLeft()).deepCopy(transaction);

        transactions.put(Thread.currentThread(), 
                new Pair<Persistence, FileManager>(presistance, transaction));
    }

    @Override
    public void commit() 
            throws FledTransactionException 
    {
        Pair<Persistence, FileManager> committing = 
                transactions.remove(Thread.currentThread());
        committing.getRight().commit();
        parent = committing;
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        transactions.remove(Thread.currentThread()).getRight().rollback();
    }

    @Override
    public Browser<Pair<Long, Object>> browse(long id)
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().browse(id);
    }

    @Override
    public Browser<Pair<Long, Object>> browse() 
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().browse();
    }

    @Override
    public Object delete(long id) 
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().delete(id);
    }

    @Override
    public Object insert(long id, Object record, boolean replace) 
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().insert(id, record, replace);
    }

    @Override
    public String getContext() 
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().getContext();
    }

    @Override
    public Object select(long id) 
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().select(id);
    }

    @Override
    public long size() 
            throws FledPresistanceException 
    {
        return getTransaction().getLeft().size();
    }

    @Override
    public void truncate() 
            throws FledTransactionException 
    {
        throw new UnsupportedOperationException("not supported with transactions");
    }
}
