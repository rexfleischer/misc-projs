/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.threads;

import com.rf.pipeline.PipelineWorkException;
import com.rf.pipeline.WorkIterator;
import com.rf.pipeline.pipes.PipeWorkFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * this class will balance the process of consuming threads and make threads
 * that have finished their work start other tasks.
 * @author REx
 */
public class ThreadManager
{
    protected class WorkPipelinePart implements Comparable<WorkPipelinePart>
    {
        int workDone;
        int amountLeft;
        List<WorkerThread> threads;
        WorkPipelinePart input;
        WorkPipelinePart[] outputs;
        PipeWorkFactory factory;

        @Override
        public int compareTo(WorkPipelinePart o)
        {
            return o.amountLeft - amountLeft;
        }
    }
    
    private Map<String, WorkPipelinePart> pipeline;
    
    private WorkerThread[] threads;
    
    private int threadsUsed;
    
    private int threadWaitTime;
    
    private double memoryTarget;
    
    private double cpuTarget;
    
    public ThreadManager(int threadWaitTime, 
                         double memoryTarget,
                         double cpuTarget,
                         int maxThreads, 
                         Map<String, PipeWorkFactory> factories) 
            throws  PipelineWorkException
    {
        this.threads        = new WorkerThread[maxThreads];
        this.threadsUsed    = 0;
        this.threadWaitTime = threadWaitTime;
        this.memoryTarget   = memoryTarget;
        this.cpuTarget      = cpuTarget;
        this.pipeline       = new HashMap<String, WorkPipelinePart>();
        
        /**
         * the basic idea here is to connect all the factories logically
         * so when we analyze the status of the work factories we can
         * easily look make decisions based on how the factories are connected
         * 
         * but this needs to happen in two steps, first we need to initialize
         * all of them, then we link them together
         */
        {
            Iterator<String> it = factories.keySet().iterator();
            while(it.hasNext())
            {
                String context = it.next();
                WorkPipelinePart part = new WorkPipelinePart();
                part.factory    = factories.get(context);
                part.amountLeft = part.factory.howMuchMore();
                part.threads    = new LinkedList<WorkerThread>();


                pipeline.put(context, part);
            }
        }
        
        {
            Iterator<String> it = pipeline.keySet().iterator();
            while(it.hasNext())
            {
                String context = it.next();
                WorkPipelinePart part = pipeline.get(context);
                
                /**
                 * link outputs
                 */
                String[] contexts = part.factory.outputContexts();
                part.outputs    = new WorkPipelinePart[contexts.length];
                for(int i = 0; i < contexts.length; i++)
                {
                    part.outputs[i] = pipeline.get(contexts[i]);
                }
                
                /**
                 * link input
                 */
                String input = part.factory.inputContext();
                if (!input.isEmpty())
                {
                    part.input = pipeline.get(part.factory.inputContext());
                }
            }
        }
    }
    
    /**
     * 
     * @param separate this tells the manager if it should run on a separate
     * thread or if the calling thread will be the manager thread.
     * @param returns this tells the manager to return when no work is left,
     * else it will wait for work
     */
    public void start(boolean separate, boolean returns, int waitTime) 
            throws PipelineWorkException
    {
        while(true)
        {
            try
            {
                if (!update())
                {
                    if (returns)
                    {
                        break;
                    }
                }
                Thread.sleep(waitTime);
            }
            catch (InterruptedException ex) 
            { 
                System.err.println("iterrupted during operation: " + ex.getMessage());
            }
        }
    }
    
    public void stop() throws InterruptedException
    {
        /**
         * signal all of the threads to shut off
         */
        for(WorkerThread thread : threads)
        {
            if (thread != null)
            {
                thread.flagToTurnOff();
            }
        }
        
        /**
         * now do a join on them, and if they
         */
        for(WorkerThread thread : threads)
        {
            if (thread != null)
            {
                thread.interrupt();
                try
                {
                    thread.join();
                }
                catch(InterruptedException ex)
                {
                    // @TODO: handle this
                }
            }
        }
        
        /**
         * now we need to set the thread objects for the gc
         */
        for(int i = 0; i < threads.length; i++)
        {
            threads[i] = null;
        }
        threadsUsed = 0;
    }
    
    public void returnWork(WorkIterator work) throws PipelineWorkException
    {
        WorkPipelinePart part = pipeline.get(work.getContext());
        part.factory.pause(work);
    }
    
    protected Map<String, WorkPipelinePart> getLogicalPipeline()
    {
        return pipeline;
    }
    
    private WorkerThread createNewThread()
    {
        if (threadsUsed == threads.length)
        {
            return null;
        }
        WorkerThread result = new WorkerThread(this, threadWaitTime);
        threads[threadsUsed++] = result;
        return result;
    }
    
    private boolean removeThread(WorkerThread thread)
    {
        thread.flagToTurnOff();
        try
        {
            thread.join();
        }
        catch (InterruptedException ex)
        {
            System.err.println(ex.getMessage());
        }
        for(int i = 0; i < threads.length; i++)
        {
            if (threads[i].getId() == thread.getId())
            {
                if (i + 1 == threads.length)
                {
                    threads[i] = null;
                }
                else
                {
                    System.arraycopy(threads, i + 1, threads, i, threads.length - i);
                }
                threadsUsed--;
                return true;
            }
        }
        return false;
    }
    
    private double getMemoryPercentage()
    {
        Runtime runtime = Runtime.getRuntime();
        double max = (double) runtime.maxMemory();
        return (max - ((double) runtime.freeMemory())) / max;
    }
    
    private double getCpuPercentage()
    {
        /**
         * i havent found a way to do this that i am willing 
         * to work with, so i will just say that the cpu is
         * always on target.
         */
        return cpuTarget;
    }
    
    
    
    private boolean update() throws PipelineWorkException
    {
        boolean workLeft = false;
        /**
         * basically, we find memory and cpu to try to help us make
         * some decision when the system is under very heavy or light 
         * load. if the memory is high, then it will know to take 
         * threads off the source (starting) work factories and put them
         * where work (memory) is building up the most. if memory is usage 
         * is low, then the manager will try to take threads off the ending
         * (persistence) work factories and place them in places that create
         * work. 
         */
        
        
        /**
         * step 1:
         * get metrics. this includes memory and cpu usage and estimated 
         * amount of work left from current running objects.
         */
        // not implemented yet.
//        double memory = getMemoryPercentage();
//        double cpu = getCpuPercentage();
        
        Collection<WorkPipelinePart> parts = pipeline.values();
        for(WorkPipelinePart part : parts)
        {
            part.amountLeft = part.factory.howMuchMore();
            part.workDone = part.factory.workDone();
        }
        
        /**
         * now, we need to order them based on the 'amountLeft'
         */
        WorkPipelinePart[] partsArray = new WorkPipelinePart[parts.size()];
        partsArray = parts.toArray(partsArray);
        
        Arrays.sort(partsArray);
        
        /**
         * basically, if there are a few situations:
         * 1 - there is work to be done and and the manager is not at max
         *     threads.. this means the thread manager will create a new
         *     thread and make the thread do that work
         * 2 - there is no more work to be done on an iterator and the thread
         *     is waiting on the work object. this means the thread will be
         *     taken off the work object and either put onto another work
         *     object or stopped
         * 3 - there is important work to be done and the manager is at max
         *     threads meaning the manager will move threads from the lowest
         *     priority and putting them where needed.
         */
        
        // this is where we will put threads that are taken off of work 
        // objects but aren't getting turned off. we dont want to turn them
        // off if they are just going to be reassigned.
        LinkedList<WorkerThread> pausedThreads = new LinkedList<WorkerThread>();
        
        // first, lets see if there are threads on work objects that 
        // have no work.
        for(int i = partsArray.length - 1; 
            i >= 0 && (partsArray[i].amountLeft == 0); 
            i--)
        {
            if (!partsArray[i].threads.isEmpty())
            {
                Iterator<WorkerThread> it = partsArray[i].threads.iterator();
                while(it.hasNext())
                {
                    WorkerThread thread = it.next();
                    if (thread.getStatus() == WorkerThreadStatus.WAITING)
                    {
                        pausedThreads.add(thread);
                        it.remove();
                    }
                }
            }
        }
        
        for(int i = 0; i < partsArray.length; i++)
        {
            if (partsArray[i].amountLeft > 0)
            {
                workLeft = true;
            }
            if (partsArray[i].amountLeft > 0 && partsArray[i].threads.isEmpty())
            {
                WorkIterator iterator = partsArray[i].factory.getIterator();
                WorkerThread worker = null;
                if (pausedThreads.isEmpty())
                {
                    if (threadsUsed < threads.length)
                    {
                        worker = createNewThread();
                        worker.start();
                    }
                }
                else
                {
                    worker = pausedThreads.remove();
                }
                if (worker == null)
                {
                    iterator.close();
                    continue;
                }
                try
                {
                    worker.setWork(iterator);
                }
                catch (WorkerThreadException ex)
                {
                    iterator.close();
                    removeThread(worker);
                    System.err.println(ex.getMessage());
                    continue;
                }
                partsArray[i].threads.add(worker);
            }
        }
        
        /**
         * make the unused threads return
         */
        for(WorkerThread thread : pausedThreads)
        {
            thread.flagToTurnOff();
            try
            {
                thread.join();
            }
            catch (InterruptedException ex)
            {
                System.err.println(ex.getMessage());
            }
            
        }
        
        return workLeft;
    }
}
