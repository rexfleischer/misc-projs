/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester.digest;

import com.rf.logs.digester.interfaces.IDigester;
import com.rf.logs.metrics.Metric;
import com.rf.logs.metrics.interfaces.IMetricCollection;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rfleischer
 */
public class AkamaiRegexDigester implements IDigester
{
    private Pattern pattern;

    private long count;

    public AkamaiRegexDigester()
    {
        this.pattern = Pattern.compile(
                "([0-9]{4}-[0-9]{2}-[0-9]{2})\\t" + "([^\\t]*)\\t" +   // date         time
                "([^\\t]*)\\t" + "([^\\t]*)\\t" +   // cs-ip        cs-method
                "([^\\t]*)\\t",                     // cs-uri
                Pattern.MULTILINE);
        count = 0;
    }

    public IMetricCollection digest(IMetricCollection collection, String content)
    {
        if (collection == null)
        {
            throw new NullPointerException("collection");
        }
        if (content == null)
        {
            throw new NullPointerException("content");
        }
        Matcher matcher = pattern.matcher(content);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        while(matcher.find())
        {
            Metric metric = new Metric();
            metric.IP = matcher.group(3);
            metric.request = matcher.group(5);
            metric.parseTime(matcher.group(2));
            try
            {
                metric.date = dateFormat.parse(matcher.group(1));
            }
            catch (ParseException ex)
            {
                metric.error = ex.getMessage();
            }
            try
            {
                collection.add(metric);
            }
            catch (IOException ex)
            {
                System.err.println(ex.getMessage());
            }

            count++;
            if (count % 10000 == 0)
            {
                System.out.println(Thread.currentThread().getName() + " found " + count);
            }
        }
        return collection;
    }

}
