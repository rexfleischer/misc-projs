/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection;

/**
 *
 * @author REx
 */
public class ClientConfigurationException extends Exception
{

    public ClientConfigurationException(String message)
    {
        super(message);
    }

    public ClientConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ClientConfigurationException(Throwable cause)
    {
        super(cause);
    }
    
}
