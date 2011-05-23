/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics;

import com.rf.logs.metrics.collections.SerializingMetricCollection;
import com.rf.memory.manager.IPresistence;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author REx
 */
public interface MetricCollection 
{
    public enum MetricCollections
    {
        SERIALIZING()
        {
            public MetricCollection getMetricCollection()
            {
                return new SerializingMetricCollection();
            }
        };

        public abstract MetricCollection getMetricCollection();
    }

    public void init(IPresistence presistence, IPresistence query, int maxMetricChunk);

    public void add(Metric metric) throws IOException;

    public void commit() throws IOException;

    public void beginIteration()
            throws FileNotFoundException, IOException, ClassNotFoundException;

    public Metric next()
            throws FileNotFoundException, IOException, ClassNotFoundException;
}
