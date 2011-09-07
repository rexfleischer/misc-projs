/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.query;

import com.rf.dcore.table.query.logictree.CommandNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author REx
 */
public class TableQuery
{
    private final ArrayList<CommandNode> commands;

    public TableQuery(final List<CommandNode> commands)
    {
        List<CommandNode> sorted = new ArrayList<>(commands);
        //Collections.sort(sorted);
        this.commands = new ArrayList<>(sorted);
    }

    public final Iterator<CommandNode> commandsIterator()
    {
        return commands.iterator();
    }

    public final CommandNode[] commandsArray()
    {
        return (CommandNode[]) commands.toArray();
    }
}
