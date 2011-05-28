/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics.collections;

import com.rf.logs.metrics.Metric;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public class DeamonedGenericMetricServer implements IMetricCollection
{
    protected class DeamonedIOGrabber extends Thread
    {
        @Override
        public void run()
        {
            while(true)
            {
                boolean needsSleep = true;
                synchronized(DeamonedGenericMetricServer.this.metricLock)
                {
                    if (metrics == null)
                    {
                        needsSleep = false;
                        try
                        {
                            metrics = DeamonedGenericMetricServer.this.getNextChunk();
                        }
                        catch (Exception ex)
                        {
                            System.err.println(ex.getMessage());
                        }
                        if (metrics == null)
                        {
                            return;
                        }
                    }
                }
                if (needsSleep)
                {
                    try
                    {
                        Thread.currentThread().wait(1);
                    }
                    catch (InterruptedException ex)
                    {
                        // keep going
                    }
                }
            }
        }
    }

    private DeamonedIOGrabber deamon;

    private final Object metricLock;

    private int iteratorChunkAt;

    private long iteratorTotal;

    private long iteratorLastNotice;

    private IPersistence presistence;

    private ArrayList<Metric> metrics;

    public DeamonedGenericMetricServer()
    {
        metrics = new ArrayList<Metric>();
        metricLock = new Object();
        deamon = null;
    }

    public void init(IPersistence presistence, int maxMetricChunk)
    {
        this.presistence = presistence;
    }

    public void add(Metric metric) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void commit() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Metric next()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void beginIteration()
    {
        iteratorChunkAt = 0;
        iteratorTotal = 0;
        metrics = null;
        deamon = new DeamonedIOGrabber();
        deamon.start();
    }

    public ArrayList<Metric> nextChunk()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        while(deamon.isAlive())
        {
            synchronized(metricLock)
            {
                if (metrics != null)
                {
                    ArrayList<Metric> result = metrics;
                    metrics = null;
                    return result;
                }
            }
            try
            {
                Thread.currentThread().wait(1);
            }
            catch (InterruptedException ex)
            {
                // do nothing
            }
        }
        return null;
    }

    private ArrayList<Metric> getNextChunk()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ArrayList<Metric> _metrics = null;
        while(_metrics == null)
        {
            if (iteratorChunkAt >= presistence.size())
            {
                return null;
            }
            Object result = presistence.getContent(iteratorChunkAt);
            iteratorChunkAt++;
            if (result == null)
            {
                return null;
            }
            _metrics = (ArrayList<Metric>) result;
            if (_metrics.isEmpty())
            {
                _metrics = null;
            }
        }
        iteratorTotal += _metrics.size();
        if (iteratorLastNotice + 10000 <= iteratorTotal)
        {
            System.out.println(
                    "metric collection iteration at " + iteratorTotal);
            iteratorLastNotice = iteratorTotal;
        }
        return _metrics;
    }

}
