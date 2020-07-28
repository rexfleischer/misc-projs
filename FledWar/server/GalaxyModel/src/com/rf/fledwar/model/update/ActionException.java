/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.update;

/**
 *
 * @author REx
 */
public class ActionException extends Exception
{

    public ActionException()
    {
    }

    public ActionException(String message)
    {
        super(message);
    }

    public ActionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ActionException(Throwable cause)
    {
        super(cause);
    }
    
}
