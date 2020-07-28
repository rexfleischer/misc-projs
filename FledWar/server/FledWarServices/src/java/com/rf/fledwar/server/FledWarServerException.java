/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server;

/**
 *
 * @author REx
 */
public class FledWarServerException extends Exception
{

    public FledWarServerException(String message)
    {
        super(message);
    }

    public FledWarServerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FledWarServerException(Throwable cause)
    {
        super(cause);
    }
    
}
