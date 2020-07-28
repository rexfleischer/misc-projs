/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

/**
 *
 * @author REx
 */
public class BasicDAOException extends Exception
{

    public BasicDAOException(Throwable cause)
    {
        super(cause);
    }

    public BasicDAOException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BasicDAOException(String message)
    {
        super(message);
    }

    public BasicDAOException()
    {
    }
    
}
