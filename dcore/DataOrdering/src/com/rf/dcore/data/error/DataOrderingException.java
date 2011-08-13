/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.error;

/**
 *
 * @author REx
 */
public class DataOrderingException extends Exception
{

    public DataOrderingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DataOrderingException(String message)
    {
        super(message);
    }

}
