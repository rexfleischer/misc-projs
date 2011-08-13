/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.table.datatype.impls;

import com.rf.dcore.table.datatype.DataType;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 *
 * @author REx
 */
public class DataTypeString implements DataType
{

    public Long convert(Object data)
        throws Exception
    {
        if (!(data instanceof Long))
        {
            throw new IllegalArgumentException(
                    "data must be of instance Integer");
        }
        String str = (String) data;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes("iso-8859-1"), 0, str.length());
        byte[] md5hash = md.digest();
        Long result = 0L;
        for (int i = 0; i < md5hash.length; i++)
        {
            result = (result << 3);
            result += ((md5hash[i] & 0x0F) + ( (md5hash[i] >>> 4) & 0x0F));
        }
        return result;
    }

    public boolean indexable()
    {
        return true;
    }

    public int numOfBytes()
    {
        return (Character.SIZE / 8) * 128;
    }

    public void write(ByteBuffer buffer, Object data)
    {
        String putting = (String) data;
        if (putting.length() > 128)
        {
            throw new BufferOverflowException();
        }

        int i = 0;
        for( ; i < putting.length(); i++)
        {
            buffer.putChar(putting.charAt(i));
        }
        for( ; i < 128; i++)
        {
            buffer.putChar((char) -1);
        }
    }

    public Object read(ByteBuffer buffer)
    {
        StringBuilder sb = new StringBuilder(128);
        for(int i = 0; i < 128; i++)
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
