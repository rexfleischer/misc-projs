/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class FledPropertiesFactory 
{
    private static Map<String, FledProperties> properties;
    
    static 
    {
        properties = new HashMap<String, FledProperties>();
    }
    
    public static FledProperties getProperties(String set) 
            throws IOException
    {
        FledProperties result = null;
        synchronized(FledPropertiesFactory.class)
        {
            result = properties.get(set);
            if (result == null)
            {
                result = new FledProperties();
                result.load(FledPropertiesFactory.class.getResourceAsStream(set));
                properties.put(set, result);
            }
        }
        return result;
    }
    
    public static FledProperties getProperties() 
            throws IOException
    {
        return getProperties("default");
    }
}
