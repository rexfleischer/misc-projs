/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.util;

/**
 *
 * @author REx
 */
public class ByteArray
{
    public static final int DEFAULT = 512;
    
    private byte[] bytes;
    
    private int max;
    
    public ByteArray()
    {
        bytes   = new byte[DEFAULT];
        max     = 0;
    }
    
    public ByteArray(int size)
    {
        bytes   = new byte[size];
        max     = 0;
    }
    
    public ByteArray(byte[] bytes)
    {
        this(bytes, bytes.length);
    }
    
    public ByteArray(byte[] bytes, int size)
    {
        if (size < bytes.length)
        {
            throw new IllegalArgumentException(
                    "size must be larger than or equal to bytes.length");
        }
        max         = bytes.length;
        this.bytes  = new byte[size];
        System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
    }
    
    public void read(byte[] dest, int start)
    {
        System.arraycopy(this.bytes, start, dest, 0, dest.length);
    }
    
    public void read(int start, byte[] dest, int destPos, int length)
    {
        System.arraycopy(bytes, start, dest, destPos, length);
    }
    
    public short readShort(int pos)
    {
        return (short)
            (((short) (bytes[pos]) << 8) |
             ((short) (bytes[pos+1] & 0xff)));
    }
    
    public int readInt(int pos)
    {
        return
            (((int)(bytes[pos+0]) << 24) |
             ((int)(bytes[pos+1]) << 16) |
             ((int)(bytes[pos+2]) <<  8) |
             ((int)(bytes[pos+3])));
    }
    
    public long readLong(int pos)
    {
        return
            (((long)(bytes[pos+0]) << 56) |
             ((long)(bytes[pos+1]) << 48) |
             ((long)(bytes[pos+2]) << 40) |
             ((long)(bytes[pos+3]) << 32) |
             ((long)(bytes[pos+4]) << 24) |
             ((long)(bytes[pos+5]) << 16) |
             ((long)(bytes[pos+6]) <<  8) |
             ((long)(bytes[pos+7])));
    }
    
    public void insert(byte[] src, int pos)
    {
        // if the amount of usable bytes is less than the amount
        // that will be used with this insert, then resize the array
        if (max + src.length > bytes.length)
        {
            resize(max + src.length);
        }
        
        // shift everything src.length to make space for src
        System.arraycopy(bytes, pos, bytes, pos + src.length, max - pos);
        
        // copy everything in
        System.arraycopy(src, 0, bytes, pos, src.length);
        
        max += src.length;
    }
    
    public void insertSome(byte[] src, int pos, int delete)
    {
        if (max + src.length - delete > bytes.length)
        {
            resize(max + src.length);
        }
        
        System.arraycopy(bytes, pos + delete, bytes, pos + src.length, max - (pos + delete));
        
        System.arraycopy(src, 0, bytes, pos, src.length);
        
        max += (src.length - delete);
    }
    
    public void write(byte[] src, int pos)
    {
        if (pos + src.length > bytes.length)
        {
            resize(pos + src.length);
            max = bytes.length;
        }
        else if (pos + src.length > max)
        {
            max = pos + src.length;
        }
        System.arraycopy(src, 0, bytes, pos, src.length);
    }
    
    public void write(int start, byte[] src, int srcPos, int length)
    {
        if (start + length > bytes.length)
        {
            resize(start + length);
            max = bytes.length;
        }
        else if (start + length > max)
        {
            max = start + length;
        }
        System.arraycopy(src, srcPos, bytes, start, length);
    }
    
    public void writeShort(short src, int pos)
    {
        if (pos + 1 > bytes.length)
        {
            resize(pos + 1);
            max = bytes.length;
        }
        else if (pos + 1 > max)
        {
            max = pos + 1;
        }
        bytes[pos    ] = (byte)(src >> 8);
        bytes[pos + 1] = (byte)(0xff & src);
    }
    
    public void writeInt(int src, int pos)
    {
        if (pos + 4 > bytes.length)
        {
            resize(pos + 4);
            max = bytes.length;
        }
        else if (pos + 4 > max)
        {
            max = pos + 4;
        }
        bytes[pos    ] = (byte)(src >> 24);
        bytes[pos + 1] = (byte)(0xff & (src >> 16));
        bytes[pos + 2] = (byte)(0xff & (src >> 8));
        bytes[pos + 3] = (byte)(0xff & src);
    }
    
    public void writeLong(long src, int pos)
    {
        if (pos + 8 > bytes.length)
        {
            resize(pos + 8);
            max = bytes.length;
        }
        else if (pos + 8 > max)
        {
            max = pos + 8;
        }
        bytes[pos    ] = (byte)(src >> 56);
        bytes[pos + 1] = (byte)(0xff & (src >> 48));
        bytes[pos + 2] = (byte)(0xff & (src >> 40));
        bytes[pos + 3] = (byte)(0xff & (src >> 32));
        bytes[pos + 4] = (byte)(0xff & (src >> 24));
        bytes[pos + 5] = (byte)(0xff & (src >> 16));
        bytes[pos + 6] = (byte)(0xff & (src >>  8));
        bytes[pos + 7] = (byte)(0xff & src);
    }
    
    public void delete(int pos, int size)
    {
        System.arraycopy(bytes, pos + size, bytes, pos, max - pos - size);
        max -= size;
    }
    
    public void resize(int newLength)
    {
        byte[] _bytes = new byte[newLength];
        System.arraycopy(bytes, 0, _bytes, 0, 
                (bytes.length < newLength) ? bytes.length : newLength);
        bytes = _bytes;
    }
    
    public int compacity()
    {
        return bytes.length;
    }
    
    public int compacityUsed()
    {
        return max;
    }
    
    public byte[] array()
    {
        return bytes;
    }
    
    public void minimize()
    {
        resize(max);
    }
    
    public byte[] copyUsedBytes()
    {
        byte[] result = new byte[max];
        System.arraycopy(bytes, 0, result, 0, max);
        return result;
    }
    
    public boolean bytesEqual(ByteArray array)
    {
        if (array.max != max)
        {
            return false;
        }
        
        for(int i = 0; i < max; i++)
        {
            if (bytes[i] != array.bytes[i])
            {
                return false;
            }
        }
        
        return true;
    }
}
