/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.filemanager;

import com.rf.fled.util.StreamSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        ByteBuffer result = ByteBuffer.allocate((int)channel.size());
        channel.read(result);
        result.position(0);
        return result;
    }
    
    public static Object deserializeObject(String filename)
            throws IOException, ClassNotFoundException
    {
        FileChannel channel = (FileChannel)
                Files.newByteChannel(Paths.get(filename));
        ByteBuffer result = ByteBuffer.allocate((int)channel.size());
        channel.read(result);
        result.position(0);
        
        byte[] bytes = new byte[result.capacity()];
        result.get(bytes);
        ByteArrayInputStream inData =  new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(inData);
        return in.readObject();
    }
    
    public static void serialize(String filename, ByteBuffer data) 
            throws IOException
    {
        data.position(0);
        FileChannel channel = (FileChannel) Files.newByteChannel(
                    Paths.get(filename), 
                    StandardOpenOption.WRITE, 
                    StandardOpenOption.TRUNCATE_EXISTING, 
                    StandardOpenOption.CREATE);
        channel.write(data);
        channel.close();
    }
    
    public static void serialize(String filename, Object data)
            throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamSerializer.serialize(out, data);
        ByteBuffer buffer = ByteBuffer.allocate(out.size());
        buffer.put(out.toByteArray());
        buffer.position(0);
        FileChannel channel = (FileChannel) Files.newByteChannel(
                    Paths.get(filename), 
                    StandardOpenOption.WRITE, 
                    StandardOpenOption.TRUNCATE_EXISTING, 
                    StandardOpenOption.CREATE);
        channel.write(buffer);
        channel.close();
    }
}
