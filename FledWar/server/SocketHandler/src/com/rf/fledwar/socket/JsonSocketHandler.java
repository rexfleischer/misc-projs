/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class JsonSocketHandler
{
    private final static Gson gson = new Gson();
    
    private final static char START = '{';
    
    private final static char END = '}';
    
    /**
     * 
     */
    private String beforeBuffer;
    
    /**
     * a queue of reads
     */
    private LinkedList<Message> reads;
    
    /**
     * the output stream of the socket
     */
    private OutputStream output;
    
    /**
     * the input stream of the socket
     */
    private InputStream input;
    
    /**
     * the socket being wrapped 
     */
    private Socket socket;
    
    /**
     * for wrapping an already created socket
     * @param wrapping
     * @throws IOException 
     */
    public JsonSocketHandler(Socket wrapping) throws IOException
    {
        this.socket = wrapping;
        this.output = wrapping.getOutputStream();
        this.input  = wrapping.getInputStream();
        this.reads  = new LinkedList<>();
        
        socket.setKeepAlive(true);
        socket.setSoTimeout(100);
        socket.setReceiveBufferSize(32 * 1024);
    }
    
    /**
     * for making a this class handle the socket creation
     * @param host
     * @param port
     * @throws IOException 
     */
    public JsonSocketHandler(String host, int port) throws IOException
    {
        this(new Socket(host, port));
    }
    
    /**
     * gets the underline socket
     * @return 
     */
    public Socket getSocket()
    {
        return socket;
    }
    
    /**
     * closes the streams and the socket
     * @throws IOException 
     */
    public void close() throws IOException
    {
        input.close();
        output.close();
        socket.close();
    }
    
    /**
     * sends the map as bytes serialized into json
     * @param json
     * @throws IOException 
     */
    public void write(Message message) throws IOException
    {
        byte[] rawbytes = gson.toJson(message.getData()).getBytes();
        output.write(rawbytes);
        output.flush();
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    public Message read() throws IOException
    {
        readSocket();
        
        return (!reads.isEmpty()) 
                ? reads.remove() 
                : null;
    }
    
    private void readSocket() throws IOException
    {
        byte[] bytes = readFully();
        
        if (bytes.length == 0)
        {
            return;
        }
        
        /**
         * because a socket will just buffer the bytes
         * end to end, we need to check the strings and 
         * split them up. but because we know a Map
         * is going to and from the socket, then we can
         * assume the string will be separated by {} 
         */
        String rawstring = new String(bytes);
        if (beforeBuffer != null)
        {
            rawstring = (beforeBuffer + rawstring);
            beforeBuffer = null;
        }
        
        int level = 0;
        int start = -1;
        for(int i = 0; i < rawstring.length(); i++)
        {
            char character = rawstring.charAt(i);
            
            if (character == START)
            {
                if (level == 0)
                {
                    start = i;
                }
                level++;
            }
            else if (character == END)
            {
                level--;
                if (level == 0)
                {
                    try
                    {
                        String jsonpart = rawstring.substring(start, i+1);
                        Map json = gson.fromJson(jsonpart, Map.class);
                        Message message = new Message(json);
                        reads.offerLast(message);
                        start = -1;
                    }
                    catch(JsonSyntaxException ex)
                    {
                        throw new IOException(
                                String.format("invalid communication format [%s]", 
                                              rawstring));
                    }
                }
                else if (level < 0)
                {
                    throw new IOException(
                            String.format("invalid communication format [%s]", 
                                          rawstring));
                }
            }
        }
        
        // check to see if we have any leftover so we can 
        // just prepend that to the next one
        if (start != -1)
        {
            beforeBuffer = new String(rawstring.substring(start));
        }
    }
    
    private byte[] readFully() throws IOException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        
        try
        {
            int length;
            do
            {
                length = input.read(buffer);
                if (length > 0)
                {
                    bytes.write(buffer, 0, length);
                }
            }
            while(length == buffer.length);
        }
        catch(SocketTimeoutException ex)
        {
            // these are ok
        }
        
        return bytes.toByteArray();
    }
}
