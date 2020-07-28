/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.exception;

/**
 *
 * @author REx
 */
public class CompilationException extends Exception
{

    public CompilationException (Throwable cause)
    {
        super(cause);
    }

    public CompilationException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public CompilationException (String message)
    {
        super(message);
    }

    public CompilationException ()
    {
    }
    
}
