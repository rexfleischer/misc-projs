/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.fled.datatype;

import com.rf.fled.exceptions.FledDataTypeException;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author REx
 */
public enum DataTypes
{
    BOOLEAN()
    {
        @Override public Long convert(Object data) 
                throws FledDataTypeException
        {
            throw new UnsupportedOperationException();
        }
        @Override public boolean indexable()
        {
            return false;
        }
        @Override public int numOfBytes()
        {
            return 1;
        }
        @Override public void write(ByteBuffer buffer, Object data)
        {
            Boolean writing = (Boolean) data;
            buffer.put(writing ? (byte)-1 : (byte)0);
        }
        @Override public Object read(ByteBuffer buffer)
        {
            byte read = buffer.get();
            if (read == ((byte)-1))
            {
                return true;
            }
            return false;
        }
    },
    INTEGER()
    {
        @Override public Long convert(Object data)
                throws FledDataTypeException
        {
            if (!(data instanceof Integer))
            {
                throw new IllegalArgumentException(
                        "data must be of instance Integer");
            }
            return new Long((Integer) data);
        }
        @Override public boolean indexable()
        {
            return true;
        }
        @Override public int numOfBytes()
        {
            return Integer.SIZE / 8;
        }
        @Override public void write(ByteBuffer buffer, Object data)
        {
            buffer.putInt((Integer)data);
        }
        @Override public Object read(ByteBuffer buffer)
        {
            return buffer.getInt();
        }
    },
    LONG()
    {
        @Override public Long convert(Object data)
                throws FledDataTypeException
        {
            if (!(data instanceof Long))
            {
                throw new IllegalArgumentException(
                        "data must be of instance Integer");
            }
            return (Long) data;
        }
        @Override public boolean indexable()
        {
            return true;
        }
        @Override public int numOfBytes()
        {
            return Long.SIZE / 8;
        }
        @Override public void write(ByteBuffer buffer, Object data)
        {
            buffer.putLong((Long)data);
        }
        @Override public Object read(ByteBuffer buffer)
        {
            return buffer.getLong();
        }
    },
    DATETIME()
    {
        @Override public Long convert(Object data)
            throws FledDataTypeException
        {
            if (!(data instanceof Long))
            {
                throw new IllegalArgumentException(
                        "data must be of instance Long");
            }
            return (Long) data;
        }
        @Override public boolean indexable()
        {
            return true;
        }
        @Override public int numOfBytes()
        {
            return Long.SIZE / 8;
        }
        @Override public void write(ByteBuffer buffer, Object data)
        {
            buffer.putLong((Long)data);
        }
        @Override public Object read(ByteBuffer buffer)
        {
            return buffer.getLong();
        }
    },
    STRING()
    {
        @Override public Long convert(Object data)
            throws FledDataTypeException
        {
            if (!(data instanceof Long))
            {
                throw new IllegalArgumentException(
                        "data must be of instance Integer");
            }
            String str = (String) data;
            MessageDigest md = null;
            try
            {
                md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes("iso-8859-1"), 0, str.length());
            }
            catch(NoSuchAlgorithmException ex)
            {
                
            }
            catch(UnsupportedEncodingException ex)
            {
                
            }
            byte[] md5hash = md.digest();
            Long result = 0L;
            for (int i = 0; i < md5hash.length; i++)
            {
                result = (result << 3);
                result += ((md5hash[i] & 0x0F) + ( (md5hash[i] >>> 4) & 0x0F));
            }
            return result;
        }
        @Override public boolean indexable()
        {
            return true;
        }
        @Override public int numOfBytes()
        {
            return (Character.SIZE / 8) * 128;
        }
        @Override public void write(ByteBuffer buffer, Object data)
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
        @Override public Object read(ByteBuffer buffer)
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
    },
    TEXT()
    {
        @Override public Long convert(Object data)
            throws FledDataTypeException
        {
            throw new UnsupportedOperationException();
        }
        @Override public boolean indexable()
        {
            return false;
        }
        @Override public int numOfBytes()
        {
            return (Character.SIZE / 8) * 65536;
        }
        @Override public void write(ByteBuffer buffer, Object data)
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
        @Override public Object read(ByteBuffer buffer)
        {
            StringBuilder sb = new StringBuilder(128);
            for(int i = 0; i < 65536; i++)
            {
                char get = buffer.getChar();
                if (get != (char)-1)
                {
                    sb.append(get);
                }
            }
            return sb.toString();
        }
    };
    
    public abstract int numOfBytes();

    public abstract boolean indexable();

    public abstract Long convert(Object data) throws FledDataTypeException;

    public abstract void write(ByteBuffer buffer, Object data);

    public abstract Object read(ByteBuffer buffer);
}
