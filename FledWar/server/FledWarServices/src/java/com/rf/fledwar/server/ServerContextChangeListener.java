/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server;

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
        catch(Exception ex) { }
    }
    
}
