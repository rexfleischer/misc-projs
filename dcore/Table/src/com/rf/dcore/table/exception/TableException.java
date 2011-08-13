/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.exception;

/**
 *
 * @author REx
 */
public class TableException extends Exception
{

    public TableException(String message)
    {
        super(message);
    }

    public TableException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
