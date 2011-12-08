/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

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
     * @param serializer
     * @return
     * @throws FledPresistanceException 
     */
    public Object loadNamedFile(String context, Serializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param name
     * @param data
     * @param serializer
     * @throws FledPresistanceException 
     */
    public void saveNamedFile(String context, Object data, Serializer<byte[]> serializer)
            throws FledPersistenceException;
    
    /**
     * 
     * @param name
     * @throws FledPresistanceException 
     */
    public void deleteNamedFile(String context)
            throws FledPersistenceException;
    
    /**
     * 
     * @return 
     */
    long incFileCount();
}
