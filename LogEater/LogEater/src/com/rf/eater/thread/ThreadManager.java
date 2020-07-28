/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.thread;

import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.consumer.WorkIterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * this class will balance the process of consuming threads and make threads
 * that have finished their work start other tasks.
 * @author REx
 */
public class ThreadManager
{
    protected class WorkPipelinePart
    {
        WorkFactory factory;
    }
    
    private WorkPipelinePart[] pipeline;
    
    private Map<String, LinkedList<WorkIterator>> allWork;
    
    public ThreadManager(int numOfWorkers, WorkFactory[] pipeline)
    {
        this.pipeline = new WorkPipelinePart[pipeline.length];
        for(int i = 0; i < pipeline.length; i++)
        {
            WorkPipelinePart part = new WorkPipelinePart();
            part.factory = pipeline[i];
            
            
            this.pipeline[i] = part;
        }
    }
    
    public synchronized void setWork(WorkIterator work)
    {
        if (allWork.containsKey(work.workType()))
        {
            allWork.get(work.workType()).add(work);
        }
        else
        {
            LinkedList<WorkIterator> newList = new LinkedList<WorkIterator>();
            newList.add(work);
            allWork.put(work.workType(), newList);
        }
    }
}
