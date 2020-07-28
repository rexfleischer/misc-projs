/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.generic;

import com.rf.fled.compiler.ByteScriptSyntaxException;
import com.rf.fled.compiler.ICompiler;
import com.rf.fled.compiler.tokenizer.InfixToRPNConverter;
import com.rf.fled.compiler.tokenizer.Token;
import com.rf.fled.compiler.tokenizer.Tokenizer;
import com.rf.fled.compiler.tokenizer.TokenizerFunctionSets;
import com.rf.fled.compiler.tokenizer.TypePrecedencePair;
import com.rf.fled.environment.ExecutionEnvironment;
import com.rf.fled.environment.bytecode.ByteCode;
import com.rf.fled.environment.bytecode.ByteCodeAggregator;
import com.rf.fled.environment.bytecode.ByteCodeAggregatorException;
import com.rf.fled.environment.util.OrderedNamedValueArray;
import java.util.List;

/**
 *
 * @author REx
 */
public class CompilerImpl implements ICompiler
{
    private Tokenizer tokenizer;
    
    private InfixToRPNConverter converter;
    
    public CompilerImpl ()
    {
        OrderedNamedValueArray<TypePrecedencePair> functions = 
                TokenizerFunctionSets.getAll();
        tokenizer = new Tokenizer(functions);
        converter = new InfixToRPNConverter(functions);
    }

    @Override
    public ByteCode compile (String expression) 
            throws ByteScriptSyntaxException
    {
        ByteCodeAggregator aggregator = null;
        try
        {
            aggregator = new ByteCodeAggregator();
        }
        catch (ByteCodeAggregatorException ex)
        {
            throw new ByteScriptSyntaxException(
                    "unable to initialize the byte code aggregator", ex);
        }
        List<Token> tokens = tokenizer.tokenize(expression);
        ExecutionEnvironment environment = new ExecutionEnvironment();
        
        ByteCodeWriteManager writer = new ByteCodeWriteManager(converter);
        writer.compile(aggregator, environment, tokens, 0);
        
        try
        {
            return aggregator.finished();
        }
        catch (ByteCodeAggregatorException ex)
        {
            throw new ByteScriptSyntaxException("unable to aggregate code", ex);
        }
    }
}
