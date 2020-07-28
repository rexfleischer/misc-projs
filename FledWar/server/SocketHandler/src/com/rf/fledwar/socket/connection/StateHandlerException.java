/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

/**
 *
 * @author REx
 */
public class StateHandlerException extends Exception
{

    public StateHandlerException(String message)
    {
        super(message);
    }

    public StateHandlerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public StateHandlerException(Throwable cause)
    {
        super(cause);
    }
    
}
