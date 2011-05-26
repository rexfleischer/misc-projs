/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester.digest;

import com.rf.logs.digester.IDigester;
import com.rf.logs.metrics.IMetricCollection;
import com.rf.logs.metrics.Metric;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class ApacheRegexDigester implements IDigester
{
    public static final String MATCH_SPACE      = ".*";

    public static final String MATCH_IP         = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";

    public static final String MATCH_DATETIME   = "\\[([0-3]?[0-9]\\/[a-zA-Z]{3}\\/[0-9]*):([0-9]{2}:[0-9]{2}:[0-9]{2})";

    public static final String MATCH_REQUEST    = " (\\/[^\\\"]*)( |\\\")";

    private Pattern pattern;

    private long count;

    public ApacheRegexDigester()
    {
        this.pattern = Pattern.compile(
                MATCH_IP + MATCH_SPACE + MATCH_DATETIME + MATCH_SPACE + MATCH_REQUEST, Pattern.MULTILINE);
        count = 0;
    }

    @Override
    public IMetricCollection digest(
            IMetricCollection collection,
            String content)
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
        while(matcher.find())
        {
            try
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

                collection.add(metric);
                count++;
                if (count % 1000 == 0)
                {
                    System.out.println("found 1000 more");
                }
            }
            catch (IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
        try
        {
            collection.commit();
        } 
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        return collection;
    }

}
