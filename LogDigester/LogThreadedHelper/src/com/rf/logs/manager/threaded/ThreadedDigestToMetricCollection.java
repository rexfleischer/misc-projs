/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.manager.threaded;

import com.rf.logs.digester.Digesters;
import com.rf.logs.digester.interfaces.IDigester;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class ThreadedDigestToMetricCollection
{
    protected class ThreadedDigest extends Thread
    {
        private IMetricCollection metricsDump;

        private IDigester digester;

        private IPersistence infoFrom;

        public ThreadedDigest(
                IMetricCollection metricsDump,
                IDigester digester,
                IPersistence infoFrom)
        {
            this.metricsDump = metricsDump;
            this.digester = digester;
            this.infoFrom = infoFrom;
        }

        @Override public void run()
        {
            try
            {
                while(true)
                {
                    Object chunk = infoFrom.next();
                    if (chunk == null)
                    {
                        break;
                    }
                    String data = (String) chunk;
                    digester.digest(metricsDump, data);
                }
            }
            catch(Exception ex)
            {
                System.err.println(ex.getMessage());
            }
        }
    }

    private IMetricCollection metricsDump;

    private Digesters digester;

    public ThreadedDigestToMetricCollection(
            IMetricCollection metricsDump, Digesters digester)
    {
        if (metricsDump == null)
        {
            throw new NullPointerException("metricsDump");
        }
        if (digester == null)
        {
            throw new NullPointerException("dumper");
        }
        this.metricsDump = metricsDump;
        this.digester = digester;
    }

    public void threadedDigest(IPersistence infoFrom, int numOfThreads)
    {
        if (infoFrom == null)
        {
            throw new NullPointerException("infoFrom");
        }
        if (numOfThreads < 1)
        {
            throw new IllegalArgumentException("numOfThreads");
        }

        // start all of the threads. they all get their own dumpers because
        // its not 'thread safe' at the moment. but the presistence layer
        // must be thread safe for a app like this to work
        ThreadedDigest[] threads = new ThreadedDigest[numOfThreads];
        for (int i = 0; i < numOfThreads; i++)
        {
            threads[i] = new ThreadedDigest(metricsDump, digester.getDigester(), infoFrom);
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
        try
        {
            metricsDump.commit();
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
}
