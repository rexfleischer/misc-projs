/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.entry;

import com.rf.logs.digester.IDigester;
import com.rf.logs.digester.IDigester.Digesters;
import com.rf.logs.metrics.IMetricCollection;
import com.rf.logs.metrics.IMetricCollection.MetricCollections;
import com.rf.logs.metrics.MetricQuery;
import com.rf.logs.metrics.MetricQuery.MetricQueries;
import com.rf.memory.parts.interfaces.IDumper;
import com.rf.memory.parts.interfaces.IPresistence;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
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
        String targetDir = "C:/Users/rfleischer/Desktop/images-bone3";
        String workingDir = "C:/Users/rfleischer/Desktop/workingdir";
        String resultsDir = "C:/Users/rfleischer/Desktop/resultsdir";

        // first, get all of the files and put it in a easy to read form.. note, this
        // is not required.
        IPresistence presistence = IPresistence.Presistences.TXTFILE.getPresistence();
        presistence.init(workingDir, "expanded");
        IDumper.threadedDirDump(
                targetDir, null,
                IDumper.Dumpers.GZIP,
                presistence, 64, 3);
        System.out.println("query on expanded presistence");
        System.out.println("total bytes: " + presistence.totalBytes());
        System.out.println("size: " + presistence.size());

        // now, get all of the digestable metrics and put it in a serialized
        // format in blocks of ArrayLists with 1000 Metric objects each
        IPresistence metricPresistence = IPresistence.Presistences.SERIALIZINGFILE.getPresistence();
        metricPresistence.init(resultsDir, "metric");
        IMetricCollection metrics = MetricCollections.SERIALIZING.getMetricCollection();
        metrics.init(metricPresistence, 1000);
        IDigester digester = Digesters.AkamaiDigester.getDigester();
        for (int i = 0; i < presistence.size(); i++)
        {
            digester.digest(metrics, (String) presistence.getContent(i));
        }
        System.out.println("query on metric presistence");
        System.out.println("total bytes: " + metricPresistence.totalBytes());
        System.out.println("size: " + metricPresistence.size());


        MetricQuery query = MetricQueries.REQUEST_COUNT.getMetricQuery();

        long total = 0;
        Map<String, Integer> results = query.doQuery(metrics);
        if (results.isEmpty())
        {
            System.out.println("no results");
        }
        else
        {
            Map<String, Integer> valueSortedResults = sortByValue(results);

            for(Map.Entry<String, Integer> entry : valueSortedResults.entrySet())
            {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                total += entry.getValue();
            }
        }

        System.out.println("total results: " + total);
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
