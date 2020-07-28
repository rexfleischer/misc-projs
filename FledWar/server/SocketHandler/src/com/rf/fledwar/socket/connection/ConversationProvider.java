/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author REx
 */
public abstract class ConversationProvider
{
    private Map<String, Constructor<? extends Conversation>> conversations;

    /**
     * 
     */
    public ConversationProvider()
    {
        conversations = new HashMap<>();
    }
    
    /**
     * 
     * @param clazzes 
     */
    public ConversationProvider(Map<String, Class<? extends Conversation>> clazzes)
            throws ConversationException
    {
        Iterator<String> it = clazzes.keySet().iterator();
        while(it.hasNext())
        {
            String name = it.next();
            Class<? extends Conversation> clazz = clazzes.get(name);
            registerConversation(name, clazz);
        }
    }
    
    /**
     * 
     * @param name
     * @param clazz
     * @throws ConversationException 
     */
    public final void registerConversation(String name, Class<? extends Conversation> clazz)
            throws ConversationException
    {
        Constructor<? extends Conversation> constructor;
        try
        {
            constructor = clazz.getConstructor();
        }
        catch(NoSuchMethodException | SecurityException ex)
        {
            throw new ConversationException(
                    "could not get default constructor", 
                    ex);
        }
        
        conversations.put(name, constructor);
    }

    /**
     * 
     * @param name
     * @return
     * @throws ConversationException 
     */
    public Conversation provide(String name)
            throws ConversationException
    {
        Constructor<? extends Conversation> constructor = conversations.get(name);
        if (constructor == null)
        {
            return null;
        }
        
        try
        {
            return constructor.newInstance();
        }
        catch(IllegalAccessException | 
              IllegalArgumentException | 
              InstantiationException | 
              InvocationTargetException ex)
        {
            throw new ConversationException(
                    "could not create new instance of conversation", 
                    ex);
        }
    }
    
}
