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
public class Operator_Equals implements RPNFunction
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
        if (left.type != right.type)
        {
            throw new IllegalArgumentException("type mismatch for comparison: "
                    + "both types must be the same");
        }
        if (left.type == TokenType.INTEGER)
        {
            return new TokenPair(null, TokenType.BOOLEAN, 
                    ((Integer)left.value) == ((Integer)right.value));
        }
        if (left.type == TokenType.DOUBLE)
        {
            return new TokenPair(null, TokenType.BOOLEAN, 
                    0.0001 >= Math.abs(((Double)left.value) - ((Double)right.value)));
        }
        if (left.type == TokenType.NULL)
        {
            return new TokenPair(null, TokenType.BOOLEAN, true);
        }
        return new TokenPair(null, TokenType.BOOLEAN, left.value.equals(right.value));
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
