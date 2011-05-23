/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.entry;

import com.rf.logs.digester.IDigester;
import com.rf.logs.digester.IDigester.Digesters;
import com.rf.logs.metrics.MetricCollection;
import com.rf.logs.metrics.MetricCollection.MetricCollections;
import com.rf.logs.metrics.MetricQuery;
import com.rf.logs.metrics.MetricQuery.MetricQueries;
import com.rf.memory.manager.IDumper;
import com.rf.memory.manager.IPresistence;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
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
        String targetDir = "C:/Users/REx/Desktop/targetdir";
        String workingDir = "C:/Users/REx/Desktop/workingdir";
        
        IPresistence presistence = IPresistence.Presistences.TXTFILE.getPresistence();
        presistence.init(workingDir, "expanded");

        IDumper.threadedDirDump(
                targetDir, null,
                IDumper.Dumpers.TXTFILE,
                presistence, 64, 1);

        System.out.println("total bytes: " + presistence.totalBytes());
        System.out.println("size: " + presistence.size());

        IPresistence queryPresistence = IPresistence.Presistences.SERIALIZINGFILE.getPresistence();
        queryPresistence.init(workingDir, "query");
        IPresistence metricPresistence = IPresistence.Presistences.SERIALIZINGFILE.getPresistence();
        metricPresistence.init(workingDir, "metric");
        IDigester digester = Digesters.W3CRegexDigester.getDigester();
        MetricCollection metrics = MetricCollections.SERIALIZING.getMetricCollection();
        metrics.init(metricPresistence, queryPresistence, 100);

        for (int i = 0; i < presistence.size(); i++)
        {
            digester.digest(metrics, (String) presistence.getContent(i));
        }

        MetricQuery query = MetricQueries.REQUEST_COUNT.getMetricQuery();

        Map<String, Integer> results = query.doQuery(metrics);
        if (results.isEmpty())
        {
            System.out.println("no results");
        }
        else
        {
            Iterator<String> it = results.keySet().iterator();
            while(it.hasNext())
            {
                String result = it.next();
                System.out.println(result + ": " + results.get(result));
            }
        }

        presistence.clear();
        metricPresistence.clear();
    }

}
