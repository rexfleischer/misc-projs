/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.simulation;

/**
 *
 * @author REx
 */
public class BlackboxSimulationException extends Exception
{

    public BlackboxSimulationException()
    {
    }

    public BlackboxSimulationException(String message)
    {
        super(message);
    }

    public BlackboxSimulationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BlackboxSimulationException(Throwable cause)
    {
        super(cause);
    }
    
}
