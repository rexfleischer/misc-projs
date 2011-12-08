/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

/**
 *
 * @author REx
 */
public class FledTransactionException extends Exception
{

    public FledTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FledTransactionException(String message) {
        super(message);
    }

    public FledTransactionException() {
    }
    
}
