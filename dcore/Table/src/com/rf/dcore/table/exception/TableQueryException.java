/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.exception;

/**
 *
 * @author REx
 */
public class TableQueryException extends Exception
{

    public TableQueryException(String message)
    {
        super(message);
    }

    public TableQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
