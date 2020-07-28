/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.compiler;

import com.rf.bytescript.environment.CompilingEnvironment;
import com.rf.bytescript.exception.ByteScriptInitException;
import com.rf.bytescript.exception.ByteScriptSyntaxException;
import com.rf.bytescript.exception.CompilationException;
import com.rf.bytescript.exception.UndefinedFunctionException;
import com.rf.bytescript.exception.UndefinedVariableException;
import com.rf.bytescript.executor.IByteCodeExecutor;
import com.rf.bytescript.util.ByteCodeWriter;
import com.rf.bytescript.value.Value;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class Compiler_General implements ICompiler
{

    @Override
    public void init (Properties properties) throws ByteScriptInitException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IByteCodeExecutor compile (String expression) 
            throws  ByteScriptInitException, 
                    ByteScriptSyntaxException, 
                    CompilationException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compile (
            List<Value> infix, 
            ByteCodeWriter writer, 
            CompilingEnvironment enviornment, 
            int position) 
            throws  ByteScriptSyntaxException, 
                    CompilationException, 
                    UndefinedFunctionException,
                    UndefinedVariableException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
