/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.command;

import com.rf.fled.database.IDatabase;
import com.rf.fled.database.result.IResultSet;
import com.rf.fled.script.tokenizer.TokenPair;

/**
 *
 * @author REx
 */
public interface IDatabaseCommand 
{
    public IResultSet execute(
            IDatabase tables, 
            TokenPair[] statements)
            throws CommandException;
}
