/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.exception;

/**
 *
 * @author REx
 */
public class ByteScriptSyntaxException extends Exception
{

    public ByteScriptSyntaxException (Throwable cause)
    {
        super(cause);
    }

    public ByteScriptSyntaxException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public ByteScriptSyntaxException (String message)
    {
        super(message);
    }

    public ByteScriptSyntaxException ()
    {
    }
    
}
