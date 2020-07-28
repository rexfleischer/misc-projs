/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline;

/**
 *
 * @author REx
 */
public class PipelineWorkException extends Exception
{

    public PipelineWorkException(Throwable cause)
    {
        super(cause);
    }

    public PipelineWorkException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PipelineWorkException(String message)
    {
        super(message);
    }

    public PipelineWorkException()
    {
    }
    
}
