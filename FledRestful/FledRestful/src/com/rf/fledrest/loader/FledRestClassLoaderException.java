/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.loader;

/**
 *
 * @author REx
 */
public class FledRestClassLoaderException extends Exception
{

    public FledRestClassLoaderException(Throwable cause)
    {
        super(cause);
    }

    public FledRestClassLoaderException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FledRestClassLoaderException(String message)
    {
        super(message);
    }

    public FledRestClassLoaderException()
    {
    }
    
}
