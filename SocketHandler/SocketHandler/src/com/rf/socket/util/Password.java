/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author REx
 */
public class Password implements Serializable
{
    public static final int BYTE_COUNT = 64;
    
    private final byte[] bytes;
    
    public Password()
    {
        this.bytes = generate();
    }
    
    public Password(byte[] bytes)
    {
        this.bytes = bytes;
    }
    
    private byte[] generate()
    {
        byte[] result = new byte[BYTE_COUNT];
        (new Random()).nextBytes(result);
        return result;
    }
    
    public byte[] getBytes()
    {
        byte[] result = new byte[BYTE_COUNT];
        System.arraycopy(this.bytes, 0, result, 0, BYTE_COUNT);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Password other = (Password) obj;
        if (!Arrays.equals(this.bytes, other.bytes))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + Arrays.hashCode(this.bytes);
        return hash;
    }
}
