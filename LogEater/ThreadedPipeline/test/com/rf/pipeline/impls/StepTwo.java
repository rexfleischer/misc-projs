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
public class StepTwo extends WorkIterator
{
    public StepTwo()
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
        WorkQueue output = outputs.get("output");
        for(int i = 0; i < 100; i++)
        {
            Object funny = (String) input.pop();
            if (funny == null)
            {
                return false;
            }
            if (i == 0)
            {
                println("passing messages starting at [" + funny + "]");
            }
            output.push(funny);
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
        System.out.println("[StepTwo ("+this.getId()+") time: "+System.currentTimeMillis()+" message: "+msg+"]");
    }
}
