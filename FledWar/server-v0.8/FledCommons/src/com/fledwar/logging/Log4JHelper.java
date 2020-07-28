/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.logging;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author REx
 */
public class Log4JHelper
{
    public static final String DEFAULT_LOG4J = "log4j.default.properties";
    
    public static void startDefaultLogger()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(Log4JHelper.class.getResourceAsStream(DEFAULT_LOG4J));
            PropertyConfigurator.configure(properties);
        }
        catch(IOException ex)
        {
            throw new IllegalStateException("some stupid stuff went down", ex);
        }
    }
    
    public static void startLogger(Properties log4jprops)
    {
        PropertyConfigurator.configure(log4jprops);
    }
}
