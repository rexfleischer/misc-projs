
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
public class Construct_If implements RPNConstruct
{
    @Override
    public boolean resolvesTrue(
            RPNStatementQueue[] constructParams, 
            RPNExecutionState state, 
            RPNExecutor executor,
            boolean firstIteration) throws FledExecutionException 
    {
        if (!firstIteration)
        {
            throw new IllegalArgumentException(
                    "illegal execution of a construct");
        }
        if (constructParams.length != 1)
        {
            throw new IllegalArgumentException(
                    "invalid amount in constructs for 'if' statement");
        }
        
        TokenPair result = executor.execute(state, constructParams[0]);
        if (result == null)
        {
            throw new IllegalArgumentException(
                    "if statement does not resolve");
        }
        if (result.type != TokenType.BOOLEAN)
        {
            throw new IllegalArgumentException(
                    "if statement doesnt not resolve to a boolean");
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
        
        RPNStatementConstruct realConstruct = new RPNStatementConstruct(
                    thisToken, compiledParams, buffer.size() + 1, buffer.size() + 2);
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
            /**
             * now we have to build the actual construct
             */
            if (infix.get(cursor + 1).type == TokenType.CONSTRUCT)
            {
                cursor++;
                cursor += RPNUtil.compileConstruct(buffer, infix, cursor, compiler, false);
            }
            else
            {
                cursor++;
                cursor += RPNUtil.compileNextStatement(buffer, infix, cursor, compiler);
            }
        }
        else
        {
            /**
             * first, add the left bracket. note: this will always only
             * move up two one space. 
             */
            RPNStatementConstruct leftBracket = new RPNStatementConstruct(
                    infix.get(cursor + 1), null, buffer.size() + 1, buffer.size() + 1);
            buffer.add(leftBracket);
            
            /**
             * compile whats in the bracket
             */
            cursor += RPNUtil.compileConstructBrackets(
                    buffer, infix, cursor, compiler);

            /**
             * now add the right bracket
             */
            TokenPair constructEnd = infix.get(cursor);
            constructEnd.value = false;
            RPNStatementConstruct rightBracket = new RPNStatementConstruct(
                        constructEnd, null, buffer.size() + 1, buffer.size() + 1);
            buffer.add(rightBracket);

            cursor++; // the right bracket
        }
        
        /**
         * dont forget to set where the if statement is supposed to go if
         * things dong work out
         */
        realConstruct.setIfResolvesFalse(buffer.size());
        
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
        return new String[]{"else"};
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

    @Override
    public int paramsInParathesis() 
    {
        return 1;
    }

    @Override
    public boolean iterative() 
    {
        return false;
    }
}
