/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

/**
 *
 * @author REx
 */
public class ConversationException extends Exception
{

    public ConversationException(String message)
    {
        super(message);
    }

    public ConversationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ConversationException(Throwable cause)
    {
        super(cause);
    }
    
}
