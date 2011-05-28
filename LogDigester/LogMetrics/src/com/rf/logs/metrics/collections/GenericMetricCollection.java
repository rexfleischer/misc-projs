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
public class GenericMetricCollection implements IMetricCollection
{
    private final Object metricLock;

    private int maxMetricChunk;

    private int metricChunkAt;

    private int iteratorPosition;

    private int iteratorChunkAt;

    private long iteratorTotal;

    private long iteratorLastNotice;

    private IPersistence presistence;

    private ArrayList<Metric> metrics;

    public GenericMetricCollection()
    {
        metrics = new ArrayList<Metric>();
        metricLock = new Object();
    }

    public void init(IPersistence presistence, int maxMetricChunk)
    {
        this.presistence = presistence;
        this.maxMetricChunk = maxMetricChunk;
        metricChunkAt = 0;
    }

    public void add(Metric metric) throws IOException
    {
        Object committed = null;
        synchronized(metricLock)
        {
            metrics.add(metric);
            metricChunkAt++;
            if (maxMetricChunk <= metricChunkAt)
            {
                // this may look weird at first, but it is to
                // make sure that while information is being committed
                // to the presistance layer, that other threads can
                // still add data without waiting for a potentially
                // slow io or database crap.
                committed = metrics;
                metrics = new ArrayList<Metric>();
                metricChunkAt = 0;
                System.out.println("added metric chunk to presistence layer");
            }
        }
        if (committed != null)
        {
            presistence.pushContent(committed);
        }
    }

    public void commit() throws IOException
    {
        Object committed = null;
        synchronized(metricLock)
        {
            committed = metrics;
            metrics = new ArrayList<Metric>();
            metricChunkAt = 0;
        }
        presistence.pushContent(committed);
    }

    public void beginIteration()
    {
        iteratorPosition = 0;
        iteratorChunkAt = 0;
        iteratorTotal = 0;
        metrics = null;
    }

    public Metric next()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Metric metric = null;
        iteratorTotal++;
        if (iteratorTotal % 10000 == 0)
        {
            System.out.println("total iterations at " + iteratorTotal);
        }
        synchronized(metricLock)
        {
            if (metrics == null)
            {
                metrics = getNextChunk();
                iteratorPosition = 0;
            }
            if (iteratorPosition >= metrics.size())
            {
                if (iteratorChunkAt >= presistence.size())
                {
                    return null;
                }
                metrics = getNextChunk();
                if (metrics == null)
                {
                    return null;
                }
                metric = metrics.get(0);
                iteratorPosition = 1;
            }
            else
            {
                metric = metrics.get(iteratorPosition);
                iteratorPosition++;
            }
        }
        return metric;
    }

    public ArrayList<Metric> nextChunk()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ArrayList<Metric> result = null;
        synchronized(metricLock)
        {
            if (iteratorChunkAt >= presistence.size())
            {
                return null;
            }
            if (metrics == null)
            {
                result = getNextChunk();
                if (result == null)
                {
                    return null;
                }
                iteratorTotal += result.size();
            }
            else
            {
                iteratorTotal += metrics.size() - iteratorPosition;
                result = metrics;
                metrics = null;
            }
            if (iteratorLastNotice <= iteratorTotal + 10000)
            {
                System.out.println("total iterations at " + iteratorTotal);
            }
        }
        return result;
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
        return _metrics;
    }

}
