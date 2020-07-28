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
public class Function_Echo implements RPNFunction
{

    @Override
    public TokenPair compute(TokenPair[] params, RPNExecutionState state) 
    {
        if (params.length != 1)
        {
            throw new IllegalArgumentException("invalid amount of params");
        }
        
        TokenPair token = RPNUtil.resolve(state, params[0]);
        
        if (token.type != TokenType.STRING &&
            token.type != TokenType.INTEGER &&
            token.type != TokenType.DOUBLE &&
            token.type != TokenType.BOOLEAN &&
            token.type != TokenType.NULL)
        {
            throw new IllegalArgumentException("invalid param datatype");
        }
        
        state.getOutputStream().print(token.value);
        return null;
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
