/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2;

import com.rf.fled.script.rpn2.environment.ExecutionEnvironment;
import com.rf.fled.script.ExpressionExecutor;
import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.FledScriptInitException;
import com.rf.fled.script.rpn2.executor.ByteCodeExecutorFactory;
import com.rf.fled.script.rpn2.executor.IByteCodeExecutor;
import com.rf.fled.script.rpn2.util.ByteCodeReader;
import com.rf.fled.script.rpn2.environment.FunctionSet_Compiled;
import com.rf.fled.script.rpn2.util.Value;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class RPN2InitExecutor implements ExpressionExecutor
{
    private FunctionSet_Compiled code;
    
    private Properties properties;

    protected RPN2InitExecutor(FunctionSet_Compiled code, Properties properties)
    {
        this.code = code;
        this.properties = properties;
    }
    
    @Override
    public TokenPair execute (
            List<Object> input, 
            Map<String, TokenPair> variables, 
            boolean returnLast) 
            throws FledExecutionException
    {
        /**
         * first, lets load the main function
         */
        ByteCodeReader main = code.getMain();
        
        ExecutionEnvironment enviornment = null;
        try
        {
            enviornment = new ExecutionEnvironment(code);
        }
        catch(FledScriptInitException ex)
        {
            throw new FledExecutionException(
                    "could not initialize the execution enviroment", ex);
        }
        
        String executorName = properties.getProperty("executor");
        if (executorName == null || executorName.isEmpty())
        {
            throw new FledExecutionException("no executor specified");
        }
        IByteCodeExecutor executor = null;
        try
        {
            executor = ByteCodeExecutorFactory
                    .valueOf(executorName)
                    .getInstance(enviornment);
        }
        catch (Exception ex)
        {
            throw new FledExecutionException(
                    "could not initialize byte code executor", ex);
        }
        
        Value exitValue = executor.execute(main);
        
        switch(exitValue.type)
        {
            case BOOLEAN:
                return new TokenPair(null, TokenType.BOOLEAN, exitValue.value);
            case DOUBLE:
                return new TokenPair(null, TokenType.DOUBLE, exitValue.value);
            case INTEGER:
                return new TokenPair(null, TokenType.INTEGER, exitValue.value);
            case STRING:
                return new TokenPair(null, TokenType.STRING, exitValue.value);
            default:
                return null;
        }
    }
}
