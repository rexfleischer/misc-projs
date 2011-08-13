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
public class DataTypeBoolean implements DataType
{
    public Long convert(Object data)
        throws Exception
    {
        throw new UnsupportedOperationException();
    }

    public boolean indexable()
    {
        return false;
    }

    public int numOfBytes()
    {
        return 1;
    }

    public void write(ByteBuffer buffer, Object data)
    {
        Boolean writing = (Boolean) data;
        buffer.put(writing ? (byte)-1 : (byte)0);
    }

    public Object read(ByteBuffer buffer)
    {
        byte read = buffer.get();
        if (read == ((byte)-1))
        {
            return true;
        }
        return false;
    }
}
