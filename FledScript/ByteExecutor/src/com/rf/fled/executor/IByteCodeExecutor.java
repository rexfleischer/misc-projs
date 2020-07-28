/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.executor;

import com.rf.fled.environment.ExecutionEnvironment;
import com.rf.fled.environment.bytecode.ByteCode;
import com.rf.fled.environment.heap.HeapValue;

/**
 *
 * @author REx
 */
public interface IByteCodeExecutor
{
    public void init(ExecutionEnvironment environment)
            throws ByteExecutorInitException;
    
    public HeapValue execute(ByteCode function)
            throws ByteExecutorRuntimeException;
}
