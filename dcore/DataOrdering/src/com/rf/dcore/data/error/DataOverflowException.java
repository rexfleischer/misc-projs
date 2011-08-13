/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.error;

/**
 *
 * @author REx
 */
public class DataOverflowException extends Exception
{

    public DataOverflowException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DataOverflowException(String message)
    {
        super(message);
    }

}
