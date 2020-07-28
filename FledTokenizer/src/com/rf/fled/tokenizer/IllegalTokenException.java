/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

/**
 *
 * @author REx
 */
public class IllegalTokenException extends Exception
{

    public IllegalTokenException(Throwable cause)
    {
        super(cause);
    }

    public IllegalTokenException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public IllegalTokenException(String message)
    {
        super(message);
    }

    public IllegalTokenException()
    {
    }
    
}
