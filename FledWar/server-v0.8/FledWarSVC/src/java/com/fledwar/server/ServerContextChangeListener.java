/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author REx
 */
public class ServerContextChangeListener implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        try
        {
            FledWarServer.start();
        }
        catch(Exception ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            FledWarServer.shutdown();
        }
        catch(Exception ex)
        {
            Writer writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));
            System.err.println(writer.toString());
        }
    }
    
}
