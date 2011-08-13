/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.data.error;

/**
 *
 * @author REx
 */
public class OrderedFileException extends Exception
{

    public OrderedFileException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public OrderedFileException(String message)
    {
        super(message);
    }

}
