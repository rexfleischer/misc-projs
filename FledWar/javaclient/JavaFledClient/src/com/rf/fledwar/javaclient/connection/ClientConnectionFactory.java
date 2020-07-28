/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rf.fledwar.javaclient.connection.conversation.ConversationProviderClient;
import com.rf.fledwar.javaclient.connection.curators.AttachSessionKey;
import com.rf.fledwar.javaclient.connection.curators.MessageVerbose;
import com.rf.fledwar.javaclient.util.HttpUtil;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationProvider;
import com.rf.fledwar.socket.connection.StateHandlerException;
import com.rf.fledwar.socket.connection.StateHandlerPipeline;
import com.rf.fledwar.socket.util.ConnectionStateHandlers;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class ClientConnectionFactory
{
    protected static Logger logger = Logger.getLogger(ClientConnectionFactory.class);
    
    public static final String DEFAULT_CONFIG = "fledclient.properties";
    
    public static final String PORT_FINDER = "connection.portfinder";
    
    public static final String HOST = "connection.host";
    
    /**
     * 
     * @param autoStart if you want this to call start() on the client
     * thread before returning the ClientConnection
     * @return
     * @throws ClientConfigurationException
     * @throws ClientInitException 
     */
    public static ClientConnection getDefault(boolean autoStart)
            throws ClientConfigurationException, ClientInitException
    {
        Properties properties = new Properties();
        try
        {
            properties.load(ClientConnection.class
                    .getResourceAsStream(DEFAULT_CONFIG));
        }
        catch(IOException ex)
        {
            String message = "could not find default configuration";
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
        return get(properties, autoStart);
    }
    
    /**
     * 
     * @param properties
     * @param autoStart if you want this to call start() on the client
     * thread before returning the ClientConnection
     * @return
     * @throws ClientConfigurationException
     * @throws ClientInitException 
     */
    public static ClientConnection get(Properties properties, boolean autoStart) 
            throws ClientConfigurationException, ClientInitException
    {
        // first, check properties
        if (properties == null)
        {
            String message = "properties cannot be null";
            logger.error(message);
            throw new NullPointerException(message);
        }
        
        // we have to know what port the server is, so we 
        // need to go through all those hoops
        Integer port = getPort(properties);
        
        // now we get the socket to the server
        Socket socket = getSocket(port, properties);
        
        // ready the conversation provider for the work 
        // handlers in the connection
        ConversationProvider provider = getConversationProvider(properties);
        
        // now get the work handlers
        Map<ConnectionState, StateHandlerPipeline> handlers = 
                getStateHandlers(properties);
        
        
        
        // finally we need to put it all together
        ClientConnection result;
        try
        {
            result = new ClientConnection(socket, provider, handlers);
            result.registerWriteCurator(new AttachSessionKey());
            result.registerReadCurator(new MessageVerbose("receiving %s"));
            result.registerWriteCurator(new MessageVerbose("sending %s"));
            if (autoStart)
            {
                result.start();
            }
            return result;
        }
        catch(IOException | StateHandlerException ex)
        {
            String message = "could not init client connection";
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
    }
    
    private static Integer getPort(Properties properties)
            throws ClientConfigurationException, ClientInitException
    {
        String portfinderurl = properties.getProperty(PORT_FINDER);
        if (portfinderurl == null || portfinderurl.isEmpty())
        {
            String message = String.format(
                    "%s must be specified in the propeties file",
                    PORT_FINDER);
            logger.error(message);
            throw new ClientConfigurationException(message);
        }
        
        byte[] jsonbytes;
        try
        {
            jsonbytes = HttpUtil.get(portfinderurl);
        }
        catch(IOException ex)
        {
            String message = "could not get port info";
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
        
        String jsonstring = new String(jsonbytes);
        Map portrequest;
        try
        {
            portrequest = (new Gson()).fromJson(jsonstring, Map.class);
        }
        catch(JsonSyntaxException ex)
        {
            String message = String.format(
                    "invalid message from server [1] [%s]", 
                    jsonstring);
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
        
        if (portrequest.containsKey("error"))
        {
            String message = String.format(
                    "error from server [%s]", 
                    jsonstring);
            logger.error(message);
            throw new ClientInitException(message);
        }
        
        Object portcheck = portrequest.get("port");
        if (!(portcheck instanceof Double))
        {
            String message = String.format(
                    "invalid message from server [2] [%s]", 
                    jsonstring);
            logger.error(message);
            throw new ClientInitException(message);
        }
        
        return ((Double) portcheck).intValue();
    }
    
    private static Socket getSocket(Integer port, Properties properties)
            throws ClientConfigurationException, ClientInitException
    {
        String host = properties.getProperty(HOST);
        if (host == null || host.isEmpty())
        {
            String message = String.format(
                    "%s must be specified in the propeties file",
                    HOST);
            logger.error(message);
            throw new ClientConfigurationException(message);
        }
        
        try
        {
            return new Socket(host, port);
        }
        catch(Exception ex)
        {
            String message = String.format("unable to connect socket "
                                            + "[host:%s, port:%s]", 
                                           host, port);
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
    }
    
    private static ConversationProvider getConversationProvider(
            Properties properties)
            throws ClientConfigurationException, ClientInitException
    {
        try
        {
            return new ConversationProviderClient();
        }
        catch(ConversationException ex)
        {
            String message = "couldnt get conversation provider";
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
    }
    
    private static Map<ConnectionState, StateHandlerPipeline> getStateHandlers(
            Properties properties)
            throws ClientConfigurationException, ClientInitException
    {
        try
        {
            return ConnectionStateHandlers.getHandlers(properties);
        }
        catch(Exception ex)
        {
            String message = "couldnt get conversation state handlers";
            logger.error(message, ex);
            throw new ClientInitException(message, ex);
        }
    }
}
