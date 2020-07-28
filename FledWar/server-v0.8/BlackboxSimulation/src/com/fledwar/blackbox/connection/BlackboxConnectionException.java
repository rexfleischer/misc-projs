/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.connection;

/**
 *
 * @author REx
 */
public class BlackboxConnectionException extends Exception
{

    public BlackboxConnectionException()
    {
    }

    public BlackboxConnectionException(String message)
    {
        super(message);
    }

    public BlackboxConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BlackboxConnectionException(Throwable cause)
    {
        super(cause);
    }
    
}
