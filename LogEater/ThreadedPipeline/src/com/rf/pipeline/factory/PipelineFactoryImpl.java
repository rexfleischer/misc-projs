/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.factory;

import com.rf.pipeline.Pipeline;
import com.rf.pipeline.PipelineException;
import com.rf.pipeline.PipelineFactory;
import com.rf.pipeline.config.PipeDefinition;
import com.rf.pipeline.config.QueueDefinition;
import com.rf.pipeline.impls.PipelineMap;
import java.util.LinkedList;

/**
 * 
 * @author REx
 */
public class PipelineFactoryImpl extends PipelineFactory
{
    private LinkedList<PipeDefinition> pipes;
    
    private LinkedList<QueueDefinition> queues;
    
    private PipelineMap mapping;
    
    public PipelineFactoryImpl()
    {
        pipes   = new LinkedList<PipeDefinition>();
        queues  = new LinkedList<QueueDefinition>();
    }

    @Override
    public void layDefinition(PipeDefinition definition) throws PipelineException
    {
        if (mapping != null)
        {
            throw new IllegalStateException("initPipeline() was already called");
        }
        pipes.add(definition);
    }

    @Override
    public void layDefinition(QueueDefinition definition) throws PipelineException
    {
        if (mapping != null)
        {
            throw new IllegalStateException("initPipeline() was already called");
        }
        queues.add(definition);
    }

    @Override
    public Pipeline initPipeline() throws PipelineException
    {
        return new PipelineMap(pipes, queues);
    }
}
