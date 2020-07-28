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
public class Function_Tan implements RPNFunction 
{
    @Override
    public TokenPair compute(TokenPair[] params, RPNExecutionState state) 
    {
        if (params.length != 1)
        {
            throw new IllegalArgumentException("invalid amount of params");
        }
        TokenPair left = RPNUtil.resolve(state, params[0]);
        if (left.type == TokenType.INTEGER)
        {
            return new TokenPair(null, TokenType.INTEGER, Math.tan(((Integer) left.value)));
        }
        if (left.type == TokenType.DOUBLE)
        {
            return new TokenPair(null, TokenType.DOUBLE, Math.tan(((Double) left.value)));
        }
        throw new IllegalArgumentException(
                    "invalid datatype for division: " + left);
    }

    @Override
    public int numOfParams() 
    {
        return 1;
    }

    @Override
    public int getPresedence() 
    {
        return 1;
    }

    @Override
    public TokenType getType() 
    {
        return TokenType.FUNCTION;
    }
}
