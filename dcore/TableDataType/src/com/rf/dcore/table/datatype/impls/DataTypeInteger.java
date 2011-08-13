/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.datatype.impls;

import com.rf.dcore.table.datatype.DataType;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public class DataTypeInteger implements DataType
{

    public Long convert(Object data)
            throws Exception
    {
        if (!(data instanceof Integer))
        {
            throw new IllegalArgumentException(
                    "data must be of instance Integer");
        }
        return new Long((Integer) data);
    }

    public boolean indexable()
    {
        return true;
    }

    public int numOfBytes()
    {
        return Integer.SIZE / 8;
    }

    public void write(ByteBuffer buffer, Object data)
    {
        buffer.putInt((Integer)data);
    }

    public Object read(ByteBuffer buffer)
    {
        return buffer.getInt();
    }

}
