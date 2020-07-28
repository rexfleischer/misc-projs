/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script;

import com.rf.fled.script.language.FunctionSet;
import java.util.Properties;

/**
 *
 * @author REx
 */
public interface ExpressionCompiler 
{
    public void init(FunctionSet functions, Properties properties);
    
    public ExpressionExecutor compile(String expression)
            throws FledSyntaxException, UndefinedFunctionException;
    
}
