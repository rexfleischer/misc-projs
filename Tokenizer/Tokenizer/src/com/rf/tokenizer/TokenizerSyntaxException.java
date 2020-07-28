/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

/**
 *
 * @author REx
 */
public class TokenizerSyntaxException extends Exception
{

    public TokenizerSyntaxException(Throwable cause)
    {
        super(cause);
    }

    public TokenizerSyntaxException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TokenizerSyntaxException(String message)
    {
        super(message);
    }

    public TokenizerSyntaxException()
    {
    }
    
}
