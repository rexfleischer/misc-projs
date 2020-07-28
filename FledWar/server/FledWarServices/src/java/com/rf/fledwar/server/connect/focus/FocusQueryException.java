/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

/**
 *
 * @author REx
 */
public class FocusQueryException extends Exception
{

    public FocusQueryException()
    {
    }

    public FocusQueryException(String message)
    {
        super(message);
    }

    public FocusQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FocusQueryException(Throwable cause)
    {
        super(cause);
    }
    
}
