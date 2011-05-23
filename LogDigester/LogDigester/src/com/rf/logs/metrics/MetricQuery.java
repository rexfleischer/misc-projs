/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics;

import com.rf.logs.metrics.queries.RequestCountQuery;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface MetricQuery
{
    public enum MetricQueries
    {
        REQUEST_COUNT()
        {
            public MetricQuery getMetricQuery()
            {
                return new RequestCountQuery();
            }
        };

        public abstract MetricQuery getMetricQuery();
    }

    public Map<String, Integer> doQuery(MetricCollection metrics)
            throws FileNotFoundException, IOException, ClassNotFoundException;
}
