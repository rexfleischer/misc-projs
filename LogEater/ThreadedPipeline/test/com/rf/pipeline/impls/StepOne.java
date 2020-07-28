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
public class StepOne extends WorkIterator
{
    private int count = 0;
    
    private int amount = 0;
    
    public StepOne()
    {

    }

    @Override
    public synchronized void init(String context) throws PipelineWorkException
    {
        println("init with context " + context);
    }

    @Override
    protected synchronized boolean doWork(WorkQueue input, 
                             Map<String, WorkQueue> outputs,
                             Map<String, Object> routed, 
                             Map<String, Object> global) 
            throws PipelineWorkException
    {
        if (amount == 0)
        {
            amount = (Integer) routed.get("amount");
        }
        if (count == amount)
        {
            return false;
        }
        WorkQueue output = outputs.get("output");
        println("sending message from [hola! " + count + "]");
        for(int i = 0; 
            i < 100 && count < amount; 
            i++, count++)
        {
            output.push("hola! " + count);
        }
        return true;
    }

    @Override
    public synchronized void close() throws PipelineWorkException
    {
        println("closing");
    }

    @Override
    public synchronized int estimateWorkLeft(
                                Map<String, Object> routed, 
                                Map<String, Object> global) 
            throws PipelineWorkException
    {
        if (amount == 0)
        {
            amount = (Integer) routed.get("amount");
        }
        int check = (amount - count) / 100;
        if (check == 0)
        {
            return (amount == count) ? 0 : 1;
        }
        return check;
    }

    private void println(String msg)
    {
        System.out.println("[StepOne ("+this.getId()+") time: "+System.currentTimeMillis()+" message: "+msg+"]");
    }
}
