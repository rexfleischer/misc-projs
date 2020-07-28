/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.filemanager.IFileManager;
import java.util.concurrent.locks.Lock;

/**
 * optional extension to Persistence
 * @author REx
 */
public interface ITransactionalRecord 
{
    /**
     * table wide write lock
     * @return 
     */
    public Lock getWriteLock();
    
    /**
     * table wide read lock
     * @return 
     */
    public Lock getReadLock();
    
    /**
     * this is for internal use of initiating transactions
     * @return
     * @throws FledTransactionException 
     */
    public ITransactionalRecord transaction();
    
    /**
     * 
     * @return 
     */
    public IFileManager getFileManager();

    /**
     * 
     * @param fileManager 
     */
    public void setFileManager(IFileManager fileManager);
}
