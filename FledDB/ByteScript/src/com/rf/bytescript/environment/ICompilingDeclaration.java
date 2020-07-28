/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.environment;

import com.rf.bytescript.compiler.ICompiler;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.util.ByteCodeWriter;
import com.rf.bytescript.value.Value;
import java.util.List;

/**
 *
 * @author REx
 */
public interface ICompilingDeclaration extends IFunction 
{
    public int compile (
            CompilingEnvironment environment, 
            ByteCodeWriter writer,
            List<Value> statement,
            ICompiler compiler,
            int position)
            throws ByteScriptSyntaxException;
}
