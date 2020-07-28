/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.compiler.tokenizer;

import com.rf.fled.environment.util.OrderedNamedValueArray;

/**
 * the reason we decouple the token definitions from the things that do 
 * the compiling and such is because there is no reason to couple them
 * other than some minor conveniences... the main thing is, tokenizing 
 * is different from compiling, which is different then executing, not
 * to mention that there are different ways of doing all... so we do it
 * like this so we dont need to deal with coupled architecture later
 * if we ever add different ways of doing these things.
 * @author REx
 */
public class TokenizerFunctionSets 
{

    public enum FunctionSet
    {
        MATH,
        CONSTRUCTS
    }
    
    public static OrderedNamedValueArray<TypePrecedencePair> get(FunctionSet ... functions)
    {
        OrderedNamedValueArray<TypePrecedencePair> result = 
                new OrderedNamedValueArray<TypePrecedencePair>(5);
        
        for(FunctionSet set : functions)
        {
            switch(set)
            {
                case CONSTRUCTS:
                    result.setValue("if",       new TypePrecedencePair("if", 0, TokenType.CONSTRUCT));
                    result.setValue("else",     new TypePrecedencePair("else", 0, TokenType.CONSTRUCT));
                    result.setValue("do",       new TypePrecedencePair("do", 0, TokenType.CONSTRUCT));
                    result.setValue("while",    new TypePrecedencePair("while", 0, TokenType.CONSTRUCT));
                    result.setValue("for",      new TypePrecedencePair("for", 0, TokenType.CONSTRUCT));
                    result.setValue("foreach",  new TypePrecedencePair("foreach", 0, TokenType.CONSTRUCT));
                    break;
            }
        }
        
        return result;
    }
    
    public static OrderedNamedValueArray<TypePrecedencePair> getAll ()
    {
        return get(FunctionSet.MATH, FunctionSet.CONSTRUCTS);
    }
}
