/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 *
 * @author REx
 */
public interface Persistence
{
    /**
     * does an insert at a specific id
     * @param id the id to insert
     * @param record the object that is going to be inserted
     * @return the old object, if any
     * @throws FledPresistanceException 
     */
    public Object insert(long id, Object record, boolean replace)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @return
     * @throws FledPresistanceException 
     */
    public Object delete(long id)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @return
     * @throws FledPresistanceException 
     */
    public Object select(long id)
            throws FledPersistenceException;
    
    /**
     * 
     * @param id
     * @return
     * @throws FledPresistanceException 
     */
    public Browser<KeyValuePair<Long, Object>> browse(long id)
            throws FledPersistenceException;
    
    /**
     * 
     * @return
     * @throws FledPresistanceException 
     */
    public Browser<KeyValuePair<Long, Object>> browse()
            throws FledPersistenceException;
    
    /**
     * 
     * @return
     * @throws FledPresistanceException 
     */
    public long size()
            throws FledPersistenceException;
    
    /**
     * 
     * @return
     * @throws FledPresistanceException 
     */
    public String getContext()
            throws FledPersistenceException;
    
    /**
     * 
     * @throws FledTransactionException 
     */
    public void beginTransaction()
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
     * truncate deletes the data but not the actual persistence manager
     * @throws FledTransactionException 
     */
    public void truncate()
            throws FledTransactionException;
}
