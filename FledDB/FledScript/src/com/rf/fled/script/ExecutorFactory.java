/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script;

import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.rpn.RPNCompiler;
import com.rf.fled.script.rpn2.RPN2Compiler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 *
 * @author REx
 */
public enum ExecutorFactory 
{
    RPN2    (RPN2Compiler.class),
    RPN     (RPNCompiler.class);
    
    
    
    private Class compiler;
    
    private ExecutorFactory(Class clazz)
    {
        this.compiler = clazz;
    }
    
    public ExpressionCompiler getInstance(FunctionSet functions, Properties properties)
            throws  NoSuchMethodException, 
                    InstantiationException, 
                    IllegalAccessException,
                    IllegalArgumentException, 
                    InvocationTargetException
    {
        /**
         * get the constructor of the compiler
         */
        Constructor contructor = compiler.getConstructor();
        
        /**
         * create a new instance
         */
        ExpressionCompiler compilerInstance = (ExpressionCompiler) contructor.newInstance();
        
        /**
         * initiate the compiler with the functions
         */
        compilerInstance.init(functions, properties);
        
        return compilerInstance;
    }
}
