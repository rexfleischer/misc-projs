/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.language;

import com.rf.fled.script.tokenizer.TokenType;

/**
 *
 * @author REx
 */
public interface Function 
{
    public int getPresedence();
    
    public TokenType getType();
}
