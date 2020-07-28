/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.lang;

import com.rf.fled.lang.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public interface IFunction
{
    public int getPresedence();
    
    public TokenType getType();
    
    public int numOfParams();
}
