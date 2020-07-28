/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author REx
 */
public class Password
{
    private static final int SIZE = 16;
    
    private static final MessageDigest digest;
    
    private static final int SALT_LENGTH = 3;
    
    private static final String SALT_CHARS = 
              "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789";
    
    private static final int RANDOM_RANGE = SALT_CHARS.length();
    
    static 
    {
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static String genrateNewSalt()
    {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        while(result.length() < SALT_LENGTH)
        {
            char next = SALT_CHARS.charAt(random.nextInt(RANDOM_RANGE));
            result.append(next);
        }
        return result.toString();
    }
    
    private byte[] bytes;
    
    /**
     * creates a random password... use this if you dont 
     * want to be able to use the pass ever again
     */
    public Password()
    {
        bytes = new byte[SIZE];
        (new Random()).nextBytes(bytes);
    }
    
    public Password(byte[] bytes)
    {
        if (bytes.length != SIZE)
        {
            throw new IllegalArgumentException();
        }
        this.bytes = bytes;
    }
    
    public Password(String password, String salt)
    {
        synchronized(digest)
        {
            bytes = digest.digest((password + salt).getBytes());
        }
    }
    
    public byte[] toByteArray()
    {
        return Arrays.copyOf(bytes, SIZE);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Arrays.hashCode(this.bytes);
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
        final Password other = (Password) obj;
        if (!Arrays.equals(this.bytes, other.bytes))
        {
            return false;
        }
        return true;
    }
    
}
