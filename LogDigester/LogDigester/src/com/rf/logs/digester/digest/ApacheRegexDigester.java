/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.logs.digester.digest;

import com.rf.logs.digester.IDigester;
import com.rf.logs.digester.RegexDefinitions;
import com.rf.logs.metrics.MetricCollection;
import com.rf.logs.metrics.Metric;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class ApacheRegexDigester implements IDigester
{
    private Pattern pattern;

    private long count;

    public ApacheRegexDigester()
    {
        this.pattern = Pattern.compile(
                RegexDefinitions.MATCH_IP +
                    RegexDefinitions.MATCH_SPACE +
                RegexDefinitions.MATCH_DATETIME +
                    RegexDefinitions.MATCH_SPACE +
                RegexDefinitions.MATCH_REQUEST, Pattern.MULTILINE);
        count = 0;
    }

    @Override
    public MetricCollection digest(
            MetricCollection collection,
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

        while(matcher.find())
        {
            try
            {
                collection.add(new Metric(
                        matcher.group(1),
                        matcher.group(4),
                        matcher.group(2),
                        matcher.group(3)));
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
