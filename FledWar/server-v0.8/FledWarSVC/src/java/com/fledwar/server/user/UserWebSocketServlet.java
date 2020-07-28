/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.user;

import com.fledwar.configuration.Configuration;
import com.fledwar.server.FledWarServer;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class UserWebSocketServlet extends WebSocketServlet
{
    private static final Logger logger = Logger
            .getLogger(UserWebSocketServlet.class);
    
    public static final String CONFIG_KEY = "websocket";
    
    public static final String ACTIVE = "active";
    
    public static final String POOL_SIZE = "pool_size";
    
    public static final String POOL_DELAY = "delay";
    
    public static final String POOL_DELAY_INIT = "delay_init";
    
    public static final String POOL_GROUP = "ws_group";
    
    public static final String CONN_INIT_SCRIPT = "conn_init";
    
    boolean active;
    
    String conn_init;
    
    ScheduledThreadPoolExecutor thread_pool;
    
    Configuration websocket_config;
    
    int thread_pool_delay;
    
    int thread_pool_delay_init;
    
    @Override
    public void init() throws ServletException
    {
        super.init();
        
        logger.info("starting web socket servlet");
        try
        {
            websocket_config = FledWarServer.getConfiguration()
                    .getAsConfiguration(CONFIG_KEY);
        }
        catch(IOException ex)
        {
            throw new ServletException(ex);
        }
        
        active = websocket_config.getAsBoolean(ACTIVE);
        
        if (active)
        {
            int pool_size = websocket_config.getAsInteger(POOL_SIZE);
            logger.info(String.format("pool_size -> %s", pool_size));
            
            thread_pool_delay = websocket_config
                    .getAsInteger(POOL_DELAY);
            thread_pool_delay_init = websocket_config
                    .getAsInteger(POOL_DELAY_INIT);

            thread_pool = new ScheduledThreadPoolExecutor(pool_size);
            
            conn_init = websocket_config.getAsString(CONN_INIT_SCRIPT);
        }
        else
        {
            logger.info("nevermind, web socket servlet not active");
        }
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        
        thread_pool.shutdown();
    }
    

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, 
                                                   HttpServletRequest hsr)
    {
        if (!active)
        {
            logger.warn(String.format("attempted connection [%s]", 
                                      hsr.getRemoteAddr()));
            return null;
        }
        return new WSConnectionHandler(hsr, this);
    }
    
}
