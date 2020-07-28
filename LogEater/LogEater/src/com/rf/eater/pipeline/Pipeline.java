/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

import com.rf.eater.source.LogFile;
import com.rf.eater.persistence.PersistenceFactory;
import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.persistence.Persistence;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author REx
 */
public interface Pipeline
{
    public WorkFactory[] getPipeline();
    
    public void setAnalysisRegex(String regex);
    
    public void setAnalysisMapping(String[] map);
    
    public void setSource(LogFile source);
    
    public void setSources(Collection<LogFile> sources);
    
    public void setPersistenceFactory(
            PersistenceFactory persistence,
            Map<String, String> connection);
}
