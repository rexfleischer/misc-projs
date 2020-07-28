/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.exception;

/**
 *
 * @author REx
 */
public class ByteScriptRuntimeException extends Exception
{

    public ByteScriptRuntimeException (Throwable cause)
    {
        super(cause);
    }

    public ByteScriptRuntimeException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public ByteScriptRuntimeException (String message)
    {
        super(message);
    }

    public ByteScriptRuntimeException ()
    {
    }
    
}
