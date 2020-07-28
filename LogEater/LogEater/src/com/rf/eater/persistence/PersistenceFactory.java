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
public enum PersistenceFactory
{
    IN_MEMORY()
    {
        @Override
        public Persistence getNewConnection(
                Map<String, String> params)
                throws PersistenceException
        {
            return new Persistence_InMemory(params);
        }
    },
    MY_SQL()
    {
        @Override
        public Persistence getNewConnection(
                Map<String, String> connectionParams)
                throws PersistenceException
        {
            return null;
        }
    };
    
    public abstract Persistence getNewConnection(
            Map<String, String> connectionParams)
            throws PersistenceException;
}
