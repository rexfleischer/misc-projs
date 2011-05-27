/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics;

import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.logs.metrics.collections.SerializingMetricCollection;

/**
 *
 * @author REx
 */
public enum MetricCollections
{
    SERIALIZING()
    {
        public IMetricCollection getMetricCollection()
        {
            return new SerializingMetricCollection();
        }
    };

    public abstract IMetricCollection getMetricCollection();
}
