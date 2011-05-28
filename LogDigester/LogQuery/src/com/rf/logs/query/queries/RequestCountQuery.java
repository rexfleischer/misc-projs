/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.query.queries;

import com.rf.logs.metrics.Metric;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.logs.query.interfaces.IMetricQuery;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author REx
 */
public class RequestCountQuery implements IMetricQuery
{
    Map<String, Integer> results;

    public void doQuery(IMetricCollection metrics)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        results = new HashMap<String, Integer>();
        metrics.beginIteration();
        while(true)
        {
//            ArrayList<Metric> metricList = metrics.nextChunk();
//            if (metricList == null)
//            {
//                break;
//            }
//            Iterator<Metric> it = metricList.iterator();
//            while(it.hasNext())
//            {
//                Metric metric = it.next();
//                if (results.containsKey(metric.request))
//                {
//                    results.put(metric.request, results.get(metric.request) + 1);
//                }
//                else
//                {
//                    results.put(metric.request, 1);
//                }
//            }
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
    }

    public void collide(Object result)
    {
        try
        {
            Map<String, Integer> thisResult = (Map<String, Integer>) result;
            Iterator<String> it = thisResult.keySet().iterator();
            while(it.hasNext())
            {
                String key = it.next();
                if (results.containsKey(key))
                {
                    results.put(key, results.get(key) + thisResult.get(key));
                }
                else
                {
                    results.put(key, thisResult.get(key));
                }
            }
            thisResult = null;
            System.gc();
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    public Object getResult()
    {
        return results;
    }
}
