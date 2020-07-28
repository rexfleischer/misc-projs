/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.user;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author REx
 */
public class SessionId
{
    private static final int CHAR_SIZE = 32;
    
    private static final int BYTE_SIZE = 16;
    
    private byte[] bytes;
    
    public SessionId()
    {
        bytes = new byte[BYTE_SIZE];
        (new Random()).nextBytes(bytes);
    }
    
    public SessionId(byte[] id)
    {
        if (id.length != BYTE_SIZE)
        {
            throw new IllegalArgumentException();
        }
        bytes = Arrays.copyOf(id, BYTE_SIZE);
    }
    
    public SessionId(char[] id)
    {
        if (id.length != CHAR_SIZE)
        {
            throw new IllegalArgumentException();
        }
        bytes = new byte[BYTE_SIZE];
        for(int i = 0, n = CHAR_SIZE; i < n; i+=2)
        {
            byte r = toByte(id[i]);
            byte l = toByte(id[i+1]);
            bytes[ (i>>1) ] = (byte)((r << 4) & 0xf0 | 
                                     (l     ) & 0x0f);
        }
    }
    
    public SessionId(String id)
    {
        this(id.toCharArray());
    }
    
    @Override
    public String toString()
    {
        return new String(toCharArray());
    }
    
    public byte[] toByteArray()
    {
        return Arrays.copyOf(bytes, BYTE_SIZE);
    }
    
    public char[] toCharArray()
    {
        char[] characters = new char[CHAR_SIZE];
        for(int i = 0, n = BYTE_SIZE; i < n; i++)
        {
            byte curr = bytes[i];
            byte r = (byte)((curr >> 4) & 0x0f);
            byte l = (byte)((curr     ) & 0x0f);
            int index = (i << 1);
            
            characters[index  ] = toChar(r);
            characters[index+1] = toChar(l);
        }
        return characters;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 13 * hash + Arrays.hashCode(this.bytes);
        return hash;
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
        final SessionId other = (SessionId) obj;
        if (!Arrays.equals(this.bytes, other.bytes))
        {
            return false;
        }
        return true;
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="converters">
    private byte toByte(char _char)
    {
        if (_char == '0')
        {
            return 0x0;
        }
        if (_char == '1')
        {
            return 0x1;
        }
        if (_char == '2')
        {
            return 0x2;
        }
        if (_char == '3')
        {
            return 0x3;
        }
        if (_char == '4')
        {
            return 0x4;
        }
        if (_char == '5')
        {
            return 0x5;
        }
        if (_char == '6')
        {
            return 0x6;
        }
        if (_char == '7')
        {
            return 0x7;
        }
        if (_char == '8')
        {
            return 0x8;
        }
        if (_char == '9')
        {
            return 0x9;
        }
        if (_char == 'a')
        {
            return 0xa;
        }
        if (_char == 'b')
        {
            return 0xb;
        }
        if (_char == 'c')
        {
            return 0xc;
        }
        if (_char == 'd')
        {
            return 0xd;
        }
        if (_char == 'e')
        {
            return 0xe;
        }
        if (_char == 'f')
        {
            return 0xf;
        }
        
        throw new IllegalArgumentException();
    }
    
    private char toChar(byte _byte)
    {
        if (_byte == 0x0)
        {
            return '0';
        }
        if (_byte == 0x1)
        {
            return '1';
        }
        if (_byte == 0x2)
        {
            return '2';
        }
        if (_byte == 0x3)
        {
            return '3';
        }
        if (_byte == 0x4)
        {
            return '4';
        }
        if (_byte == 0x5)
        {
            return '5';
        }
        if (_byte == 0x6)
        {
            return '6';
        }
        if (_byte == 0x7)
        {
            return '7';
        }
        if (_byte == 0x8)
        {
            return '8';
        }
        if (_byte == 0x9)
        {
            return '9';
        }
        if (_byte == 0xa)
        {
            return 'a';
        }
        if (_byte == 0xb)
        {
            return 'b';
        }
        if (_byte == 0xc)
        {
            return 'c';
        }
        if (_byte == 0xd)
        {
            return 'd';
        }
        if (_byte == 0xe)
        {
            return 'e';
        }
        if (_byte == 0xf)
        {
            return 'f';
        }
        
        throw new IllegalArgumentException();
    }
    //</editor-fold>
}
