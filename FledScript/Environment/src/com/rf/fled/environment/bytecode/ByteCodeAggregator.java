/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.bytecode;

import java.util.LinkedList;

/**
 *
 * @author REx
 */
public class ByteCodeAggregator
{
    private static final int FLUSH_SIZE = 512;
    
    private ByteCode curr;
    
    private int total;
    
    private LinkedList<ByteCode> bytes;
    
    private int cap;
    
    public ByteCodeAggregator()
            throws ByteCodeAggregatorException
    {
        this(Integer.MAX_VALUE);
    }
    
    public ByteCodeAggregator(int cap)
            throws ByteCodeAggregatorException
    {
        if (cap <= 64)
        {
            throw new ByteCodeAggregatorException("cap cannot be less than 64");
        }
        total = 0;
        bytes = new LinkedList<ByteCode>();
        bytes.add(new ByteCode(FLUSH_SIZE));
        curr = bytes.getLast();
        this.cap = cap;
    }
    
    public int getTotal()
    {
        return total + bytes.getLast().compacityUsed();
    }
    
    public ByteCode getCurr() throws ByteCodeAggregatorException
    {
        rangeCheck();
        return curr;
    }
    
    public void write(byte[] src) throws ByteCodeAggregatorException
    {
        curr.write(src, curr.compacityUsed());
        rangeCheck();
    }
    
    public void writeByte(byte src) throws ByteCodeAggregatorException
    {
        curr.writeByte(src, curr.compacityUsed());
        rangeCheck();
    }

    public void writeBoolean(boolean src) throws ByteCodeAggregatorException 
    {
        curr.writeBoolean(src, curr.compacityUsed());
        rangeCheck();
    }
    
    public void writeShort(short src) throws ByteCodeAggregatorException
    {
        curr.writeShort(src, curr.compacityUsed());
        rangeCheck();
    }
    
    public void writeInt(int src) throws ByteCodeAggregatorException
    {
        curr.writeInt(src, curr.compacityUsed());
        rangeCheck();
    }
    
    public void writeLong(long src) throws ByteCodeAggregatorException
    {
        curr.writeLong(src, curr.compacityUsed());
        rangeCheck();
    }
    
    public ByteCode finished() 
            throws ByteCodeAggregatorException
    {
        ByteCode result = new ByteCode(getTotal());
        for(ByteCode code : bytes)
        {
            result.write(result.compacityUsed(), 
                         code.array(), 
                         0, 
                         code.compacityUsed());
        }
        if (result.compacityUsed() != getTotal())
        {
            throw new ByteCodeAggregatorException(
                    "sanity check failed: result.compacityUsed() != getTotal()");
        }
        return result;
    }
    
    private void rangeCheck()
            throws ByteCodeAggregatorException
    {
        if (curr.compacityUsed() >= (FLUSH_SIZE - 10))
        {
            total += curr.compacityUsed();
            bytes.add(new ByteCode(FLUSH_SIZE));
            curr = bytes.getLast();
        }
        if (cap < getTotal())
        {
            throw new ByteCodeAggregatorException(
                    "cap exceeded: cap="+cap+", total="+getTotal());
        }
    }
}
