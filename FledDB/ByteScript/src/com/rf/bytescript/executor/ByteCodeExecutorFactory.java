/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.executor;

import com.rf.bytescript.environment.ExecutionEnvironment;
import com.rf.bytescript.exception.ByteScriptInitException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author REx
 */
public enum ByteCodeExecutorFactory
{
    ARRAY(ByteCodeExecutor_Array.class),;
//    SWITCH(ByteCodeExecutor_Switch.class);
    
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
                    ByteScriptInitException
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
