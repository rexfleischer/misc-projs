/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.query;

import com.rf.dcore.table.query.logictree.CommandNode;
import com.rf.dcore.operation.ComparatorOperations;
import java.util.LinkedList;

/**
 *
 * @author REx
 */
public class TableQueryBuilder
{
    private LinkedList<CommandNode> commands;

    public TableQueryBuilder()
    {
        commands = new LinkedList<>();
    }

    public TableQuery getTableQuery()
    {
        return new TableQuery(commands);
    }

    public void addCommand(
            String columnName,
            ComparatorOperations operation,
            Object value)
    {
//        commands.add(new QueryCommand(
//                columnName,
//                operation,
//                value));
    }
}
