/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

/**
 *
 * @author REx
 */
public class DataTypeImpl_Boolean implements DataTypeImpl
{
    @Override
    public int deserialize(Object[] columns, int index, int location, byte[] buffer)
    {
        columns[index] = (buffer[location] == (byte) 0xff);
        return location + 1;
    }

    @Override
    public byte[] serialize(Object object)
    {
        Boolean src = (Boolean) object;
        return new byte[]{ src ? (byte) 0xff : (byte) 0x00};
    }

    @Override
    public boolean indexable()
    {
        return false;
    }

    @Override
    public long reduceToLong(Object object) 
    {
        throw new UnsupportedOperationException("datatype not indexable");
    }

    @Override
    public String description()
    {
        return "its a freakin' boolean!";
    }

    @Override
    public String name() 
    {
        return "boolean";
    }
}
