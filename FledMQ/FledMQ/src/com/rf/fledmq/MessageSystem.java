/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

import java.util.Map;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class MessageSystem
{
    public static final String PROPERTIES_FORMAT = "fledmq.%s.%s";
    
    public static final String CLASS = "class";
    
    private static MessageQueueFactory factory;
    
    public static void init(Properties properties, String name)
            throws MessagingException
    {
        init(properties, name, null);
    }
    
    public static void init(Properties properties, 
                            String name, 
                            Map<String, Object> hints) 
            throws MessagingException
    {
        isNullCheck();
        
        String clazz = Util.getProperty(properties, 
                                        PROPERTIES_FORMAT, 
                                        name, 
                                        CLASS);
        Object instance = null;
        try
        {
            instance = Util.createInstance(clazz);
        }
        catch(Exception ex)
        {
            throw new MessagingException(
                    String.format("could not create instance of %s", clazz), 
                    ex);
        }
        if (!(instance instanceof MessageQueueFactory))
        {
            throw new MessagingException(
                    String.format("%s must be an instance "
                                  + "of MessageQueueFactory", 
                                  clazz));
        }
        factory = (MessageQueueFactory) instance;
        factory.init(properties, name, hints);
    }
    
    public static void destroy()
            throws MessagingException
    {
        notNullCheck();
        factory.destroy();
        factory = null;
    }
    
    public static void finished()
            throws MessagingException
    {
        notNullCheck();
        factory.finished();
        factory = null;
    }
    
    public static MessageQueue getQueue(String queue) 
            throws MessagingException
    {
        notNullCheck();
        return factory.factory(queue);
    }
    
    private static void notNullCheck()
            throws MessagingException
    {
        if (factory == null)
        {
            throw new MessagingException("message system not initiated");
        }
    }
    
    private static void isNullCheck()
            throws MessagingException
    {
        if (factory != null)
        {
            throw new MessagingException("message system already initiated");
        }
    }
}
