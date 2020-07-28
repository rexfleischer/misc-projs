/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.consumer.WorkIterator;
import com.rf.eater.thread.WorkerThreadException;
import com.rf.eater.util.SynchronizedQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class Pipeline_Analyze implements WorkFactory
{
    protected String[] mapping;
    
    protected String regexPattern;
    
    protected SynchronizedQueue<String> input;
    
    protected SynchronizedQueue<Map<String, String>> output;

    Pipeline_Analyze(
            SynchronizedQueue<String> input, 
            SynchronizedQueue<Map<String, String>> output)
    {
        this.input  = input;
        this.output = output;
    }
    
    protected void setAnalysisRegex(String regex)
    {
        if (regexPattern != null)
        {
            throw new IllegalStateException("analysis regex was already set");
        }
        regexPattern = regex;
    }

    void setAnalysisMapping(String[] map)
    {
        if (mapping != null)
        {
            throw new IllegalStateException("analysis mapping was already set");
        }
        mapping = map;
    }

    @Override
    public WorkIterator getIterator()
    {
        return new AnalyzeIterator();
    }

    @Override
    public int howMuchMore()
    {
        return input.numberOfElements();
    }

    @Override
    public String workType()
    {
        return "analyze";
    }

    public class AnalyzeIterator implements WorkIterator
    {
        Pattern pattern;

        public AnalyzeIterator()
        {
            pattern = Pattern.compile(regexPattern);
        }
        
        @Override
        public boolean doWork() throws WorkerThreadException
        {
            for(int i = 0; i < 100; i++)
            {
                String working = input.pop();
                if (working == null)
                {
                    return false;
                }
                
                Matcher matcher = pattern.matcher(working);
                if (matcher.find())
                {
                    Map<String, String> result = new HashMap<String, String>();
                    for(int j = 0; j < mapping.length; j++)
                    {
                        result.put(mapping[j], matcher.group(j + 1));
                    }
                    output.push(result);
                }
            }
            return true;
        }
        
        @Override
        public void close() throws WorkerThreadException
        {
            // dont really need to do anything
        }

        @Override
        public String workType()
        {
            return Pipeline_Analyze.this.workType();
        }
        
    }
}

