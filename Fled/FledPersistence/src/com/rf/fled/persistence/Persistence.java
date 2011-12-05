/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import com.rf.fled.util.Pair;

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
            throws FledPresistanceException;
    
    public Object delete(long id)
            throws FledPresistanceException;
    
    public Object select(long id)
            throws FledPresistanceException;
    
    public Browser<Pair<Long, Object>> browse(long id)
            throws FledPresistanceException;
    
    public Browser<Pair<Long, Object>> browse()
            throws FledPresistanceException;
    
    public long size()
            throws FledPresistanceException;
    
    public String getContext()
            throws FledPresistanceException;
    
    public void beginTransaction()
            throws FledTransactionException;
    
    public void commit()
            throws FledTransactionException;
    
    public void rollback()
            throws FledTransactionException;
    
    /**
     * truncate deletes the data but not the actual persistence manager
     * @throws FledTransactionException 
     */
    public void truncate()
            throws FledTransactionException;
}
