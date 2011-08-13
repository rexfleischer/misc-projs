/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.datatype.impls;

import com.rf.dcore.table.datatype.DataType;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 *
 * @author REx
 */
public class DataTypeText implements DataType
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
        return (Character.SIZE / 8) * 65536;
    }

    public void write(ByteBuffer buffer, Object data)
    {
        String putting = (String) data;
        if (putting.length() > 65536)
        {
            throw new BufferOverflowException();
        }

        int i = 0;
        for( ; i < putting.length(); i++)
        {
            buffer.putChar(putting.charAt(i));
        }
        for( ; i < 65536; i++)
        {
            buffer.putChar((char) -1);
        }
    }

    public Object read(ByteBuffer buffer)
    {
        StringBuilder sb = new StringBuilder(128);
        for(int i = 0; i < 65536; i++)
        {
            char get = buffer.getChar();
            if (get != (char)-1)
            {
                sb.append(buffer.getChar());
            }
        }
        return sb.toString();
    }
}
