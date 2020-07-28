/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

/**
 *
 * @author REx
 */
public class MessagingException extends Exception
{

    public MessagingException(Throwable cause)
    {
        super(cause);
    }

    public MessagingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MessagingException(String message)
    {
        super(message);
    }

    public MessagingException()
    {
    }
    
}
