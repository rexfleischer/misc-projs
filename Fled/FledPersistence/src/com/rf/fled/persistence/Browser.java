/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 *
 * @author REx
 */
public interface Browser<_Ty> 
{
    public boolean valid()
            throws FledPersistenceException;
    
    public boolean curr(_Ty obj)
            throws FledPersistenceException;
    
    public boolean prev(_Ty obj)
            throws FledPersistenceException;
    
    public boolean next(_Ty obj)
            throws FledPersistenceException;
}
