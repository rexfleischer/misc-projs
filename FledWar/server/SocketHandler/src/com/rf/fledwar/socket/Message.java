/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author REx
 */
public class Message
{
    public static final String PROTOCOL_NAME = "name";
    
    public static final String PROTOCOL_KEY = "key";
    
    private Map data;
    
    /**
     * for going to the client when we have to build the message
     * from the ground up.
     */
    public Message()
    {
        this.data = new LinkedHashMap();
    }
    
    /**
     * for going to the client
     * @param message 
     */
    Message(Map data)
    {
        this.data = data;
    }
    
    /**
     * for going to the client when we know all of the
     * information up front.
     * @param protocol
     * @param key
     * @param data 
     */
    Message(String protocol, UUID key, Map data)
    {
        this.data = new LinkedHashMap(data);
        this.setProtocol(protocol);
        this.setSessionKey(key);
    }
    
    public final UUID getSessionKey()
    {
        UUID result;
        Object check = data.get(PROTOCOL_KEY);
        if (check instanceof String)
        {
            result = UUID.fromString((String) check);
        }
        else
        {
            result = (UUID) check;
        }
        return result;
    }
    
    public final String getName()
    {
        return (String) data.get(PROTOCOL_NAME);
    }
    
    public final void setSessionKey(UUID key)
    {
        Objects.requireNonNull(key, PROTOCOL_KEY);
        data.put(PROTOCOL_KEY, key);
    }
    
    public final void setProtocol(String protocol)
    {
        Objects.requireNonNull(protocol, PROTOCOL_NAME);
        data.put(PROTOCOL_NAME, protocol);
    }
    
    public final Map getData()
    {
        return data;
    }
    
    public final Object getValue(Object key)
    {
        return data.get(key);
    }
    
    public final void putValue(Object key, Object value)
    {
        data.put(key, value);
    }
    
    @Override
    public String toString()
    {
        return data.toString();
    }
}
