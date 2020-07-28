
package com.rf.eater.thread;

import com.rf.eater.consumer.WorkIterator;

/**
 *
 * @author REx
 */
public class WorkerThread extends Thread
{
    private final Object WORK_LOCK;
    
    private final Object FLAG_LOCK;
    
    private boolean keepWorking;
    
    private boolean stop;
    
    private long waitTime;
    
    private ThreadManager manager;
    
    private WorkerThreadStatus status;
    
    private WorkIterator work;
    
    private Exception error;
    
    protected WorkerThread(ThreadManager manager, long waitTime)
    {
        this.FLAG_LOCK  = new Object();
        this.WORK_LOCK  = new Object();
        this.keepWorking= true;
        this.stop       = false;
        this.work       = null;
        this.manager    = manager;
        this.waitTime   = waitTime;
        this.status     = WorkerThreadStatus.UNINITIALIZED;
    }
    
    public void flagToReturnWork()
    {
        synchronized(FLAG_LOCK)
        {
            keepWorking = false;
        }
    }
    
    public void flagToTurnOff()
    {
        synchronized(FLAG_LOCK)
        {
            keepWorking = false;
            stop = true;
        }
    }
    
    public WorkerThreadStatus getStatus()
    {
        WorkerThreadStatus result = null;
        synchronized(FLAG_LOCK)
        {
            result = status;
        }
        return result;
    }
    
    public Exception getError()
    {
        return error;
    }
    
    public void setWork(WorkIterator work)
            throws WorkerThreadException
    {
        synchronized(FLAG_LOCK)
        {
            if (this.stop)
            {
                throw new WorkerThreadException(
                        "cannot set work when stop flag is set to true");
            }
            if (this.work != null)
            {
                throw new WorkerThreadException(
                        "cannot set work when there is already work being done");
            }
            if (status != WorkerThreadStatus.WAITING)
            {
                throw new WorkerThreadException(
                        "cannot set work unless the thread is waiting for work");
            }
            synchronized(WORK_LOCK)
            {
                this.work = work;
            }
            status = WorkerThreadStatus.WORKING;
        }
    }
    
    @Override
    public void run()
    {
        boolean needsWait = false;
        boolean needsStateSwitch = false;
        while(true)
        {
            synchronized(FLAG_LOCK)
            {
                if (status == WorkerThreadStatus.ERROR)
                {
                    try
                    {
                        Thread.sleep(waitTime);
                    }
                    catch (InterruptedException ex) { }
                    continue;
                }
                if (!keepWorking)
                {
                    synchronized(WORK_LOCK)
                    {
                        if (work != null)
                        {
                            manager.setWork(work);
                            work = null;
                        }
                    }
                    status = WorkerThreadStatus.WAITING;
                }
                if (stop)
                {
                    break;
                }
            }
            
            synchronized(WORK_LOCK)
            {
                if (work == null)
                {
                    needsWait = true;
                }
                else
                {
                    try
                    {
                        if (!work.doWork())
                        {
                            needsStateSwitch = true;
                        }
                    }
                    catch (WorkerThreadException ex)
                    {
                        error = ex;
                        status = WorkerThreadStatus.ERROR;
                    }
                }
            }
            
            if (needsStateSwitch)
            {
                needsStateSwitch = false;
                synchronized(FLAG_LOCK)
                {
                    status = WorkerThreadStatus.WAITING;
                }
            }
            
            if (needsWait)
            {
                needsWait = false;
                try
                {
                    Thread.sleep(waitTime);
                }
                catch (InterruptedException ex) {/* just ignore these */}
            }
        }
        status = WorkerThreadStatus.STOPPED;
    }
}
