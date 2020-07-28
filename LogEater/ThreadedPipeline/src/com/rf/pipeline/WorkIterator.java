/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline;

import com.rf.pipeline.pipes.PipeWorkFactory;
import com.rf.pipeline.queue.WorkQueue;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class WorkIterator
{
    private WorkQueue input;
    
    private Map<String, WorkQueue> outputs;
    
    private Map<String, Object> routed;
    
    private Map<String, Object> globals;
    
    private int id = -1;
    
    private PipeWorkFactory manager;
    
    public final void setup(int id, 
                            PipeWorkFactory manager,
                            WorkQueue input, 
                            Map<String, WorkQueue> outputs,
                            Map<String, Object> routed,
                            Map<String, Object> globals)
    {
        if (this.id != -1)
        {
            throw new IllegalStateException("cannot call setup twice");
        }
        if (id < 1)
        {
            throw new IllegalArgumentException("id cannot be less than 1");
        }
        if (manager == null)
        {
            throw new NullPointerException("manager");
        }
        this.id         = id;
        this.input      = input;
        this.outputs    = outputs;
        this.routed     = routed;
        this.globals    = globals;
        this.manager    = manager;
    }
    
    public final int getId()
    {
        return id;
    }
    
    public final String getContext()
    {
        return manager.getContext();
    }
    
    public final WorkQueue getInput()
    {
        return input;
    }
    
    public final WorkQueue getOutput(String key)
    {
        return outputs.get(key);
    }
    
    public final boolean doWork() throws PipelineWorkException
    {
        manager.incrementWork();
        return doWork(input, outputs, routed, globals);
    }
    
    public final int estimateWorkLeft() throws PipelineWorkException
    {
        return estimateWorkLeft(routed, globals);
    }
    
    /**
     * 
     * @param properties
     * @param context
     * @throws WorkException 
     */
    public abstract void init(String context) 
            throws PipelineWorkException;
    
    /**
     * when there is no input WorkQueue, then it is up to the developer 
     * to estimate how much work is left. note: the thread manager that
     * balances the threads will only be calling one instance of this class,
     * so the implementation must estimate all the work that is left.
     * @return the estimated amount of times doWork() will have to be called
     * for doWork to start return false.
     * @throws PipelineWorkException 
     */
    protected abstract int estimateWorkLeft(Map<String, Object> routed, 
                                            Map<String, Object> global) 
            throws PipelineWorkException;
    
    /**
     * this does the logically smallest amount of work before the thread 
     * should check status and if there are any commands from the manager.
     * @return 
     */
    protected abstract boolean doWork(WorkQueue input, 
                                      Map<String, WorkQueue> outputs, 
                                      Map<String, Object> routed, 
                                      Map<String, Object> global) 
            throws PipelineWorkException;
    
    /**
     * this tells the iterator that the current thread is either
     * about to change jobs or give this iterator to another thread.
     */
    public abstract void close() 
            throws PipelineWorkException;
}
