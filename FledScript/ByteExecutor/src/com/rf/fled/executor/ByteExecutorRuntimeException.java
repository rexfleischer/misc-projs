/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.executor;

/**
 *
 * @author REx
 */
public class ByteExecutorRuntimeException extends Exception
{

    public ByteExecutorRuntimeException (Throwable cause)
    {
        super(cause);
    }

    public ByteExecutorRuntimeException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public ByteExecutorRuntimeException (String message)
    {
        super(message);
    }

    public ByteExecutorRuntimeException ()
    {
    }
    
}
