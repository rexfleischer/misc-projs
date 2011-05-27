/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.manager.threaded;

import com.rf.memory.persistence.Dumpers;
import com.rf.memory.persistence.LimitOutputBuffers;
import com.rf.memory.persistence.interfaces.IDumper;
import com.rf.memory.persistence.interfaces.IInputStream;
import com.rf.memory.persistence.interfaces.IInputStreamSet;
import com.rf.memory.persistence.interfaces.IPersistence;

/**
 *
 * @author REx
 */
public class ThreadedDumpToPresistence
{
    protected class ThreadedDumper extends Thread
    {
        private IInputStreamSet inputSet;

        private IDumper dumper;

        public ThreadedDumper(
                IInputStreamSet inputSet,
                IDumper dumper)
        {
            this.inputSet = inputSet;
            this.dumper = dumper;
        }

        @Override public void run()
        {
            while(true)
            {
                IInputStream input = inputSet.next();
                if (input == null)
                {
                    break;
                }
                dumper.dump(input);
            }
        }
    }

    private Dumpers dumper;
    private IPersistence workingDir;
    private LimitOutputBuffers buffer;
    private int maxKBBuffer;

    public ThreadedDumpToPresistence(
            Dumpers dumper,
            IPersistence workingDir,
            LimitOutputBuffers buffer,
            int maxKBBuffer)
    {
        if (dumper == null)
        {
            throw new NullPointerException("dumper");
        }
        this.dumper = dumper;
        this.workingDir = workingDir;
        this.buffer = buffer;
        this.maxKBBuffer = maxKBBuffer;
    }

    public void threadedDump(IInputStreamSet inputSet, int numOfThreads)
    {
        if (inputSet == null)
        {
            throw new NullPointerException("inputSet");
        }
        if (numOfThreads < 1)
        {
            throw new IllegalArgumentException("numOfThreads");
        }

        // start all of the threads. they all get their own dumpers because
        // its not 'thread safe' at the moment. but the presistence layer
        // must be thread safe for a app like this to work
        ThreadedDumper[] threads = new ThreadedDumper[numOfThreads];
        for (int i = 0; i < numOfThreads; i++)
        {
            threads[i] = new ThreadedDumper(
                    inputSet,
                    dumper.getDumper(
                        workingDir,
                        buffer.getOutputBuffer(maxKBBuffer)));
            threads[i].start();
        }

        // wait for all the treads to finish
        for (int i = 0; i < numOfThreads; i++)
        {
            try
            {
                threads[i].join();
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
}
