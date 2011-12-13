/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.filemanager;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.FledTransactionException;
import com.rf.fled.persistence.Serializer;

/**
 *
 * @author REx
 */
public interface FileManager 
{
    public static final String EXTENSION = "db";
    
    /**
     * 
     * @return
     * @throws FledTransactionException 
     */
    public FileManager beginTransaction()
            throws FledTransactionException;
    
    /**
     * 
     * @throws FledTransactionException 
     */
    public void commit()
            throws FledTransactionException;
    
    /**
     * 
     * @throws FledTransactionException 
     */
    public void rollback()
            throws FledTransactionException;
    
    /**
     * 
     * @param id
     * @param serializer
     * @return
     * @throws FledPresistanceException 
     */
    public Object loadFile(long id, Serializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @param data
     * @param serializer
     * @throws FledPresistanceException 
     */
    public void updateFile(long id, Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param data
     * @param serializer
     * @return
     * @throws FledPresistanceException 
     */
    public long saveFile(Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @throws FledPresistanceException 
     */
    public void deleteFile(long id)
            throws FledPersistenceException;
    
    /**
     * 
     * @param name
     * @param data
     * @param serializer
     * @throws FledPresistanceException 
     */
    public void updateParentFile(Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @return 
     */
    long incFileCount();
    
    long getFileCount();
    
    String getDirectory();
}
