/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.persistence;

/**
 *
 * @author REx
 */
public class PersistenceException extends Exception
{

    public PersistenceException (Throwable cause)
    {
        super(cause);
    }

    public PersistenceException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public PersistenceException (String message)
    {
        super(message);
    }

    public PersistenceException ()
    {
    }
    
}
