/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

/**
 *
 * @author REx
 */
public class TokenizerParseException extends Exception
{

    public TokenizerParseException(Throwable cause)
    {
        super(cause);
    }

    public TokenizerParseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TokenizerParseException(String message)
    {
        super(message);
    }

    public TokenizerParseException()
    {
    }
    
}
