/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.cluster;

import com.rf.socket.util.Password;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author REx
 */
public class Message 
{
    public final Password password;
    
    public final byte[] message;
    
    public final int command;

    public Message(byte[] bytes)
    {
        byte[] pass = new byte[Password.BYTE_COUNT];
        System.arraycopy(bytes, 0, pass, 0, Password.BYTE_COUNT);
        this.password = new Password(pass);
        
        command = 
                (bytes[Password.BYTE_COUNT    ] << 24) & 0xff |
                (bytes[Password.BYTE_COUNT    ] << 16) & 0xff |
                (bytes[Password.BYTE_COUNT    ] <<  8) & 0xff |
                (bytes[Password.BYTE_COUNT    ]      ) & 0xff;
        
        int length = bytes.length - Password.BYTE_COUNT - 4;
        message = new byte[length];
        System.arraycopy(bytes, Password.BYTE_COUNT + 4, message, 0, length);
    }
    
    public Message(Password password, int command, byte[] message)
    {
        this.password = password;
        this.command = command;
        this.message = message;
    }
    
    public byte[] getBytes() throws IOException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        
        byte[] pass = password.getBytes();
        bytes.write(pass, 0, pass.length);
        
        bytes.write((command >> 24) & 0xff);
        bytes.write((command >> 16) & 0xff);
        bytes.write((command >>  8) & 0xff);
        bytes.write((command      ) & 0xff);
        
        bytes.write(message);
        
        return bytes.toByteArray();
    }
}
