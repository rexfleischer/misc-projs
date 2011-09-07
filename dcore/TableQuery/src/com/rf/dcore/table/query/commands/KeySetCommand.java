/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.query.commands;

import com.rf.dcore.operation.KeySetOperation;
import com.rf.dcore.operation.KeySetOperations;

/**
 *
 * @author REx
 */
public class KeySetCommand
{
    public final KeySetOperation operation;
    
    public KeySetCommand(KeySetOperations operation)
    {
        this.operation = operation.getOperation();
    }
}
