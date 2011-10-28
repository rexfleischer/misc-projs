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
    public long insert(Object record)
            throws FledPresistanceException;
    
    public void update(long id, Object record)
            throws FledPresistanceException;
    
    public void delete(long id)
            throws FledPresistanceException;
    
    public Object select(long id)
            throws FledPresistanceException;
    
    public Browser<Pair<Long, Object>> browse(long id)
            throws FledPresistanceException;
    
    public void beginTransaction()
            throws FledPresistanceException;
    
    public void commit()
            throws FledPresistanceException;
    
    public void rollback()
            throws FledPresistanceException;
}
