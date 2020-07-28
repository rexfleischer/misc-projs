/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

/**
 *
 * @author REx
 */
class DataTypeImpl_Binary implements DataTypeImpl 
{
    @Override
    public int deserialize(Object[] columns, int index, int location, byte[] buffer)
    {
        // first, get how many bytes are actually in the string...
        // we can just save this as an int
        int length = 
                (((int)(buffer[location  ] & 0xff) << 24) |
                 ((int)(buffer[location+1] & 0xff) << 16) |
                 ((int)(buffer[location+2] & 0xff) <<  8) |
                 ((int)(buffer[location+3] & 0xff)));
        // make the array where we hold the data
        byte[] src = new byte[length];
        // copy the string data
        System.arraycopy(buffer, location + 4, src, 0, src.length);
        // make the string
        columns[index] = src;
        // return the updated location
        return location + length + 4;
    }

    @Override
    public byte[] serialize(Object object)
    {
        byte[] src = (byte[]) object;
        byte[] result = new byte[src.length + 4];
        result[0] = (byte)((0xff000000 & src.length) >> 24);
        result[1] = (byte)((0x00ff0000 & src.length) >> 16);
        result[2] = (byte)((0x0000ff00 & src.length) >>  8);
        result[3] = (byte) (0x000000ff & src.length);
        System.arraycopy(src, 0, result, 4, src.length);
        return result;
    }

    @Override
    public long reduceToLong(Object object) 
    {
        throw new UnsupportedOperationException("datatype not indexable");
    }

    @Override
    public boolean indexable()
    {
        return false;
    }

    @Override
    public String description()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String name() 
    {
        return "binary";
    }
}
