/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.memory;

import com.rf.fled.environment.util.ByteArrayUtil;

/**
 *
 * @author REx
 */
public class Stack
{
    public static final int HEADER = 8;
    
    public static final int DEFAULT_SIZE = 10 * 1024;
    
    private int pointer;
    
    private byte[] bytes;
    
    public Stack()
    {
        bytes = new byte[DEFAULT_SIZE];
        pointer = bytes.length;
    }
    
    /**
     * the definition of a call is organized as:
     * [return address][allocation amount][pointer][function allocation]
     * @param returnAddress
     * @param allocating 
     */
    public void pushCall(int returnAddress, int allocating)
            throws StackOverflowException
    {
        try
        {
            pointer -= (HEADER + allocating);
            ByteArrayUtil.writeInt(bytes, pointer - 8, returnAddress);
            ByteArrayUtil.writeInt(bytes, pointer - 4, allocating);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            throw new StackOverflowException();
        }
    }
    
    public int popCall()
    {
        int address = ByteArrayUtil.readInt(bytes, pointer - HEADER);
        pointer += ByteArrayUtil.readInt(bytes, pointer - 4) + HEADER;
        return address;
    }
    
    public void writeByte(int offset, byte src)
    {
        ByteArrayUtil.writeByte(bytes, pointer + offset, src);
    }
    
    public void writeBoolean(int offset, boolean src)
    {
        ByteArrayUtil.writeBoolean(bytes, pointer + offset, src);
    }
    
    public void writeShort(int offset, short src)
    {
        ByteArrayUtil.writeShort(bytes, pointer + offset, src);
    }
    
    public void writeInteger(int offset, int src)
    {
        ByteArrayUtil.writeInt(bytes, pointer + offset, src);
    }
    
    public void writeDouble(int offset, double src)
    {
        ByteArrayUtil.writeDouble(bytes, pointer + offset, src);
    }
    
    public byte readByte(int offset)
    {
        return ByteArrayUtil.readByte(bytes, pointer + offset);
    }
    
    public boolean readBoolean(int offset)
    {
        return ByteArrayUtil.readBoolean(bytes, pointer + offset);
    }
    
    public short readShort(int offset)
    {
        return ByteArrayUtil.readShort(bytes, pointer + offset);
    }
    
    public int readInteger(int offset)
    {
        return ByteArrayUtil.readInt(bytes, pointer + offset);
    }
    
    public double readDouble(int offset)
    {
        return ByteArrayUtil.readDouble(bytes, pointer + offset);
    }
    
    public int memMaxAllacable()
    {
        return bytes.length - HEADER;
    }
    
    public int memAlloced()
    {
        return memMaxAllacable() - memAvailable();
    }
    
    public int memAvailable()
    {
        return pointer - HEADER;
    }
}
