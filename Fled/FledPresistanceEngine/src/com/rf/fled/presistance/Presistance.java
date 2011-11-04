/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance;

import com.rf.fled.exceptions.FledPresistanceException;
import com.rf.fled.util.Pair;

/**
 *
 * @author REx
 */
public interface Presistance
{
    /**
     * does an insert at a specific id
     * @param id the id to insert
     * @param record the object that is going to be inserted
     * @return the old object, if any
     * @throws FledPresistanceException 
     */
    public Object insert(long id, Object record)
            throws FledPresistanceException;
    
    public void delete(long id)
            throws FledPresistanceException;
    
    public Object select(long id)
            throws FledPresistanceException;
    
    public Browser<Pair<Long, Object>> browse(long id)
            throws FledPresistanceException;
    
    public Browser<Pair<Long, Object>> browse()
            throws FledPresistanceException;
    
    public void beginTransaction()
            throws FledPresistanceException;
    
    public void commit()
            throws FledPresistanceException;
    
    public void rollback()
            throws FledPresistanceException;
}
