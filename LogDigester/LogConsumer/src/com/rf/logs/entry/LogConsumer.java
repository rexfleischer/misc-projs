/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.entry;

import com.rf.logs.digester.Digesters;
import com.rf.logs.manager.threaded.ThreadedDigestToMetricCollection;
import com.rf.logs.manager.threaded.ThreadedDumpToPresistence;
import com.rf.logs.manager.threaded.ThreadedQueryFromMetricCollection;
import com.rf.logs.metrics.MetricCollections;
import com.rf.logs.metrics.MetricQueries;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.memory.persistence.Dumpers;
import com.rf.memory.persistence.InputStreamSets;
import com.rf.memory.persistence.LimitOutputBuffers;
import com.rf.memory.persistence.Persistences;
import com.rf.memory.persistence.interfaces.IInputStreamSet;
import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class LogConsumer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
            throws FileNotFoundException, IOException, InterruptedException, ClassNotFoundException
    {
        
        IPersistence workingDir = Persistences.TXTFILE.getPersistence(
                "C:/Users/REx/Desktop/workingdir",
                "expanded_");
        System.out.println("query on expanded presistence");
        System.out.println("total bytes: " + workingDir.totalBytes());
        System.out.println("size: " + workingDir.size());



        IPersistence realizedDir = Persistences.SERIALIZINGFILE.getPersistence(
                "C:/Users/REx/Desktop/realizeddir",
                "metrics_");
        System.out.println("query on metrics presistence");
        System.out.println("total bytes: " + realizedDir.totalBytes());
        System.out.println("size: " + realizedDir.size());


        if (workingDir.size() == 0)
        {
            IInputStreamSet targetDir = InputStreamSets.FullDirectoryGrab_GZipFile(
                "C:/Users/REx/Desktop/targetdir");
            
            ThreadedDumpToPresistence threadedDumper = new ThreadedDumpToPresistence(
                    Dumpers.BufferedStreamDump,
                    workingDir,
                    LimitOutputBuffers.RegexLineEnd,
                    256);
            threadedDumper.threadedDump(targetDir, 3);

            System.out.println("query on expanded presistence");
            System.out.println("total bytes: " + workingDir.totalBytes());
            System.out.println("size: " + workingDir.size());
        }


        IMetricCollection metricCollection =
                MetricCollections.SERIALIZING.getMetricCollection();
        metricCollection.init(realizedDir, 1000);
        if (realizedDir.size() == 0)
        {
            ThreadedDigestToMetricCollection threadedDigester =
                    new ThreadedDigestToMetricCollection(
                        metricCollection,
                        Digesters.W3CRegexDigester);
            threadedDigester.threadedDigest(workingDir, 3);

            System.out.println("query on metrics presistence");
            System.out.println("total bytes: " + realizedDir.totalBytes());
            System.out.println("size: " + realizedDir.size());
        }

        ThreadedQueryFromMetricCollection threadedQuery = new
                ThreadedQueryFromMetricCollection(MetricQueries.REQUEST_COUNT);
        Object[] results = threadedQuery.threadedQuery(metricCollection, 3);

        long total = 0;
        Map<String, Integer> result = (Map<String, Integer>) results[0];
        results[0] = null;
        for (int i = 1; i < results.length; i++)
        {
            Map<String, Integer> thisResult = (Map<String, Integer>) results[i];
            Iterator<String> it = thisResult.keySet().iterator();
            while(it.hasNext())
            {
                String key = it.next();
                if (result.containsKey(key))
                {
                    result.put(key, result.get(key) + thisResult.get(key));
                }
                else
                {
                    result.put(key, thisResult.get(key));
                }
            }
            thisResult = null;
            results[i] = null;
            System.gc();
        }

        if (result.isEmpty())
        {
            System.out.println("no results");
        }
        else
        {
            result = sortByValue(result);

            for(Map.Entry<String, Integer> entry : result.entrySet())
            {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                total += entry.getValue();
            }

            System.out.println("total metrics found: " + total);
        }

        //workingDir.clear();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
            sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list,
                new Comparator<Map.Entry<K, V>>()
                {
                    public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
                    {
                        return (o1.getValue()).compareTo( o2.getValue() );
                    }
                }
        );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }


}
