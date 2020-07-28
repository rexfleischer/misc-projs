/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.heap;

/**
 *
 * @author REx
 */
public class HeapManager
{
    /**
     * just make the heap size 10KB for now
     */
    private static final int DEFAULT_HEAP_SIZE = 10 * 1024;
    
    private byte[] heap;
    
    public HeapManager()
    {
        heap = new byte[DEFAULT_HEAP_SIZE];
    }
    
    public HeapManager(int size)
    {
        heap = new byte[size];
    }
    
    protected void garbageCollect()
    {
        
    }
    
    public int getHeapSize()
    {
        return heap.length;
    }
    
    public int declareBoolean()
    {
        return 0;
    }
    
    public int declareInteger()
    {
        return 0;
    }
    
    public int declareDouble()
    {
        return 0;
    }
    
   // public 
}
