/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.filemanager.IFileManager;

/**
 *
 * @author REx
 */
public interface ITransactionalFileManager extends IFileManager
{
    public void commit() throws FledTransactionException;
    
    public void rollback() throws FledTransactionException;
}
