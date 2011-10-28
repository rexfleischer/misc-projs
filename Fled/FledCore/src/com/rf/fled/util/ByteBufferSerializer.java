/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author REx
 */
public class ByteBufferSerializer 
{
    public static ByteBuffer deserialize(String filename) 
            throws IOException
    {
        FileChannel channel = (FileChannel)
                Files.newByteChannel(Paths.get(filename));
        return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
    }
    
    public static void serialize(String filename, ByteBuffer data) 
            throws IOException
    {
        FileChannel channel = (FileChannel) Files.newByteChannel(
                    Paths.get(filename), 
                    StandardOpenOption.WRITE, 
                    StandardOpenOption.READ, 
                    StandardOpenOption.CREATE);
        channel.write(data);
    }
}
