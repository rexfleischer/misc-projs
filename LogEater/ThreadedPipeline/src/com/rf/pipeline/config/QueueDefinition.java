/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.config;

/**
 *
 * @author REx
 */
public class QueueDefinition
{
    public final String context;
    
    public final String queue_type;
    
    public final String queue_interface;
    
    public QueueDefinition(String context,
                           String queue_type,
                           String queue_interface)
    {
        this.context            = context;
        this.queue_type         = queue_type;
        this.queue_interface    = queue_interface;
    }
}
