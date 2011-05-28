/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.manager.threaded;

import com.rf.logs.query.MetricQueries;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.logs.query.interfaces.IMetricQuery;

/**
 *
 * @author REx
 */
public class ThreadedQueryFromMetricCollection
{
    protected class ThreadedQuery extends Thread
    {
        private IMetricCollection collection;

        private IMetricQuery querier;

        private Object result;

        public ThreadedQuery(IMetricCollection collection, IMetricQuery querier)
        {
            this.collection = collection;
            this.querier = querier;
        }

        @Override public void run()
        {
            try
            {
                querier.doQuery(collection);
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }

        public Object getResults()
        {
            return result;
        }
    }

    private MetricQueries query;

    public ThreadedQueryFromMetricCollection(MetricQueries query)
    {
        if (query == null)
        {
            throw new NullPointerException("dumper");
        }
        this.query = query;
    }

    public Object threadedQuery(IMetricCollection collection, int numOfThreads)
    {
        if (collection == null)
        {
            throw new NullPointerException("inputSet");
        }
        if (numOfThreads < 1)
        {
            throw new IllegalArgumentException("numOfThreads");
        }

        collection.beginIteration();
        IMetricQuery[] queries = new IMetricQuery[numOfThreads];
        ThreadedQuery[] threads = new ThreadedQuery[numOfThreads];
        for (int i = 0; i < numOfThreads; i++)
        {
            queries[i] = query.getMetricQuery();
            threads[i] = new ThreadedQuery(collection, queries[i]);
            threads[i].start();
        }

        // wait for all the treads to finish
        for (int i = 0; i < numOfThreads; i++)
        {
            try
            {
                threads[i].join();
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        for (int i = 1; i < numOfThreads; i++)
        {
            queries[0].collide(queries[i].getResult());
            queries[i] = null;
            System.gc();
        }
        
        return queries[0].getResult();
    }
}
