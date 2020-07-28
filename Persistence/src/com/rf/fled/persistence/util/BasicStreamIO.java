
package com.rf.fled.persistence.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author REx
 */
public class BasicStreamIO 
{
    public static void objectToStream(Object object, OutputStream output) 
            throws IOException
    {
        BufferedOutputStream buf = null;
        ObjectOutputStream out = null;
        try
        {
            buf = new BufferedOutputStream(output);
            out = new ObjectOutputStream(buf);
            out.writeObject(object);
        }
        finally
        {
            if (buf != null)
            {
                buf.close();
            }
            if (out != null)
            {
                out.close();
            }
        }  
    }
    
    public static Object streamToObject(InputStream input) 
            throws IOException, ClassNotFoundException
    {
        
        Object result = null;
        ObjectInputStream in = null;
        BufferedInputStream buf = null;
        try
        {
            buf = new BufferedInputStream(input);
            in = new ObjectInputStream(buf);
            result = in.readObject();
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (buf != null)
            {
                buf.close();
            }
        }
        return result;
    }
}
