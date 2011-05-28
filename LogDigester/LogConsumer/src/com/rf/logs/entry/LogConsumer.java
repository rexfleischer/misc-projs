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
import com.rf.logs.query.MetricQueries;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import com.rf.logs.query.util.Sort;
import com.rf.memory.persistence.Dumpers;
import com.rf.memory.persistence.InputStreamSets;
import com.rf.memory.persistence.LimitOutputBuffers;
import com.rf.memory.persistence.Persistences;
import com.rf.memory.persistence.interfaces.IInputStreamSet;
import com.rf.memory.persistence.interfaces.IPersistence;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                    56);
            threadedDumper.threadedDump(targetDir, 3);

            System.out.println("query on expanded presistence");
            System.out.println("total bytes: " + workingDir.totalBytes());
            System.out.println("size: " + workingDir.size());
        }


        IMetricCollection metricCollection =
                MetricCollections.GENERIC.getMetricCollection();
        metricCollection.init(realizedDir, 500);
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

        IMetricCollection deamonedMetricCollection =
                MetricCollections.DEAMONED_SERVER.getMetricCollection();
        deamonedMetricCollection.init(realizedDir, 0);
        long total = 0;
        Map<String, Integer> result = (Map<String, Integer>) 
                threadedQuery.threadedQuery(deamonedMetricCollection, 3);

        if (result.isEmpty())
        {
            System.out.println("no results");
        }
        else
        {
            result = Sort.sortByValue(result);

            for(Map.Entry<String, Integer> entry : result.entrySet())
            {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                total += entry.getValue();
            }

            System.out.println("total metrics found: " + total);
        }

        //workingDir.clear();
        realizedDir.clear();
    }
}
