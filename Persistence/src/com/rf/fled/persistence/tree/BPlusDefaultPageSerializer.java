/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.tree;

import com.rf.fled.persistence.ISerializer;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class BPlusDefaultPageSerializer implements ISerializer<byte[]>
{
    @Override
    public Object deserialize(byte[] buffer) 
            throws IOException 
    {
        return new BPlusPage(buffer);
    }

    @Override
    public byte[] serialize(Object obj) 
            throws IOException
    {
        BPlusPage page = (BPlusPage) obj;
        return page.bytes.getBytes();
    }
}