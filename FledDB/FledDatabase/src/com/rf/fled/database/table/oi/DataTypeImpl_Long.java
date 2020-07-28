/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

/**
 *
 * @author REx
 */
public class DataTypeImpl_Long implements DataTypeImpl
{
    @Override
    public int deserialize(Object[] columns, int index, int location, byte[] buffer)
    {
        columns[index] = 
            (((long)(buffer[location  ] & 0xff) << 56) |
             ((long)(buffer[location+1] & 0xff) << 48) |
             ((long)(buffer[location+2] & 0xff) << 40) |
             ((long)(buffer[location+3] & 0xff) << 32) |
             ((long)(buffer[location+4] & 0xff) << 24) |
             ((long)(buffer[location+5] & 0xff) << 16) |
             ((long)(buffer[location+6] & 0xff) <<  8) |
             ((long)(buffer[location+7] & 0xff)));
        return location + 8;
    }

    @Override
    public byte[] serialize(Object object)
    {
        long src = (Long) object;
        byte[] bytes = new byte[8];
        bytes[0] = (byte)(0xff & (src >> 56));
        bytes[1] = (byte)(0xff & (src >> 48));
        bytes[2] = (byte)(0xff & (src >> 40));
        bytes[3] = (byte)(0xff & (src >> 32));
        bytes[4] = (byte)(0xff & (src >> 24));
        bytes[5] = (byte)(0xff & (src >> 16));
        bytes[6] = (byte)(0xff & (src >>  8));
        bytes[7] = (byte)(0xff & src);
        return bytes;
    }

    @Override
    public long reduceToLong(Object object) 
    {
        return (Long) object;
    }

    @Override
    public boolean indexable()
    {
        return true;
    }

    @Override
    public String description()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String name() 
    {
        return "long";
    }
}
