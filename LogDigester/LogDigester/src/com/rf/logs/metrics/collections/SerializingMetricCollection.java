/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics.collections;

import com.rf.logs.metrics.Metric;
import com.rf.logs.metrics.MetricCollection;
import com.rf.memory.manager.IPresistence;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author REx
 */
public class SerializingMetricCollection implements MetricCollection
{
    private final Object metricLock;

    private int maxMetricChunk;

    private int metricChunkAt;

    private int iteratorPosition;

    private int iteratorChunkAt;

    private IPresistence presistence;

    private ArrayList<Metric> metrics;

    public SerializingMetricCollection()
    {
        metrics = new ArrayList<Metric>();
        metricLock = new Object();
    }

    public void init(
            IPresistence presistence,
            IPresistence query,
            int maxMetricChunk)
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
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        iteratorPosition = 0;
        iteratorChunkAt = 0;
        metrics = new ArrayList<Metric>();
    }

    public synchronized Metric next()
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Metric metric = null;
        if (iteratorPosition >= metrics.size())
        {
            if (iteratorChunkAt >= presistence.size())
            {
                return null;
            }
            metrics = (ArrayList<Metric>) presistence.getContent(iteratorChunkAt);
            metric = metrics.get(0);
            iteratorPosition = 1;
            iteratorChunkAt++;
        }
        else
        {
            metric = metrics.get(iteratorPosition);
            iteratorPosition++;
        }
        return metric;
    }

}
