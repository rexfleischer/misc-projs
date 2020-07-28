/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.persistence;

import java.util.Map;

/**
 *
 * @author REx
 */
public interface Persistence
{
    public void persist(Map<String, String> values)
            throws PersistenceException;
    
    public void close()
            throws PersistenceException;
}
