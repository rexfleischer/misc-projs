/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author REx
 */
public class FileSerializer
{
    public void serialize(String filename, Object data)
            throws IOException
    {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try
        {
            fileOut = new FileOutputStream(filename);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
        }
        finally
        {
            out.close();
            fileOut.close();
        }   
    }

    public Object unserialize(String filename)
            throws  FileNotFoundException,
                    IOException,
                    ClassNotFoundException
    {
        Object result = null;
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try
        {
            fileIn = new FileInputStream(filename);
            in = new ObjectInputStream(fileIn);
            result = in.readObject();
        }
        finally
        {
            if (fileIn != null)
                fileIn.close();
            if (in != null)
                in.close();
        }
        return result;
    }
}
