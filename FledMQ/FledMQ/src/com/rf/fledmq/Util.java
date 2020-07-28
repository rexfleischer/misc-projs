/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledmq;

import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 *
 * @author REx
 */
class Util
{
    static String getProperty(Properties properties, String format, Object ... args)
            throws MessagingException
    {
        String key = String.format(format, args);
        String result = properties.getProperty(key);
        if (result == null)
        {
            throw new MessagingException(
                    String.format("%s config must be specified", key));
        }
        return result;
    }
    
    static int currentSTime()
    {
        return (int)(System.currentTimeMillis() * 0.001);
    }
    
    static Object createInstance(String clazz) throws Exception
    {
        Class actualClass = Class.forName(clazz);
        Constructor constructor = actualClass.getConstructor();
        return constructor.newInstance();
    }
}
