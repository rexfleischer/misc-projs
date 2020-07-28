/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author REx
 */
enum FileLineReaderFactory
{
    GZ()
    {
        @Override
        public BufferedReader getBufferedReader(String file)
            throws IOException
        {
            InputStream stream = discoverInputStream(file);
            InputStream gzipStream = new GZIPInputStream(stream);
            Reader decoder = new InputStreamReader(gzipStream);
            return new BufferedReader(decoder);
        }
    };
    
    public abstract BufferedReader getBufferedReader(String file)
            throws IOException;
    
    private static InputStream discoverInputStream(String file)
            throws FileNotFoundException
    {
        File check = new File(file);
        
        if (check.isFile())
        {
            return new FileInputStream(check);
        }
        
        throw new IllegalArgumentException(file + " is not discoverable");
    }
    
    public static FileLineReaderFactory discover(String file)
    {
        String ext = file.substring(file.lastIndexOf(".") + 1).toUpperCase();
        return valueOf(ext);
    }
}
