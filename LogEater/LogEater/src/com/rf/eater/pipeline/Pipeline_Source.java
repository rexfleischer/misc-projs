/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.consumer.WorkIterator;
import com.rf.eater.source.LogFile;
import com.rf.eater.thread.WorkerThreadException;
import com.rf.eater.util.SynchronizedQueue;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author REx
 */
public class Pipeline_Source implements WorkFactory
{
    private class FileUsagePair
    {
        final Object LOCK = new Object();
        boolean used;
        boolean finished;
        LogFile file;
    }
    
    private final Object LOCK;

    private LinkedList<FileUsagePair> files;

    private SynchronizedQueue<String> output;

    Pipeline_Source(SynchronizedQueue<String> output)
    {
        this.LOCK       = new Object();
        this.files      = new LinkedList<FileUsagePair>();
        this.output     = output;
    }

    protected void setSource(LogFile source)
    {
        FileUsagePair file = new FileUsagePair();
        file.file = source;
        file.used = false;
        file.finished = false;
        files.add(file);
    }

    protected void setSources(Collection<LogFile> sources)
    {
        for (LogFile file : sources)
        {
            setSource(file);
        }
    }

    @Override
    public WorkIterator getIterator()
    {
        synchronized(LOCK)
        {
            for(FileUsagePair pair : files)
            {
                synchronized(pair.LOCK)
                {
                    if (!pair.used)
                    {
                        pair.used = true;
                        return new FromSourcesIterator(pair, output);
                    }
                }
            }
            return null;
        }
    }

    @Override
    public int howMuchMore()
    {
        return files.size();
    }

    @Override
    public String workType()
    {
        return "sources";
    }

    public class FromSourcesIterator implements WorkIterator
    {
        FileUsagePair file;
        
        SynchronizedQueue<String> output;

        private FromSourcesIterator(FileUsagePair file, SynchronizedQueue<String> output)
        {
            this.file = file;
            this.output = output;
        }

        @Override
        public boolean doWork() throws WorkerThreadException
        {
            String[] lines = null;
            try
            {
                lines = file.file.getNextLines();
            }
            catch (IOException ex)
            {
                throw new WorkerThreadException("error during read", ex);
            }
            if (lines == null)
            {
                file.finished = true;
                file.used = false;
                return false;
            }
            for (String line : lines)
            {
                output.push(line);
            }
            return true;
        }

        @Override
        public String workType()
        {
            return Pipeline_Source.this.workType();
        }

        @Override
        public void close() throws WorkerThreadException
        {
            synchronized(file.LOCK)
            {
                file.used = false;
            }
        }
    }
}
