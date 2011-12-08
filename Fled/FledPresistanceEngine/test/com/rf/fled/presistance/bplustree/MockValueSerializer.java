/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.persistence.Serializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author REx
 */
public class MockValueSerializer implements Serializer<byte[]>
{
    @Override
    public Object deserialize(byte[] buffer) 
            throws IOException 
    {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer);
        ObjectInputStream in = new ObjectInputStream(byteIn);
        try {
            MockValue mock = (MockValue) in.readObject();
            byteIn.close();
            in.close();
            return mock;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    @Override
    public byte[] serialize(Object obj)
            throws IOException 
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);

        MockValue mock = (MockValue) obj;

        out.writeObject(mock);
        out.close();
        byteOut.close();

        return byteOut.toByteArray();
    }
}
