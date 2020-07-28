/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.ByteCodeWriter;

/**
 *
 * @author REx
 */
public interface ICompilingOperation extends IFunction 
{
    public void compile (ExecutionEnvironment environment, ByteCodeWriter writer)
            throws ByteScriptSyntaxException;
    
    public boolean isAlwaysLast();
}
