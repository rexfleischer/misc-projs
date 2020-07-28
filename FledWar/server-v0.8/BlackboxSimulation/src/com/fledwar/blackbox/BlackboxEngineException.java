/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox;

/**
 *
 * @author REx
 */
public class BlackboxEngineException extends Exception
{

    public BlackboxEngineException()
    {
    }

    public BlackboxEngineException(String message)
    {
        super(message);
    }

    public BlackboxEngineException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BlackboxEngineException(Throwable cause)
    {
        super(cause);
    }
    
}
