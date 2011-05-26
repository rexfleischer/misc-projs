/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.memory.persistence.interfaces;

import java.io.InputStream;

/**
 *
 * @author REx
 */
public abstract class IInputStream
{
    protected InputStream input;

    protected String context;

    protected Exception exception;
    
    public InputStream getInputStream()
    {
        return input;
    }

    public String getContext()
    {
        return context;
    }

    public boolean isError()
    {
        return exception != null;
    }

    public Exception getError()
    {
        return exception;
    }
}
