/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn;

import com.rf.fled.script.rpn.controls.RPNConstruct;
import com.rf.fled.script.FledSyntaxException;
import com.rf.fled.script.UndefinedFunctionException;
import com.rf.fled.script.rpn.controls.RPNStatement;
import com.rf.fled.script.rpn.controls.RPNStatementConstruct;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author REx
 */
public class RPNUtil 
{
    public static TokenPair resolve(RPNExecutionState state, TokenPair token)
    {
        if (token.type != TokenType.VARIABLE)
        {
            if (token.type != TokenType.BOOLEAN &&
                token.type != TokenType.NULL &&
                token.type != TokenType.STRING &&
                token.type != TokenType.INTEGER &&
                token.type != TokenType.DOUBLE)
            {
                throw new IllegalArgumentException(
                        "invalid type for a variable: " + token);
            }
            return token;
        }
        return state.getVariable(token.name);
    }
    
    public static int compileConstruct(
            List<RPNStatement> result,
            List<TokenPair> infix, 
            Integer cursor,
            RPNCompiler compiler,
            boolean subtract)
            throws FledSyntaxException, UndefinedFunctionException
    {
        int start = cursor;
        
        TokenPair token = infix.get(cursor);
        RPNConstruct thisConstruct = (RPNConstruct) 
                compiler.getFunctions().getFunction(token.name);
        if (!thisConstruct.canInitiateChains())
        {
            throw new FledSyntaxException(
                    "illegal decleration of a construct: " + token 
                    + " cannot initiate chains");
        }
        cursor += thisConstruct.compile(token, infix, cursor, result, compiler);

        /**
         * now we have to make sure that if they have to
         * chain that they can.
         */
        if (thisConstruct.canInitiateChains() &&
            infix.size() > cursor && 
            compiler.doesChain(thisConstruct, infix.get(cursor)))
        {
            /**
             * we need to keep track of all the constructs that are
             * chained together because when one is selected, then 
             * we need to be able to tell the program cursor where
             * to go after.
             */
            LinkedList<Integer> updatePositions = new LinkedList<Integer>();
            do
            {
                TokenPair thisToken = infix.get(cursor);
                updatePositions.add(result.size() - 1);
                thisConstruct = (RPNConstruct) compiler
                        .getFunctions()
                        .getFunction(thisToken.name);
                cursor += thisConstruct.compile(
                        thisToken, infix, cursor, result, compiler);
            }
            while(infix.size() > cursor && 
                    compiler.doesChain(thisConstruct, infix.get(cursor)));

            for(int i = 0; i < updatePositions.size(); i++)
            {
                Integer position = updatePositions.get(i);
                RPNStatementConstruct construct = 
                        (RPNStatementConstruct) result.get(position);
                
                if (construct.getConstructToken().name.equals("}"))
                {
                    boolean iterative = (Boolean) construct.getConstructToken().value;
                    if (!iterative)
                    {
                        construct.setIfResolvesTrue(result.size());
                    }
                }
                else
                {
                    construct.setIfResolvesFalse(result.size());
                }
            }
        }
        
        if (subtract)
        {
            cursor--;
        }
        
        return cursor - start;
    }
    
    public static int compileConstructParams(
            List<RPNStatement> result,
            List<TokenPair> infix, 
            Integer cursor,
            RPNCompiler compiler)
            throws FledSyntaxException, UndefinedFunctionException
    {
        int start = cursor;
        if (!infix.get(cursor + 1).name.equals("("))
        {
            throw new FledSyntaxException(
                    "illegal start of a construct");
        }
        LinkedList<TokenPair> params = new LinkedList<TokenPair>();
        
        int paraStack = 1;
        boolean foundEnd = false;
        for(cursor += 2; cursor < infix.size(); cursor++)
        {
            TokenPair paramToken = infix.get(cursor);
            if (infix.get(cursor).name.equals("("))
            {
                paraStack++;
            }
            else if (infix.get(cursor).name.equals(")"))
            {
                paraStack--;
                if (paraStack == 0)
                {
                    foundEnd = true;
                    break;
                }
            }
            params.add(paramToken);
        }

        if (!foundEnd)
        {
            throw new FledSyntaxException("illegal end of a construct");
        }
        
        result.addAll(compiler.compile(params, null));
        
        return cursor - start;
    }
    
    public static int compileNextStatement(
            List<RPNStatement> result,
            List<TokenPair> infix, 
            Integer cursor,
            RPNCompiler compiler)
            throws FledSyntaxException, UndefinedFunctionException
    {
        int start = cursor;
        LinkedList<TokenPair> tokens = new LinkedList<TokenPair>();
        for( ; cursor < infix.size(); cursor++)
        {
            TokenPair paramToken = infix.get(cursor);
            if (infix.get(cursor).name.equals(";"))
            {
                break;
            }
            else
            {
                tokens.add(paramToken);
            }
        }
        
        compiler.compile(tokens, result);
        
        return cursor - start;
    }
    
    public static int compileConstructBrackets(
            List<RPNStatement> result,
            List<TokenPair> infix, 
            Integer cursor,
            RPNCompiler compiler)
            throws FledSyntaxException, UndefinedFunctionException
    {
        int start = cursor;
        if (!infix.get(cursor + 1).name.equals("{"))
        {
            throw new FledSyntaxException(
                    "brackets required with constructs");
        }
        LinkedList<TokenPair> params = new LinkedList<TokenPair>();
        
        int paraStack = 1;
        boolean foundEnd = false;
        for(cursor += 2; cursor < infix.size(); cursor++)
        {
            TokenPair paramToken = infix.get(cursor);
            if (infix.get(cursor).name.equals("{"))
            {
                paraStack++;
            }
            else if (infix.get(cursor).name.equals("}"))
            {
                paraStack--;
                if (paraStack == 0)
                {
                    foundEnd = true;
                    break;
                }
            }
            params.add(paramToken);
        }

        if (!foundEnd)
        {
            throw new FledSyntaxException("bracket mismatch");
        }
        
        
        compiler.compile(params, result);
        
        return cursor - start;
    }
}
