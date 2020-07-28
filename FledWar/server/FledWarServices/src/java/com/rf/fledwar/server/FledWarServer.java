/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server;

import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.socket.connection.Connection;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.ConversationProvider;
import com.rf.fledwar.socket.connection.StateHandlerPipeline;
import com.rf.fledwar.socket.server.ServerSocketListener;
import com.rf.fledwar.socket.util.ConnectionStateHandlers;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author REx
 */
public class FledWarServer
{
    public static final String FLED_WAR_PROPERTIES = "FLEDWAR_PROPERTIES";
    
    public static final String FLED_WAR_DEFAULT_RESOURCE = "/com/rf/fledwar/server/fledwar.properties";
    
    public static final String LOG4J_PROPERTIES = "FLEDWAR_LOG4J";
    
    public static final String LOG4J_DEFAULT_RESOURCE = "/com/rf/fledwar/server/log4j.properties";
    
    public static final String GALAXYMODEL_PROPERTIES = "FLEDWAR_GM";
    
    public static final String GALAXYMODEL_DEFAULT_RESOURCE = "/com/rf/fledwar/server/galaxymodel.properties";
    
    public static final String START_LISTENER = "start.listener";
    
    public static final String LISTENER_PORT = "listener.port";
    
    public static final String LISTENER_CONVERATION_PROVIDER = "listener.conversation_provider";
    
    public static final String LISTENER_CONNECTION_HANDLER = "listener.connection.handler";
    
    private static ServerSocketListener listener = null;
    
    private static Properties config = null;
    
    private static boolean started = false;
    
    public static void start() throws Exception
    {
        Properties system = findProps(FLED_WAR_PROPERTIES, 
                                      FLED_WAR_DEFAULT_RESOURCE);
        
        Properties galaxymodel = findProps(GALAXYMODEL_PROPERTIES, 
                                           GALAXYMODEL_DEFAULT_RESOURCE);
        
        Properties log4j = findProps(LOG4J_PROPERTIES, 
                                     LOG4J_DEFAULT_RESOURCE);
        
        start(system, galaxymodel, log4j);
    }
    
    public static Properties findProps(String envkey, String defaultresource) 
            throws Exception
    {
        Properties starting = new Properties();
        String propertiesfile = System.getenv(envkey);
        if (propertiesfile == null)
        {
            InputStream stream = FledWarServer.class
                    .getResourceAsStream(defaultresource);
            if (stream == null)
            {
                throw new FledWarServerException(
                        "unable to find starting properties");
            }
            starting.load(stream);
        }
        else
        {
            starting.load(new FileInputStream(propertiesfile));
        }
        return starting;
    }
    
    public static void start(Properties config, 
                             Properties galaxymodel, 
                             Properties log4j) throws Exception
    {
        startLogger(log4j);
        
        Logger logger = Logger.getLogger(FledWarServer.class);
        logger.info("starting fledwar application");
        
        FledWarServer.config = config;
        
        Boolean startListener = Boolean.parseBoolean(
                config.getProperty(START_LISTENER, "false"));
        logger.info(String.format("string listener: %s", startListener));
        
        try
        {
            GalaxyModelEngine.start(galaxymodel);
            
            if (startListener)
            {
                Integer port = Integer.parseInt(config.getProperty(LISTENER_PORT));
                String rawFinder = config.getProperty(LISTENER_CONVERATION_PROVIDER);
                String rawHandler = config.getProperty(LISTENER_CONNECTION_HANDLER);
                
                ConversationProvider provider = (ConversationProvider) Class
                        .forName(rawFinder)
                        .getConstructor()
                        .newInstance();
                
                Class<? extends Connection> handler = 
                        (Class<? extends Connection>) Class.forName(rawHandler);
                
                Map<ConnectionState, StateHandlerPipeline> handlers =
                        ConnectionStateHandlers.getHandlers(config);
                
                startListener(port, handler, provider, handlers);
            }
        }
        catch(Exception ex)
        {
            try
            {
                shutdown();
            }
            catch(Exception OMG)
            {
                logger.error("could not shutdown application after error during startup", OMG);
            }
            
            logger.error("could not start application", ex);
            throw ex;
        }
        
        started = true;
    }
    
    public static void shutdown() throws Exception
    {
        if (listener != null)
        {
            listener.shutdown();
            listener.join();
        }
        listener = null;
        
        GalaxyModelEngine.shutdown();
        
        started = false;
    }
    
    public static boolean isStarted()
    {
        return started;
    }
    
    public static void startListener(Integer port, 
                                     Class<? extends Connection> connection,
                                     ConversationProvider provider, 
                                     Map<ConnectionState, StateHandlerPipeline> handlers) 
            throws Exception
    {
        listener = new ServerSocketListener(
                port, 
                connection, 
                ConversationProvider.class, 
                Map.class);
        listener.setProviderParams(provider, handlers);
        listener.start();
    }
    
    public static void startLogger(Properties log4jprops)
    {
        PropertyConfigurator.configure(log4jprops);
    }
    
    public static ServerSocketListener getServerSocketListener()
    {
        return listener;
    }
    
    public static Properties getProperties()
    {
        return config;
    }
}
