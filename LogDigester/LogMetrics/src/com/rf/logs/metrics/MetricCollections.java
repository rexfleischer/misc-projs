/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics;

import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.logs.metrics.collections.GenericMetricCollection;

/**
 *
 * @author REx
 */
public enum MetricCollections
{
    GENERIC()
    {
        public IMetricCollection getMetricCollection()
        {
            return new GenericMetricCollection();
        }
    },
    DEAMONED_SERVER()
    {
        public IMetricCollection getMetricCollection()
        {
            return new GenericMetricCollection();
        }
    };

    public abstract IMetricCollection getMetricCollection();
}
