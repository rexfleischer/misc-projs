/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.query.logictree;

import com.rf.dcore.table.query.commands.ComparatorCommand;
import com.rf.dcore.table.query.commands.KeySetCommand;

/**
 *
 * @author REx
 */
public abstract class CommandNode
{
    public abstract boolean isComparator();
    
    public abstract boolean isKeySetOperation();

    public abstract ComparatorCommand getAsComparator();

    public abstract KeySetCommand getAsKeySetOperator();
}
