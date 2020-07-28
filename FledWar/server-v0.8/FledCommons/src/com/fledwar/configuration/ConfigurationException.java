/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.configuration;

/**
 *
 * @author REx
 */
public class ConfigurationException extends RuntimeException
{

    public ConfigurationException()
    {
    }

    public ConfigurationException(String message)
    {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause)
    {
        super(cause);
    }
    
}
