/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.IPersistence;

/**
 *
 * @author REx
 */
public class TransactionFactory 
{
    public static IPersistence create(
            IPersistence persistence,
            TransactionType type)
    {
        /**
         * blocking off transactions for now
         */
        switch(type)
        {
            case SINGLE_WRITER_IN_MEMORY:
                return new Transaction_SingleWriter(persistence, type);
            case NONE:
                return persistence;
            default:
                throw new IllegalArgumentException("type");
        }
    }
}
