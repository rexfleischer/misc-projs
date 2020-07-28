/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.cluster;

/**
 *
 * @author REx
 */
public class CommandHandlerException extends Exception
{

    public CommandHandlerException(Throwable cause)
    {
        super(cause);
    }

    public CommandHandlerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CommandHandlerException(String message)
    {
        super(message);
    }

    public CommandHandlerException()
    {
    }
    
}
