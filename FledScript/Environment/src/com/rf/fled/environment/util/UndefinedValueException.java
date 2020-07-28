/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.util;

/**
 *
 * @author REx
 */
public class UndefinedValueException extends Exception
{

    public UndefinedValueException (Throwable cause)
    {
        super(cause);
    }

    public UndefinedValueException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public UndefinedValueException (String message)
    {
        super(message);
    }

    public UndefinedValueException ()
    {
    }
    
}
