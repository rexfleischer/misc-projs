/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.exception;

/**
 *
 * @author REx
 */
public class ByteScriptInitException extends Exception
{

    public ByteScriptInitException (Throwable cause)
    {
        super(cause);
    }

    public ByteScriptInitException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public ByteScriptInitException (String message)
    {
        super(message);
    }

    public ByteScriptInitException ()
    {
    }
    
}
