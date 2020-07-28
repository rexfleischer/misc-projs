/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.executor;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author REx
 */
public enum ByteCodeExecutorFactory
{
    ARRAY(ByteCodeExecutor_Array.class),
    SWITCH(ByteCodeExecutor_Switch.class);
    
    private Class executor;
    
    private ByteCodeExecutorFactory(Class executor)
    {
        this.executor = executor;
    }
    
    public IByteCodeExecutor getInstance(ExecutionEnvironment enviornment)
            throws  NoSuchMethodException, 
                    InstantiationException, 
                    IllegalAccessException,
                    IllegalArgumentException, 
                    InvocationTargetException, 
                    FledExecutionException
    {
        /**
         * get the constructor of the compiler
         */
        Constructor contructor = executor.getConstructor();
        
        /**
         * create a new instance
         */
        IByteCodeExecutor compilerInstance = (IByteCodeExecutor) contructor.newInstance();
        
        /**
         * initialize
         */
        compilerInstance.init(enviornment);
        
        return compilerInstance;
    }
}
