/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import java.util.Map;
import java.util.Objects;

/**
 *
 * @author REx
 */
public class ConversationInit
{
    public final String name;
    
    public final Map<String, Object> options;

    public ConversationInit(String name, Map<String, Object> options)
    {
        this.name = Objects.requireNonNull(name, "name");
        this.options = options;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
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
        final ConversationInit other = (ConversationInit) obj;
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        return true;
    }
    
}
