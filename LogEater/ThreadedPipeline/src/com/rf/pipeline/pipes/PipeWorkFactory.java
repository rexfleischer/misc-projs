/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.pipes;

import com.rf.pipeline.PipelineWorkException;
import com.rf.pipeline.WorkIterator;

/**
 *
 * @author REx
 */
public interface PipeWorkFactory
{
    /**
     * this does the logically smallest amount of 
     * work before the thread should check status
     * and if there are any commands from the 
     * manager.
     * @return 
     */
    public WorkIterator getIterator() throws PipelineWorkException;
    
    /**
     * pauses work on this iterator
     * @param it
     * @throws WorkException 
     */
    public void pause(WorkIterator it) throws PipelineWorkException;
    
    /**
     * this tells the caller how many more times 
     * doWork must be called in order to have no
     * more work.
     * @return 
     */
    public int howMuchMore() throws PipelineWorkException;
    
    /**
     * this returns the work done since the last call
     */
    public int workDone() throws PipelineWorkException;
    public void incrementWork() throws PipelineWorkException;
    
    /**
     * this tells the caller what type of work 
     * this iterator represents. 
     * @return 
     */
    public String getContext();
    
    public String inputContext();
    
    public String[] outputContexts();
    
    /**
     * 
     * @param key
     * @param object
     * @return 
     */
    public void setObject(String key, Object object);
    
    public Object getObject(String key);
}
