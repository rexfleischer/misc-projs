/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.impls;

import com.rf.pipeline.PipelineWorkException;
import com.rf.pipeline.WorkIterator;
import com.rf.pipeline.queue.WorkQueue;
import java.util.Map;

/**
 *
 * @author REx
 */
public class StepThree extends WorkIterator
{
    public StepThree()
    {

    }

    @Override
    public void init(String context) throws PipelineWorkException
    {
        println("init with context " + context);
    }

    @Override
    protected boolean doWork(WorkQueue input, 
                             Map<String, WorkQueue> outputs,
                             Map<String, Object> routed, 
                             Map<String, Object> global) 
            throws PipelineWorkException
    {
        for(int i = 0; i < 100; i++)
        {
            String funny = (String) input.pop();
            if (funny == null)
            {
                return false;
            }
            if (i == 0)
            {
                println("received messages starting at [" + funny + "]");
            }
        }
        return true;
    }

    @Override
    public void close() throws PipelineWorkException
    {
        println("closing");
    }

    @Override
    public int estimateWorkLeft(Map<String, Object> routed, 
                                Map<String, Object> global) 
            throws PipelineWorkException
    {
        return 0;
    }

    private void println(String msg)
    {
        System.out.println("[StepThree ("+this.getId()+") time: "+System.currentTimeMillis()+" message: "+msg+"]");
    }
}
