
package com.rf.fled.persistence.util;

import com.rf.fled.persistence.ISerializer;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class LongSerializer implements ISerializer<byte[]>
{
    @Override
    public Object deserialize(byte[] buffer) 
            throws IOException 
    {
        if (buffer.length != 8)
        {
            throw new IOException("buffer is corrupt");
        }
        return
            (((long)(buffer[0] & 0xff) << 56) |
             ((long)(buffer[1] & 0xff) << 48) |
             ((long)(buffer[2] & 0xff) << 40) |
             ((long)(buffer[3] & 0xff) << 32) |
             ((long)(buffer[4] & 0xff) << 24) |
             ((long)(buffer[5] & 0xff) << 16) |
             ((long)(buffer[6] & 0xff) <<  8) |
             ((long)(buffer[7] & 0xff)));
    }

    @Override
    public byte[] serialize(Object obj) 
            throws IOException 
    {
        long src = (Long) obj;
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
}
