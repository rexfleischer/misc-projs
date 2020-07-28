/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.bytecode;

/**
 *
 * @author REx
 */
public class ByteCodeAggregatorException extends Exception
{

    public ByteCodeAggregatorException (Throwable cause)
    {
        super(cause);
    }

    public ByteCodeAggregatorException (String message, Throwable cause)
    {
        super(message, cause);
    }

    public ByteCodeAggregatorException (String message)
    {
        super(message);
    }

    public ByteCodeAggregatorException ()
    {
    }
    
}
