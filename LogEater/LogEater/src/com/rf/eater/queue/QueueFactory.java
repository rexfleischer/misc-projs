/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.queue;

/**
 *
 * @author REx
 */
public enum QueueFactory
{
    IN_MEMORY
    {
        @Override
        public Queue getInstance()
        {
            return null; //new Queue_InMemory();
        }
    };
    
    public abstract Queue getInstance();
}
