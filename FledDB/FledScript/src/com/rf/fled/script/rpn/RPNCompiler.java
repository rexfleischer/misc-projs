/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn;

import com.rf.fled.script.rpn.controls.RPNConstruct;
import com.rf.fled.script.rpn.controls.RPNStatementQueue;
import com.rf.fled.script.ExpressionCompiler;
import com.rf.fled.script.ExpressionExecutor;
import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.language.FunctionSet;
import com.rf.fled.script.UndefinedFunctionException;
import com.rf.fled.script.rpn.controls.RPNStatement;
import com.rf.fled.script.rpn.controls.RPNStatementConstruct;
import com.rf.fled.script.tokenizer.InfixToRPNConverter;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import com.rf.fled.script.tokenizer.Tokenizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class RPNCompiler implements ExpressionCompiler
{
    private FunctionSet functions;
    
    private Properties properties;
    
    private InfixToRPNConverter converter;

    @Override
    public void init(FunctionSet functions, Properties properties) 
    {
        this.functions  = functions;
        this.properties = properties;
        this.converter  = new InfixToRPNConverter(functions);
    }

    @Override
    public ExpressionExecutor compile(String expression) 
            throws FledSyntaxException, UndefinedFunctionException 
    {
        ArrayList<TokenPair> infix = (new Tokenizer(functions)).tokenize(expression);
        
        List<RPNStatement> statements = compile(infix, null);
        
        return new RPNExecutor(statements, functions);
    }
    
    public List<RPNStatement> compile(List<TokenPair> infix, List<RPNStatement> buffer) 
            throws FledSyntaxException, UndefinedFunctionException
    {
        if (buffer == null)
        {
            buffer = new LinkedList<RPNStatement>();
        }
        LinkedList<TokenPair> statement = new LinkedList<TokenPair>();
        
        for(int i = 0; i < infix.size(); i++)
        {
            TokenPair token = infix.get(i);
            
            if (token.type == TokenType.SEPERATOR)
            {
                buffer.add(new RPNStatementQueue(converter.convert(statement)));
                statement = new LinkedList<TokenPair>();
            }
            else if (token.type == TokenType.BRACKET)
            {
                if (!statement.isEmpty())
                {
                    throw new FledSyntaxException("illegal bracket placement");
                }
                /**
                 * we just push everything to the next iteration
                 */
                token.value = false;
                buffer.add(new RPNStatementConstruct(
                        token, null, buffer.size() + 1, buffer.size() + 1));
            }
            else if (token.type == TokenType.CONSTRUCT)
            {
                if (!statement.isEmpty())
                {
                    throw new FledSyntaxException(
                            "illegal start of a construct: " + token);
                }
                i += RPNUtil.compileConstruct(buffer, infix, i, this, true);
            }
            else
            {
                statement.add(token);
            }
        }
        
        if (!statement.isEmpty())
        {
            buffer.add(new RPNStatementQueue(converter.convert(statement)));
        }
        
        return buffer;
    }
    
    public FunctionSet getFunctions()
    {
        return functions;
    }

    protected boolean doesChain(
            RPNConstruct thisConstruct,
            TokenPair get) 
            throws UndefinedFunctionException 
    {
        /**
         * we need to first make sure that thisConstruct can chain 
         * with nextConstruct
         */
        boolean found = false;
        for(String key : thisConstruct.chainsWith())
        {
            if (get.name.equals(key))
            {
                return true;
            }
        }
        return false;
    }
}
