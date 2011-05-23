/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.manager;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author REx
 */
public class ThreadedDumper extends Thread
{
    private IDumper dumper;
    
    private IPresistence presistence;

    private ArrayList<String> files;

    private long maxKBChunk;

    public ThreadedDumper(
            IDumper dumper,
            IPresistence presistence,
            ArrayList<String> files,
            long maxKBChunk)
    {
        this.dumper = dumper;
        this.presistence = presistence;
        this.files = new ArrayList<String>(files);
        this.maxKBChunk = maxKBChunk;
    }

    @Override public void run()
    {
        Iterator<String> it = files.iterator();
        while(it.hasNext())
        {
            String file = it.next();
            dumper.dump(file, presistence, maxKBChunk);
        }
    }
}
