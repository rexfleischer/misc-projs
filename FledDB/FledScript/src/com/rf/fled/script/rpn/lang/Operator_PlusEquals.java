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
public class Operator_PlusEquals implements RPNFunction
{
    @Override
    public TokenPair compute(TokenPair[] params, RPNExecutionState state) 
    {
        if (params.length != 2)
        {
            throw new IllegalArgumentException("invalid amount of params");
        }
        if (params[0].type != TokenType.VARIABLE)
        {
            throw new IllegalArgumentException("can only assign values to a variable");
        }
        TokenPair left = RPNUtil.resolve(state, params[0]);
        TokenPair right = RPNUtil.resolve(state, params[1]);
        if (left.type == TokenType.INTEGER && right.type == TokenType.INTEGER)
        {
            state.setVariable(params[0].name, 
                    new TokenPair(null, TokenType.INTEGER, 
                            ((Integer)left.value) + ((Integer)right.value)));
        }
        else if (left.type == TokenType.DOUBLE && right.type == TokenType.DOUBLE)
        {
            state.setVariable(params[0].name, 
                    new TokenPair(null, TokenType.DOUBLE, 
                            ((Double)left.value) + ((Double)right.value)));
        }
        else if (left.type == TokenType.STRING)
        {
            state.setVariable(params[0].name, 
                    new TokenPair(null, TokenType.STRING, 
                            ((String)left.value) + right.value));
        }
        else
        {
            throw new IllegalArgumentException("invalid datatype for operation");
        }
        return null;
    }

    @Override
    public int numOfParams() 
    {
        return 2;
    }

    @Override
    public int getPresedence() 
    {
        return 0;
    }

    @Override
    public TokenType getType() 
    {
        return TokenType.OPERATOR;
    }
}
