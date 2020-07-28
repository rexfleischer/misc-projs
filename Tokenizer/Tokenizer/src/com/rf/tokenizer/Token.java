/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

import java.util.Objects;

/**
 *
 * @author REx
 */
public class Token
{
    public final String type;
    
    public final String matched;
    
    public final int start;

    public Token(String type, String matched, int start)
    {
        this.type       = type;
        this.matched    = matched;
        this.start      = start;
    }
    
    @Override
    public String toString()
    {
        return String.format("[type: %s, matched: %s, index: %s]", 
                             type, matched, start);
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
        final Token other = (Token) obj;
        if (!Objects.equals(this.type, other.type))
        {
            return false;
        }
        if (!Objects.equals(this.matched, other.matched))
        {
            return false;
        }
        if (this.start != other.start)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.matched);
        hash = 37 * hash + this.start;
        return hash;
    }
}
