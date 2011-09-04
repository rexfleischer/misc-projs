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
public class DataTypeDate implements DataType
{

    public Long convert(Object data)
        throws Exception
    {
        if (!(data instanceof Long))
        {
            throw new IllegalArgumentException(
                    "data must be of instance Long");
        }
        return (Long) data;
    }

    public boolean indexable()
    {
        return true;
    }

    public int numOfBytes()
    {
        return Long.SIZE / 8;
    }

    public void write(ByteBuffer buffer, Object data)
    {
        buffer.putLong((Long)data);
    }

    public Object read(ByteBuffer buffer)
    {
        return buffer.getLong();
    }

}
