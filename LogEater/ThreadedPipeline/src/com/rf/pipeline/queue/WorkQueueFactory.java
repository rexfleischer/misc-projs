/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.queue;

import java.util.Map;

/**
 *
 * @author REx
 */
public enum WorkQueueFactory
{
    IN_MEMORY
    {
        @Override
        public WorkQueue getInstance(WorkQueueInterface type)
        {
            switch(type)
            {
                case OBJECT:
                    return new WorkQueue_InMemory();
                    
                case STRING:
                    return new WorkQueue_InMemory<String>();
                    
                case STRING_MAP:
                    return new WorkQueue_InMemory<Map<String, String>>();
                    
                case OBJECT_MAP:
                    return new WorkQueue_InMemory<Map<String, Object>>();
                    
                default:
                    throw new IllegalArgumentException(
                            "type is not a valid value");
            }
        }
    };
    
    public abstract WorkQueue getInstance(WorkQueueInterface type);
}
