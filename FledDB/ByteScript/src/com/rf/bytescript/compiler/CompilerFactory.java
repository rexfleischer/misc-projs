/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.bytescript.compiler;

import com.rf.bytescript.exception.ByteScriptInitException;
import com.rf.bytescript.exception.CompilationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 *
 * @author REx
 */
public enum CompilerFactory
{
    GENERAL(Compiler_General.class);
    
    private Class compiler;
    
    private CompilerFactory(Class clazz) 
    {
        this.compiler = clazz;
    }
    
    public ICompiler getInstance(Properties properties) 
            throws  NoSuchMethodException, 
                    InstantiationException, 
                    IllegalAccessException, 
                    IllegalArgumentException, 
                    InvocationTargetException, 
                    ByteScriptInitException, 
                    CompilationException
    {
        /**
         * get the constructor of the compiler
         */
        Constructor contructor = compiler.getConstructor();
        
        /**
         * create a new instance
         */
        ICompiler compilerInstance = (ICompiler) contructor.newInstance();
        
        /**
         * initiate the compiler with the functions
         */
        compilerInstance.init(properties);
        
        return compilerInstance;
    }
}
