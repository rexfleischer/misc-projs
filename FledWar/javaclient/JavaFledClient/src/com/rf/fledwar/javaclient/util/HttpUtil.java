/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author REx
 */
public class HttpUtil
{
    public static byte[] get(String url) throws MalformedURLException, IOException
    {
        URL dest = new URL(url);
        
        try(InputStream in = dest.openConnection().getInputStream())
        {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = in.read(data, 0, data.length)) != -1) 
            {
                buffer.write(data, 0, nRead);
            }
            
            return buffer.toByteArray();
        }
    }
}
