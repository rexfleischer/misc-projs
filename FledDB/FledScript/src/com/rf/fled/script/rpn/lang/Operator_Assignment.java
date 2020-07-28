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
public class Operator_Assignment implements RPNFunction
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
        state.setVariable(params[0].name, RPNUtil.resolve(state, params[1]));
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
