/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

/**
 *
 * @author REx
 */
public enum TransactionType
{
    /**
     * no transaction
     */
    NONE,
    
    /**
     * this does the transactions in memory
     */
    SINGLE_WRITER_IN_MEMORY,
}
