/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang;

/**
 *
 * @author REx
 */
public class FledExecutionException extends Exception
{

    public FledExecutionException (Throwable cause)
    {
        super(cause);
    }

    public FledExecutionException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public FledExecutionException (String message)
    {
        super(message);
    }

    public FledExecutionException ()
    {
    }
    
}
