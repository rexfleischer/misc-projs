/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.dcore.util.record;

/**
 *
 * @author REx
 */
public class IndexedRecord implements Comparable<IndexedRecord>
{
    public final int location;
    
    public final int key;
    
    public final long comparator;
    
    public IndexedRecord(int location, int key, long comparator)
    {
        this.comparator = comparator;
        this.location   = location;
        this.key        = key;
    }

    @Override
    public int compareTo(IndexedRecord o)
    {
        return key - o.key;
    }
}
