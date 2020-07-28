/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

/**
 *
 * @author REx
 */
public class GameException extends Exception
{

    public GameException(Throwable cause)
    {
        super(cause);
    }

    public GameException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public GameException(String message)
    {
        super(message);
    }

    public GameException()
    {
    }
    
}
