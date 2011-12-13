/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.Persistence;

/**
 *
 * @author REx
 */
public class TransactionFactory 
{
    public static Persistence create(
            Persistence persistence,
            FileManager fileManager, 
            TransactionType type)
    {
        switch(type)
        {
            case IN_MEMORY:
                return new Transaction_InMemory(fileManager, persistence);
            case NONE:
                return persistence;
            default:
                throw new IllegalArgumentException("type");
        }
    }
}
