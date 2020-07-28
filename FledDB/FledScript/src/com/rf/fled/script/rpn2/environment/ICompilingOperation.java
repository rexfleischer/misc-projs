/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.environment;

import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.language.Function;
import com.rf.fled.script.rpn2.util.ByteCodeWriter;

/**
 *
 * @author REx
 */
public interface ICompilingOperation extends Function 
{
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer)
            throws FledSyntaxException;
    
    public boolean isAlwaysLast();
}
