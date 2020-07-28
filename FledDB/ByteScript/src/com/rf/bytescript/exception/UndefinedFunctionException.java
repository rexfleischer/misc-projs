/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.exception;

/**
 *
 * @author REx
 */
public class UndefinedFunctionException extends Exception
{

    public UndefinedFunctionException (Throwable cause)
    {
        super(cause);
    }

    public UndefinedFunctionException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public UndefinedFunctionException (String message)
    {
        super(message);
    }

    public UndefinedFunctionException ()
    {
    }
    
}
