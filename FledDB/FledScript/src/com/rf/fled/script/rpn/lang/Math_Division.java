/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn.lang;

import com.rf.fled.script.rpn.RPNExecutionState;
import com.rf.fled.script.rpn.controls.RPNFunction;
import com.rf.fled.script.rpn.RPNUtil;
import com.rf.fled.script.tokenizer.TokenPair;
import com.rf.fled.script.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public class Math_Division implements RPNFunction 
{
    @Override
    public TokenPair compute(TokenPair[] params, RPNExecutionState state) 
    {
        if (params.length != 2)
        {
            throw new IllegalArgumentException("invalid amount of params");
        }
        TokenPair left = RPNUtil.resolve(state, params[0]);
        TokenPair right = RPNUtil.resolve(state, params[1]);
        if (left.type == TokenType.INTEGER && right.type == TokenType.INTEGER)
        {
            return new TokenPair(null, TokenType.INTEGER, 
                    ((Integer)left.value) / ((Integer)right.value));
        }
        else if (left.type == TokenType.DOUBLE && right.type == TokenType.DOUBLE)
        {
            return new TokenPair(null, TokenType.DOUBLE, 
                    ((Double)left.value) / ((Double)right.value));
        }
        throw new IllegalArgumentException("invalid datatype for operation");
    }

    @Override
    public int numOfParams() 
    {
        return 2;
    }

    @Override
    public int getPresedence() 
    {
        return 2;
    }

    @Override
    public TokenType getType() 
    {
        return TokenType.OPERATOR;
    }
}
