/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.util;

/**
 *
 * @author REx
 */
public class ByteArrayUtil
{
    public static byte readByte(byte[] bytes, int pos)
    {
        return bytes[pos];
    }

    public static boolean readBoolean(byte[] bytes, int pos) 
    {
        return bytes[pos] != (byte) 0x00;
    }
    
    public static short readShort(byte[] bytes, int pos)
    {
        return (short)
            (((short) (bytes[pos  ] & 0xff) << 8) |
             ((short) (bytes[pos+1] & 0xff)));
    }
    
    public static int readInt(byte[] bytes, int pos)
    {
        return
            (((int)(bytes[pos+0] & 0xff) << 24) |
             ((int)(bytes[pos+1] & 0xff) << 16) |
             ((int)(bytes[pos+2] & 0xff) <<  8) |
             ((int)(bytes[pos+3] & 0xff)));
    }
    
    public static long readLong(byte[] bytes, int pos)
    {
        return
            (((long)(bytes[pos+0] & 0xff) << 56) |
             ((long)(bytes[pos+1] & 0xff) << 48) |
             ((long)(bytes[pos+2] & 0xff) << 40) |
             ((long)(bytes[pos+3] & 0xff) << 32) |
             ((long)(bytes[pos+4] & 0xff) << 24) |
             ((long)(bytes[pos+5] & 0xff) << 16) |
             ((long)(bytes[pos+6] & 0xff) <<  8) |
             ((long)(bytes[pos+7] & 0xff)));
    }
    
    public static double readDouble(byte[] bytes, int pos)
    {
        return Double.longBitsToDouble(readLong(bytes, pos));
    }
    
    public static void writeByte(byte[] bytes, int pos, byte src)
    {
        bytes[pos] = src;
    }

    public static void writeBoolean(byte[] bytes, int pos, boolean src) 
    {
        bytes[pos] = (byte) (src ? 0xff : 0x00);
    }
    
    public static void writeShort(byte[] bytes, int pos, short src)
    {
        bytes[pos    ] = (byte)(src >> 8);
        bytes[pos + 1] = (byte)(0xff & src);
    }
    
    public static void writeInt(byte[] bytes, int pos, int src)
    {
        bytes[pos+0] = (byte)((0xff000000 & src) >> 24);
        bytes[pos+1] = (byte)((0x00ff0000 & src) >> 16);
        bytes[pos+2] = (byte)((0x0000ff00 & src) >>  8);
        bytes[pos+3] = (byte) (0x000000ff & src);
    }
    
    public static void writeLong(byte[] bytes, int pos, long src)
    {
        bytes[pos    ] = (byte)(0xff & (src >> 56));
        bytes[pos + 1] = (byte)(0xff & (src >> 48));
        bytes[pos + 2] = (byte)(0xff & (src >> 40));
        bytes[pos + 3] = (byte)(0xff & (src >> 32));
        bytes[pos + 4] = (byte)(0xff & (src >> 24));
        bytes[pos + 5] = (byte)(0xff & (src >> 16));
        bytes[pos + 6] = (byte)(0xff & (src >>  8));
        bytes[pos + 7] = (byte)(0xff & src);
    }
    
    public static void writeDouble(byte[] bytes, int pos, double src)
    {
        writeLong(bytes, pos, Double.doubleToLongBits(src));
    }
    
    public void resize(byte[] bytes, int newLength)
    {
        if (newLength == bytes.length)
        {
            return;
        }
        byte[] _bytes = new byte[newLength];
        System.arraycopy(bytes, 0, _bytes, 0, 
                (bytes.length < newLength) ? bytes.length : newLength);
        bytes = _bytes;
    }
}
