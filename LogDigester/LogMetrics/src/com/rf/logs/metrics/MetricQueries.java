/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics;

import com.rf.logs.metrics.interfaces.IMetricQuery;
import com.rf.logs.metrics.queries.RequestCountQuery;

/**
 *
 * @author REx
 */
public enum MetricQueries
{
    REQUEST_COUNT()
    {
        public IMetricQuery getMetricQuery()
        {
            return new RequestCountQuery();
        }
    };

    public abstract IMetricQuery getMetricQuery();
}
