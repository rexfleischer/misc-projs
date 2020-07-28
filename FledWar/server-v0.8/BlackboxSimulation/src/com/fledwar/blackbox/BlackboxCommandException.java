/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox;

/**
 *
 * @author REx
 */
public class BlackboxCommandException extends Exception
{

    public BlackboxCommandException()
    {
    }

    public BlackboxCommandException(String message)
    {
        super(message);
    }

    public BlackboxCommandException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BlackboxCommandException(Throwable cause)
    {
        super(cause);
    }
    
}
