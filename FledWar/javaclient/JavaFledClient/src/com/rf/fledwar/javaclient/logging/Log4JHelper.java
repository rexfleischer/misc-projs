/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.logging;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author REx
 */
public class Log4JHelper
{
    public static final String CONFIG_LOG4J = "log4j.properties";
    
    public static void startLogger(Properties log4j)
    {
        PropertyConfigurator.configure(log4j);
    }
    
    public static void startLogger() throws IOException
    {
        Properties log4j = new Properties();
        log4j.load(Log4JHelper.class.getResourceAsStream(CONFIG_LOG4J));
        PropertyConfigurator.configure(log4j);
    }
}
