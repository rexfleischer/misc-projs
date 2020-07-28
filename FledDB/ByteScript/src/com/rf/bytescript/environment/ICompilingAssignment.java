/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.environment.IFunction;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.ByteCodeWriter;
import com.rf.bytescript.value.Value;
import java.util.List;

/**
 *
 * @author REx
 */
public interface ICompilingAssignment extends IFunction 
{
    public void compile (
            CompilingEnvironment enviornment, 
            ByteCodeWriter writer, 
            List<Value> statement)
            throws ByteScriptSyntaxException;
}
