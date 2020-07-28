/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.util;

/**
 *
 * @author REx
 */
public class LongList 
{
    private static final int LONG_SIZE = 8;
    
    private ByteArray bytes;

    private int keysUsed;
    
    private boolean rangeCheck(int index)
    {
        return 0 <= index && index < keysUsed;
    }
    
    private int position(int index)
    {
        return index * LONG_SIZE;
    }

    public LongList() 
    {
        keysUsed = 0;
        bytes = new ByteArray(8); // assume at least one will be added
    }
    
    public LongList(byte[] buffer) 
    {
        if (buffer.length % LONG_SIZE != 0)
        {
            throw new IllegalArgumentException(
                    "buffer is a list of longs, but buffer.length % 8 != 0");
        }
        keysUsed = buffer.length / LONG_SIZE;
        bytes = new ByteArray(buffer);
    }

    public byte[] copyUsedBytes() 
    {
        return bytes.copyUsedBytes();
    }
    
    public void remove(long target)
    {
        for(int i = 0; i < keysUsed; i++)
        {
            long key = bytes.readLong(position(i));
            if (key == target)
            {
                bytes.delete(position(i), LONG_SIZE);
                keysUsed--;
                i--;
            }
        }
    }
    
    public boolean exists(long target)
    {
        for(int i = 0; i < keysUsed; i++)
        {
            long key = bytes.readLong(position(i));
            if (key == target)
            {
                return true;
            }
        }
        return false;
    }
    
    public void add(long value)
    {
        if (!exists(value))
        {
            bytes.writeLong(value, position(keysUsed));
            keysUsed++;
        }
    }

    public boolean isEmpty() 
    {
        return keysUsed == 0;
    }
}
