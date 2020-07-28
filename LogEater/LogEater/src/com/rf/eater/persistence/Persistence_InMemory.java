/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.persistence;

import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class Persistence_InMemory implements Persistence
{
    public LinkedList<Map<String, String>> values;

    Persistence_InMemory(Map<String, String> params)
    {
        values = new LinkedList<Map<String, String>>();
    }

    @Override
    public void close() throws PersistenceException
    {
        // nothing to do
    }

    @Override
    public void persist(Map<String, String> values) throws PersistenceException
    {
        if (values == null)
        {
            throw new NullPointerException("values");
        }
        if (values.isEmpty())
        {
            throw new IllegalArgumentException("values");
        }
        this.values.add(values);
    }
    
}
