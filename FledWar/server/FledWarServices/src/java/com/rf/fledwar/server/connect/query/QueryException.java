/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

/**
 *
 * @author REx
 */
public class QueryException extends Exception
{

    public QueryException()
    {
    }

    public QueryException(String message)
    {
        super(message);
    }

    public QueryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public QueryException(Throwable cause)
    {
        super(cause);
    }
    
}
