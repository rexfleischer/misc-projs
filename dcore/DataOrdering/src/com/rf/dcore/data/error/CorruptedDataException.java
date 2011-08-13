/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.error;

/**
 *
 * @author REx
 */
public class CorruptedDataException extends Exception
{

    public CorruptedDataException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CorruptedDataException(String message)
    {
        super(message);
    }

}
