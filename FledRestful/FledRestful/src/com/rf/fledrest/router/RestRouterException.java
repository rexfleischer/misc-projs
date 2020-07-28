/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

/**
 *
 * @author REx
 */
public class RestRouterException extends Exception
{

    public RestRouterException(Throwable cause)
    {
        super(cause);
    }

    public RestRouterException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RestRouterException(String message)
    {
        super(message);
    }

    public RestRouterException()
    {
    }
    
}
