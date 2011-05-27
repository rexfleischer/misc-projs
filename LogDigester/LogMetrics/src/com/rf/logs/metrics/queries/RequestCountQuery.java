/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.metrics.queries;

import com.rf.logs.metrics.Metric;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.logs.metrics.interfaces.IMetricQuery;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class RequestCountQuery implements IMetricQuery
{

    public Map<String, Integer> doQuery(IMetricCollection metrics)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Map<String, Integer> results = new HashMap<String, Integer>();
        metrics.beginIteration();
        while(true)
        {
            Metric metric = metrics.next();
            if (metric == null)
            {
                break;
            }
            if (results.containsKey(metric.request))
            {
                results.put(metric.request, results.get(metric.request) + 1);
            }
            else
            {
                results.put(metric.request, 1);
            }
        }
        return results;
    }
    
}
