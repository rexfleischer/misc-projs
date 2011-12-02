/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author REx
 */
public class ByteSerializer 
{
    public static byte[] serialize(Object data)
            throws IOException
    {
        ObjectOutputStream out = null;
        ByteArrayOutputStream bytes = null;
        try
        {
            bytes = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bytes);
            out.writeObject(data);
        }
        finally
        {
            if (out != null)
                out.close();
            if (bytes != null)
                bytes.close();
        }   
        return bytes.toByteArray();
    }
    
    public static Object deserialize(byte[] data) 
            throws IOException, ClassNotFoundException
    {
        Object result = null;
        ObjectInputStream in = null;
        ByteArrayInputStream input = null;
        try
        {
            input = new ByteArrayInputStream(data);
            in = new ObjectInputStream(input);
            result = in.readObject();
        }
        finally
        {
            if (in != null)
                in.close();
            if (input != null)
                input.close();
        }
        return result;
    }
}
