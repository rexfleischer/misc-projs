/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.tokenizer;

/**
 * these are for tokens that are special strings and act as some
 * sort of other type. for instance, the code:
 * var name = 'value';
 * the tokenizer will recognize 'var' and make that token a
 * declaration token. same with constructs.
 * it also lets assignment operators and other special operators
 * work better with the tokenizer because you can make operators
 * be different things as well with this.
 * @author REx
 */
public class TypePrecedencePair
{
    /**
     * this is the string that actually is required to cause the 
     * precedence and type to change in the tokenizer
     */
    public final String name;
    
    /**
     * the precedence of this token. most of the type, this will not be 
     * needed except for special cases like text operators such as AND or OR
     */
    public final int precedence;
    
    /**
     * the type the token it will be if the token string matches name. 
     */
    public final TokenType type;
    
    /**
     * 
     * @param name
     * @param precedence
     * @param type 
     */
    public TypePrecedencePair(String name, int precedence, TokenType type)
    {
        this.name       = name;
        this.precedence = precedence;
        this.type       = type;
    }
}
