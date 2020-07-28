/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.thread;

/**
 *
 * @author REx
 */
public class ThreadManagerException extends Exception
{

    public ThreadManagerException(String message)
    {
        super(message);
    }

    public ThreadManagerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ThreadManagerException(Throwable cause)
    {
        super(cause);
    }
    
}
