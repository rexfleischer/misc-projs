/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket;

/**
 *
 * @author REx
 */
public class SocketHandlerException extends Exception
{

    public SocketHandlerException()
    {
    }

    public SocketHandlerException(String message)
    {
        super(message);
    }

    public SocketHandlerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SocketHandlerException(Throwable cause)
    {
        super(cause);
    }
    
}
