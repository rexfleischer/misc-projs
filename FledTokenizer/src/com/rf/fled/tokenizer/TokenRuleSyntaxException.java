/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

/**
 *
 * @author REx
 */
public class TokenRuleSyntaxException extends Exception
{

    public TokenRuleSyntaxException(Throwable cause)
    {
        super(cause);
    }

    public TokenRuleSyntaxException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TokenRuleSyntaxException(String message)
    {
        super(message);
    }

    public TokenRuleSyntaxException()
    {
    }
    
}
