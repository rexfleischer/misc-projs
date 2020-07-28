/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto;

/**
 *
 * @author REx
 */
public class BasicVTOException extends RuntimeException
{

    public BasicVTOException()
    {
    }

    public BasicVTOException(String message)
    {
        super(message);
    }

    public BasicVTOException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BasicVTOException(Throwable cause)
    {
        super(cause);
    }
    
}
