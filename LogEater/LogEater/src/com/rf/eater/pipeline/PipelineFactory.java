/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

/**
 *
 * @author REx
 */
public enum PipelineFactory
{
    BASIC()
    {
        @Override
        public Pipeline getPipeline()
        {
            return new PipelineWork_Basic();
        }
    };
    
    public abstract Pipeline getPipeline();
}
