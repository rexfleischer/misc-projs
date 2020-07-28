/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.threads;

/**
 *
 * @author REx
 */
public class WorkerThreadException extends Exception
{

    public WorkerThreadException (Throwable cause)
    {
        super(cause);
    }

    public WorkerThreadException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public WorkerThreadException (String message)
    {
        super(message);
    }

    public WorkerThreadException ()
    {
    }
    
}
