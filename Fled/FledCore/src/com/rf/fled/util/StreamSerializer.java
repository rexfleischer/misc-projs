/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.fled.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author REx
 */
public class StreamSerializer
{
    public static void serialize(OutputStream output, Object data)
            throws IOException
    {
        ObjectOutputStream out = null;
        try
        {
            out = new ObjectOutputStream(output);
            out.writeObject(data);
        }
        finally
        {
            if (out != null)
                out.close();
        }   
    }

    public static Object deserialize(InputStream input)
            throws  FileNotFoundException,
                    IOException,
                    ClassNotFoundException
    {
        Object result = null;
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(input);
            result = in.readObject();
        }
        finally
        {
            if (in != null)
                in.close();
        }
        return result;
    }
}
