/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.query.commands;

import com.rf.dcore.operation.ComparatorOperations;

/**
 *
 * @author REx
 */
public class ComparatorCommand
{
    public final String columnName;

    public final ComparatorOperations operation;

    public final Object value;

    public ComparatorCommand(
            String columnName,
            ComparatorOperations operation,
            Object value)
    {
        if (columnName == null)
        {
            throw new NullPointerException("columnName");
        }
        if (operation == null)
        {
            throw new NullPointerException("operation");
        }
        if (value == null)
        {
            throw new NullPointerException("value");
        }
        this.columnName = columnName;
        this.operation  = operation;
        this.value      = value;
    }
}
