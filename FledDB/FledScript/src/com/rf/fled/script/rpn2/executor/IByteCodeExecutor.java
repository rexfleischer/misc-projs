/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.executor;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import com.rf.fled.script.rpn2.util.ByteCodeReader;
import com.rf.fled.script.rpn2.util.Value;

/**
 *
 * @author REx
 */
public interface IByteCodeExecutor
{
    public void init(ExecutionEnvironment environment)
            throws FledExecutionException;
    
    public Value execute(ByteCodeReader function)
            throws FledExecutionException;
}
