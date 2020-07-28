/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

/**
 *
 * @author REx
 */
public class TokenRuleOverlapException extends Exception
{

    public TokenRuleOverlapException(Throwable cause)
    {
        super(cause);
    }

    public TokenRuleOverlapException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TokenRuleOverlapException(String message)
    {
        super(message);
    }

    public TokenRuleOverlapException()
    {
    }
    
}
