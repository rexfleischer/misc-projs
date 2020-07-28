/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.consumer;

import com.rf.eater.thread.WorkerThreadException;

/**
 *
 * @author REx
 */
public interface WorkFactory
{
    /**
     * this does the logically smallest amount of 
     * work before the thread should check status
     * and if there are any commands from the 
     * manager.
     * @return 
     */
    public WorkIterator getIterator() throws WorkerThreadException;
    
    /**
     * this tells the caller how many more times 
     * doWork must be called in order to have no
     * more work.
     * @return 
     */
    public int howMuchMore();
    
    /**
     * this tells the caller what type of work 
     * this iterator represents. 
     * @return 
     */
    public String workType();
}
