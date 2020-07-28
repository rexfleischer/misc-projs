/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline;

import com.rf.pipeline.config.PipeDefinition;
import com.rf.pipeline.config.QueueDefinition;
import com.rf.pipeline.factory.PipelineFactoryImpl;

/**
 *
 * @author REx
 */
public abstract class PipelineFactory
{
    public static PipelineFactory getFactory()
    {
        return new PipelineFactoryImpl();
    }
    
    public abstract Pipeline initPipeline() throws PipelineException;
    
    public abstract void layDefinition(PipeDefinition definition) throws PipelineException;
    
    public abstract void layDefinition(QueueDefinition definition) throws PipelineException;
}
