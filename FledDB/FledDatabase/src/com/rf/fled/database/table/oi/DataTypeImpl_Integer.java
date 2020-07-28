/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

/**
 *
 * @author REx
 */
public class DataTypeImpl_Integer implements DataTypeImpl
{
    @Override
    public int deserialize(Object[] columns, int index, int location, byte[] buffer)
    {
        columns[index] = 
                (((int)(buffer[location  ] & 0xff) << 24) |
                 ((int)(buffer[location+1] & 0xff) << 16) |
                 ((int)(buffer[location+2] & 0xff) <<  8) |
                 ((int)(buffer[location+3] & 0xff)));
        return location + 4;
    }

    @Override
    public byte[] serialize(Object object)
    {
        int src = (Integer) object;
        byte[] result = new byte[4];
        result[0] = (byte)((0xff000000 & src) >> 24);
        result[1] = (byte)((0x00ff0000 & src) >> 16);
        result[2] = (byte)((0x0000ff00 & src) >>  8);
        result[3] = (byte) (0x000000ff & src);
        return result;
    }

    @Override
    public long reduceToLong(Object object) 
    {
        return (long)((Integer) object);
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
        return "Integer";
    }
}
