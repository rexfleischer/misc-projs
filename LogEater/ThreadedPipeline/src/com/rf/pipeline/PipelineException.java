/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline;

/**
 *
 * @author REx
 */
public class PipelineException extends Exception
{

    public PipelineException(Throwable cause)
    {
        super(cause);
    }

    public PipelineException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PipelineException(String message)
    {
        super(message);
    }

    public PipelineException()
    {
    }
    
}
