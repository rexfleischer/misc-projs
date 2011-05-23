/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.manager;

import com.rf.memory.manager.dumper.GZipDumper;
import com.rf.memory.manager.dumper.TxtFileDumper;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public abstract class IDumper
{
    public enum Dumpers
    {

        GZIP()    
        {
            public IDumper getDumper()
            {
                return new GZipDumper();
            }
        },
        TXTFILE()    
        {
            public IDumper getDumper()
            {
                return new TxtFileDumper();
            }
        };

        public abstract IDumper getDumper();
    }

    public abstract void dump(String filename, IPresistence presistence, long maxKBChunk);

    public static void threadedDirDump(
            String targetDir,
            Pattern filePattern,
            Dumpers dumper,
            IPresistence presistence,
            long maxKBChunk,
            int numOfThreads)
            throws InterruptedException
    {
        if (targetDir == null)
        {
            throw new NullPointerException("targetDir");
        }
        if (dumper == null)
        {
            throw new NullPointerException("dumper");
        }
        if (presistence == null)
        {
            throw new NullPointerException("presistence");
        }
        if (targetDir.isEmpty())
        {
            throw new IllegalArgumentException("targetDir");
        }
        if (maxKBChunk < 1)
        {
            throw new IllegalArgumentException("maxKBChunk");
        }
        // get the list of all the files in the dir
        File[] files = (new File(targetDir)).listFiles();

        // for organization
        ArrayList<ArrayList<String>> fileNames = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < numOfThreads; i++)
        {
            fileNames.add(new ArrayList<String>());
        }

        // check if the file is part of the pattern, and then if it is put in
        // the organization stack for the threads
        int count = 0;
        for (int i = 0; i < files.length; i++)
        {
            String filename = files[i].getAbsolutePath();
            if (filePattern == null)
            {
                fileNames.get(count % numOfThreads).add(filename);
                count++;
            }
            else
            {
                Matcher matcher = filePattern.matcher(filename);
                if (matcher.find())
                {
                    fileNames.get(count % numOfThreads).add(filename);
                    count++;
                }
            }
        }

        // start all of the threads. they all get their own dumpers because
        // its not 'thread safe' at the moment. but the presistence layer
        // must be thread safe for a app like this to work
        ThreadedDumper[] threads = new ThreadedDumper[numOfThreads];
        for (int i = 0; i < numOfThreads; i++)
        {
            threads[i] = new ThreadedDumper(
                    dumper.getDumper(),
                    presistence,
                    fileNames.get(i),
                    maxKBChunk);
            threads[i].start();
        }

        // wait for all the treads to finish
        for (int i = 0; i < numOfThreads; i++)
        {
            threads[i].join();
        }
    }

}
