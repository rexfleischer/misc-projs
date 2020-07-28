/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection;

/**
 *
 * @author REx
 */
public class ClientInitException extends Exception
{

    public ClientInitException(String message)
    {
        super(message);
    }

    public ClientInitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ClientInitException(Throwable cause)
    {
        super(cause);
    }
    
}
