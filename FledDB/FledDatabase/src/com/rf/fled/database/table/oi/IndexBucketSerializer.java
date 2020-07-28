/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.database.table.oi;

import com.rf.fled.persistence.ISerializer;
import com.rf.fled.persistence.util.LongList;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class IndexBucketSerializer implements ISerializer<byte[]>
{
    @Override
    public Object deserialize(byte[] buffer) throws IOException 
    {
        return new LongList(buffer);
    }

    @Override
    public byte[] serialize(Object obj) throws IOException 
    {
        return ((LongList) obj).copyUsedBytes();
    }
}
