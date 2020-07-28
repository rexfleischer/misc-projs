/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline;

/**
 *
 * @author REx
 */
public interface Pipeline
{
    public void initThreads() throws PipelineException;
    
    public void stopThreads() throws PipelineException;
    
    public void setThreadConfig(String context, String message) throws PipelineException;
    
    public void setQueueObject(String context, Object object) throws PipelineException;
    
    public void setPipeObject(String context, String key, Object object) throws PipelineException;
    
    public void setGlobalObject(String key, Object object) throws PipelineException;
    
    public Object getPipeObject(String context, String key) throws PipelineException;
    
    public Object getGlobalObject(String key) throws PipelineException;
    
    public String getStatus();
}
