/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.filemanager.IFileManager;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author REx
 */
public class Transaction_SingleWriter implements IPersistence
{
    private IPersistence parent;
    
    private IPersistence transaction;
    
    private Thread transactionOwner;
    
    private final Object LOCK;
    
    private ReentrantLock transactionLock;
    
    private TransactionType type;
    
    private IPersistence getTransactionContext()
    {
        synchronized(LOCK)
        {
            if (transactionOwner == Thread.currentThread())
            {
                return transaction;
            }
            return parent;
        }
    }
    
    public Transaction_SingleWriter(IPersistence parent, TransactionType type)
    {
        if (!(parent instanceof ITransactionalRecord))
        {
            throw new IllegalArgumentException("parent must implement Transactional");
        }
        this.type   = type;
        this.parent = parent;
        transaction = null;
        transactionOwner = null;
        LOCK = new Object();
        transactionLock = new ReentrantLock(true);
    }

    @Override
    public void beginTransaction()
            throws FledTransactionException 
    {
        if (transactionOwner == Thread.currentThread())
        {
            throw new FledTransactionException("transaction already started");
        }
        transactionLock.lock();
        if (transaction != null)
        {
            throw new FledTransactionException("unknown error occurred");
        }
        synchronized(LOCK)
        {
            ITransactionalRecord result = ((ITransactionalRecord) parent).transaction();
            IFileManager manager = null;
            switch(type)
            {
                case SINGLE_WRITER_IN_MEMORY:
                    manager = new TransactionChild_InMemory(
                            ((ITransactionalRecord) parent).getFileManager());
                    break;
                default:
                    throw new IllegalArgumentException(
                            "invalid transaction type");
            }
            result.setFileManager(manager);
            transaction = (IPersistence) result;
            transactionOwner = Thread.currentThread();
        }
    }

    @Override
    public void commit()
            throws FledTransactionException 
    {
        synchronized(LOCK)
        {
            if (transactionOwner == null || 
                transactionOwner != Thread.currentThread())
            {
                throw new FledTransactionException(
                        "have not started a transaction");
            }
            ITransactionalRecord transactionalParent = (ITransactionalRecord) parent;
            ITransactionalRecord transactionInstance = (ITransactionalRecord) transaction;
            try
            {
                transactionalParent.getWriteLock().lock();
                
                ((ITransactionalFileManager) 
                        transactionInstance.getFileManager())
                        .commit();
                
                transactionInstance
                        .setFileManager(((ITransactionalRecord) parent)
                        .getFileManager());
                
                parent = transaction;
            }
            finally
            {    
                transactionalParent.getWriteLock().unlock();
            }
            transaction = null;
            transactionOwner = null;
        }
        transactionLock.unlock();
    }

    @Override
    public void rollback()
            throws FledTransactionException 
    {
        synchronized(LOCK)
        {
            transaction = null;
            transactionOwner = null;
        }
        transactionLock.unlock();
    }

    @Override
    public IBrowser<KeyValuePair<Long, Object>> browse(long id)
            throws FledPersistenceException 
    {
        return getTransactionContext().browse(id);
    }

    @Override
    public IBrowser<KeyValuePair<Long, Object>> browse() 
            throws FledPersistenceException 
    {
        return getTransactionContext().browse();
    }

    @Override
    public Object delete(long id) 
            throws FledPersistenceException 
    {
        return getTransactionContext().delete(id);
    }

    @Override
    public Object insert(long id, Object record, boolean replace) 
            throws FledPersistenceException 
    {
        return getTransactionContext().insert(id, record, replace);
    }

    @Override
    public boolean update(long id, Object record) 
            throws FledPersistenceException 
    {
        return getTransactionContext().update(id, record);
    }

    @Override
    public String getContext() 
            throws FledPersistenceException 
    {
        return getTransactionContext().getContext();
    }

    @Override
    public Object select(long id) 
            throws FledPersistenceException 
    {
        return getTransactionContext().select(id);
    }

    @Override
    public long size() 
            throws FledPersistenceException 
    {
        return getTransactionContext().size();
    }

    @Override
    public void truncate() 
            throws FledPersistenceException 
    {
        getTransactionContext().truncate();
    }

    @Override
    public void drop() 
            throws FledPersistenceException 
    {
        getTransactionContext().drop();
    }
}
