/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.impls;

import com.rf.pipeline.Pipeline;
import com.rf.pipeline.PipelineException;
import com.rf.pipeline.PipelineWorkException;
import com.rf.pipeline.config.PipeDefinition;
import com.rf.pipeline.config.QueueDefinition;
import com.rf.pipeline.pipes.PipeWorkFactory;
import com.rf.pipeline.pipes.PipeWorkFactory_1i1o;
import com.rf.pipeline.queue.WorkQueue;
import com.rf.pipeline.queue.WorkQueueFactory;
import com.rf.pipeline.queue.WorkQueueInterface;
import com.rf.pipeline.threads.ThreadManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class PipelineMap implements Pipeline
{
    private Map<String, PipeWorkFactory> pipes;
    
    private Map<String, WorkQueue> queues;
    
    private Map<String, Object> globals;
    
    private Map<String, String> threadConfig;
    
    private ThreadManager threads;

    public PipelineMap(LinkedList<PipeDefinition> pipes, 
                       LinkedList<QueueDefinition> queues)
            throws PipelineException
    {
        this.globals        = new HashMap<String, Object>();
        this.pipes          = new HashMap<String, PipeWorkFactory>();
        this.queues         = new HashMap<String, WorkQueue>();
        this.threadConfig   = new HashMap<String, String>();
        
        for(QueueDefinition definition : queues)
        {
            WorkQueueInterface type = WorkQueueInterface.valueOf(
                    definition.queue_interface.toUpperCase());
            WorkQueueFactory factory = WorkQueueFactory.valueOf(
                    definition.queue_type.toUpperCase());
            WorkQueue queue = factory.getInstance(type);
            
            this.queues.put(definition.context, queue);
        }
        
        List<String> usedAsInput = new LinkedList<String>(this.queues.keySet());
        for(PipeDefinition definition : pipes)
        {
            if (definition.input.isEmpty() && definition.outputs.isEmpty())
            {
                throw new PipelineException(
                        "there is no input or output for pipe '" 
                        + definition.context + "'");
            }
            
            /**
             * setup the input queue
             */
            WorkQueue input = null;
            if (!definition.input.isEmpty())
            {
                input = this.queues.get(definition.input);
                if (input == null)
                {
                    throw new PipelineException(
                            definition.input + " is not a defined queue");
                }
                Object checker = usedAsInput.remove(definition.input);
                if (checker == null)
                {
                    throw new PipelineException(
                            definition.input + " was used twice as input: "
                            + "work queues must be used as input for "
                            + "one work factory");
                }
            }
            
            
            /**
             * put together the outputs of the work object
             */
            Map<String, WorkQueue> outputs = new HashMap<String, WorkQueue>();
            for(String key : definition.outputs.keySet())
            {
                String queue = definition.outputs.get(key);
                WorkQueue instance = this.queues.get(queue);
                if (instance == null)
                {
                    throw new PipelineException(
                            queue + " is an unknown queue trying to "
                            + "be added to pipe '" + definition.context + "'");
                }
                outputs.put(key, instance);
            }
            
            /**
             * now, we have to put it all together
             */
            PipeWorkFactory factory = null;
            try
            {
                factory = new PipeWorkFactory_1i1o(
                        definition.clazz, 
                        definition.context, 
                        input, 
                        definition.input,
                        outputs, 
                        definition.outputs,
                        globals);
            }
            catch (Exception ex)
            {
                throw new PipelineException(
                        "unable to initialize pipe '" + definition.context + "'", ex);
            }
            
            this.pipes.put(factory.getContext(), factory);
        }
        
        if (!usedAsInput.isEmpty())
        {
            throw new PipelineException("not all defined queues were used: " 
                    + usedAsInput.toString());
        }
        
    }
    
    @Override
    public void setGlobalObject(String key, Object object)
    {
        globals.put(key, object);
    }

    @Override
    public Object getGlobalObject(String key)
    {
        return globals.get(key);
    }

    @Override
    public void setQueueObject(String context, Object object) throws PipelineException
    {
        WorkQueue queue = this.queues.get(context);
        if (queue == null)
        {
            throw new PipelineException("unknown context for queue: " + context);
        }
        queue.push(object);
    }

    @Override
    public void setPipeObject(String context, String key, Object object) throws PipelineException
    {
        PipeWorkFactory factory = this.pipes.get(context);
        if (factory == null)
        {
            throw new PipelineException("unknown context for pipe: " + context);
        }
        factory.setObject(key, object);
    }

    @Override
    public Object getPipeObject(String context, String key) throws PipelineException
    {
        PipeWorkFactory factory = this.pipes.get(context);
        if (factory == null)
        {
            throw new PipelineException("unknown context for pipe: " + context);
        }
        return factory.getObject(key);
    }

    @Override
    public String getStatus()
    {
        return "something placed here.... basically, this isnt implemented yet";
    }

    @Override
    public void initThreads() throws PipelineException
    {
        if (threads != null)
        {
            throw new PipelineException("initThreads was already called");
        }
        try
        {
            Integer thread_wait_time = Integer.parseInt(threadConfig.get("thread_wait_time"));
            if (thread_wait_time == null)
            {
                thread_wait_time = 5;
            }
            
            Double memory_target = Double.parseDouble(threadConfig.get("memory_target"));
            if (memory_target == null)
            {
                memory_target = 0.5;
            }
            
            Double cpu_target = Double.parseDouble(threadConfig.get("cpu_target"));
            if (cpu_target == null)
            {
                cpu_target = 0.5;
            }
            
            Integer max_threads = Integer.parseInt(threadConfig.get("max_threads"));
            if (max_threads == null)
            {
                thread_wait_time = 3;
            }
            
            threads = new ThreadManager(
                    thread_wait_time, 
                    memory_target, 
                    cpu_target,
                    max_threads, 
                    pipes);
            
            
            Boolean separate_manager_thread = Boolean.parseBoolean(threadConfig.get("separate_manager_thread"));
            if (separate_manager_thread == null)
            {
                separate_manager_thread = false;
            }
            
            Boolean returns_when_no_work = Boolean.parseBoolean(threadConfig.get("returns_when_no_work"));
            if (returns_when_no_work == null)
            {
                returns_when_no_work = false;
            }
            
            Integer manager_thread_wait_time = Integer.parseInt(threadConfig.get("manager_thread_wait_time"));
            if (manager_thread_wait_time == null)
            {
                manager_thread_wait_time = 3;
            }
            
            threads.start(
                    separate_manager_thread, 
                    returns_when_no_work, 
                    manager_thread_wait_time);
        }
        catch (PipelineWorkException ex)
        {
            throw new PipelineException("could not initalize threads", ex);
        }
    }

    @Override
    public void stopThreads() throws PipelineException
    {
        try
        {
            threads.stop();
        }
        catch (InterruptedException ex)
        {
            throw new PipelineException("error while stopping threads: "
                    + "suggested to restart the system due to potential memory "
                    + "leak", ex);
        }
    }

    @Override
    public void setThreadConfig(String context, String message) throws PipelineException
    {
        threadConfig.put(context, message);
    }
    
    /**
     * for testing
     * i know... i know... its hacky
     * @return 
     */
    protected Map<String, PipeWorkFactory> getPipes()
    {
        return pipes;
    }
}
