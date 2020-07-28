/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang.util;

/**
 *
 * @author REx
 */
public class UndefinedVariableException extends Exception
{

    public UndefinedVariableException (Throwable cause)
    {
        super(cause);
    }

    public UndefinedVariableException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public UndefinedVariableException (String message)
    {
        super(message);
    }

    public UndefinedVariableException ()
    {
    }
    
}
