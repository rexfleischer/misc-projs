/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn.lang;

import com.rf.fled.script.FledExecutionException;
import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.UndefinedFunctionException;
import com.rf.fled.script.rpn.RPNCompiler;
import com.rf.fled.script.rpn.controls.RPNConstruct;
import com.rf.fled.script.rpn.RPNExecutionState;
import com.rf.fled.script.rpn.RPNExecutor;
import com.rf.fled.script.rpn.RPNUtil;
import com.rf.fled.script.rpn.controls.RPNStatement;
import com.rf.fled.script.rpn.controls.RPNStatementConstruct;
import com.rf.fled.script.rpn.controls.RPNStatementQueue;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author REx
 */
public class Construct_For implements RPNConstruct
{
    @Override
    public boolean resolvesTrue(
            RPNStatementQueue[] constructParams, 
            RPNExecutionState state, 
            RPNExecutor executor, 
            boolean firstIteration) 
            throws FledExecutionException 
    {
        if (constructParams.length != 3)
        {
            throw new IllegalArgumentException(
                    "invalid amount of params in construct");
        }
        
        if (firstIteration)
        {
            executor.execute(state, constructParams[0]);
        }
        else
        {
            executor.execute(state, constructParams[2]);
        }
        
        TokenPair result = executor.execute(state, constructParams[1]);
        if (result == null)
        {
            throw new IllegalArgumentException(
                    "for loop param does not resolve");
        }
        
        if (result.type != TokenType.BOOLEAN)
        {
            throw new IllegalArgumentException(
                    "for loop param does not return a boolean");
        }
        
        return (Boolean) result.value;
    }
    
    @Override
    public int compile(
            TokenPair thisToken, 
            List<TokenPair> infix, 
            int cursor, 
            List<RPNStatement> buffer, 
            RPNCompiler compiler) 
            throws FledSyntaxException, UndefinedFunctionException 
    {
        int start = cursor;
        
        /**
         * if its a construct, then we first need to get the 
         * information between the ()'s and then compile the 
         * inside of them.
         */
        List<RPNStatement> compiledParams = new LinkedList<RPNStatement>();
        cursor += RPNUtil.compileConstructParams(compiledParams, infix, cursor, compiler);
        
        
        /**
         * now we have to build the actual construct
         */
        RPNStatementConstruct realConstruct = new RPNStatementConstruct(
                thisToken, compiledParams, buffer.size() + 1, buffer.size() + 1);
        int constructPosition = buffer.size();
        buffer.add(realConstruct);

        /**
         * now, if there is an open bracket after, then we compile 
         * the internal of that and then set up the bracket constructs.
         * brackets are required as of now.
         * note: we setup brackets so the executor knows when to push
         * stacks and such.
         */
        if (!infix.get(cursor + 1).name.equals("{"))
        {
            throw new FledSyntaxException(
                    "brackets required with for-loop constructs");
        }
        RPNStatementConstruct leftBracket = new RPNStatementConstruct(
                infix.get(cursor + 1), null, buffer.size() + 1, buffer.size() + 1);
        buffer.add(leftBracket);
        
        cursor += RPNUtil.compileConstructBrackets(buffer, infix, cursor, compiler);

        TokenPair constructEnd = infix.get(cursor);
        constructEnd.value = true;
        RPNStatementConstruct rightBracket = new RPNStatementConstruct(
                constructEnd, null, constructPosition, constructPosition);
        buffer.add(rightBracket);
        
        realConstruct.setIfResolvesFalse(buffer.size());
        
        cursor++; // the right bracket
        
        return cursor - start;
    }
    
    @Override
    public boolean canInitiateChains() 
    {
        return true;
    }

    @Override
    public String[] chainsWith() 
    {
        return new String[]{};
    }

    @Override
    public boolean iterative() 
    {
        return true;
    }

    @Override
    public int paramsInParathesis() 
    {
        return 3;
    }

    @Override
    public int getPresedence() 
    {
        return 1;
    }

    @Override
    public TokenType getType() 
    {
        return TokenType.CONSTRUCT;
    }
    
}
