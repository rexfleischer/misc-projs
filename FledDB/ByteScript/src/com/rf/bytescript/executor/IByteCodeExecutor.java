/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.executor;

import com.rf.bytescript.environment.ExecutionEnvironment;
import com.rf.bytescript.exception.ByteScriptInitException;
import com.rf.bytescript.exception.ByteScriptRuntimeException;
import com.rf.bytescript.util.ByteCodeReader;
import com.rf.bytescript.value.Value;

/**
 *
 * @author REx
 */
public interface IByteCodeExecutor
{
    public void init(ExecutionEnvironment environment)
            throws ByteScriptInitException;
    
    public Value execute(ByteCodeReader function)
            throws ByteScriptRuntimeException;
}
