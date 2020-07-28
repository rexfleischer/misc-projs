/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

import com.rf.eater.persistence.Persistence;
import com.rf.eater.source.LogFile;
import com.rf.eater.persistence.PersistenceFactory;
import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.util.SynchronizedQueue;
import java.util.Collection;
import java.util.Map;

/**
 * the basic pipeline has three parts to analyzing logs. 
 * 1 - pull logs out of a file
 * 2 - analyzing the individual logs
 * 3 - persisting the analyzed logs
 * @author REx
 */
class PipelineWork_Basic implements Pipeline
{
    private Pipeline_Persistence persistence;
    
    private Pipeline_Analyze analyze;
    
    private Pipeline_Source sources;
    
    protected PipelineWork_Basic()
    {
        SynchronizedQueue<String> source_to_analyze = 
                new SynchronizedQueue<String>();
        SynchronizedQueue<Map<String, String>> analyze_to_persist = 
                new SynchronizedQueue<Map<String, String>>();
        
        sources = new Pipeline_Source(source_to_analyze);
        analyze = new Pipeline_Analyze(source_to_analyze, analyze_to_persist);
        persistence = new Pipeline_Persistence(analyze_to_persist);
    }
    
    @Override
    public WorkFactory[] getPipeline ()
    {
        return new WorkFactory[]{sources, analyze, persistence};
    }

    @Override
    public void setSource (LogFile source)
    {
        this.sources.setSource(source);
    }
    
    @Override
    public void setSources(Collection<LogFile> sources)
    {
        this.sources.setSources(sources);
    }

    @Override
    public void setPersistenceFactory (
            PersistenceFactory persistence, 
            Map<String, String> connection)
    {
        this.persistence.setPersistenceFactory(persistence, connection);
    }

    @Override
    public void setAnalysisRegex(String regex)
    {
        analyze.setAnalysisRegex(regex);
    }

    @Override
    public void setAnalysisMapping(String[] map)
    {
        analyze.setAnalysisMapping(map);
    }
}
