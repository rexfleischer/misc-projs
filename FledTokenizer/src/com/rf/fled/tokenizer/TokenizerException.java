/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

/**
 *
 * @author REx
 */
public class TokenizerException extends Exception
{

    public TokenizerException(Throwable cause)
    {
        super(cause);
    }

    public TokenizerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TokenizerException(String message)
    {
        super(message);
    }

    public TokenizerException()
    {
    }
    
}
