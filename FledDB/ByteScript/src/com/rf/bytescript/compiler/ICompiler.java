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
public interface ICompiler
{
    /**
     * 
     * @param properties
     * @throws ByteScriptInitException 
     */
    public void init (Properties properties)
            throws ByteScriptInitException;
    
    /**
     * this is for compiling an entire expression from a string down
     * @param expression
     * @return
     * @throws ByteScriptInitException
     * @throws ByteScriptSyntaxException
     * @throws CompilationException 
     */
    public IByteCodeExecutor compile(String expression)
            throws  ByteScriptInitException,
                    ByteScriptSyntaxException,
                    CompilationException;
    
    /**
     * more for internal use, but compiling needs to be a little
     * recursive with the algorithms we're using.
     * @param infix
     * @param writer
     * @param enviornment
     * @param position
     * @return
     * @throws ByteScriptSyntaxException
     * @throws CompilationException
     * @throws UndefinedFunctionException
     * @throws UndefinedVariableException 
     */
    public int compile(
            List<Value> infix, 
            ByteCodeWriter writer,
            CompilingEnvironment enviornment,
            int position)
            throws  ByteScriptSyntaxException,
                    CompilationException, 
                    UndefinedFunctionException, 
                    UndefinedVariableException;
}
