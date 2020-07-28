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
public interface WorkIterator
{
    /**
     * this does the logically smallest amount of 
     * work before the thread should check status
     * and if there are any commands from the 
     * manager.
     * @return 
     */
    public boolean doWork() throws WorkerThreadException;
    
    /**
     * this tells the caller what type of work 
     * this iterator represents. 
     * @return 
     */
    public String workType();
    
    /**
     * this tells the iterator that the current thread is either
     * about to change jobs or give this iterator to another thread.
     */
    public void close() throws WorkerThreadException;
}
