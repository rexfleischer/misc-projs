/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang;

/**
 *
 * @author REx
 */
public class FledSyntaxException extends Exception
{

    public FledSyntaxException (Throwable cause)
    {
        super(cause);
    }

    public FledSyntaxException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public FledSyntaxException (String message)
    {
        super(message);
    }

    public FledSyntaxException ()
    {
    }
    
}
