/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.executor;

/**
 *
 * @author REx
 */
public class ByteExecutorInitException extends Exception
{

    public ByteExecutorInitException (Throwable cause)
    {
        super(cause);
    }

    public ByteExecutorInitException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public ByteExecutorInitException (String message)
    {
        super(message);
    }

    public ByteExecutorInitException ()
    {
    }
    
}
