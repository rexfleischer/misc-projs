/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import com.rf.fledwar.socket.util.SafeYieldingThread;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class Connection extends SafeYieldingThread
{
    protected static Logger logger = Logger.getLogger(Connection.class);
    
    private ConnectionState state;
    
    protected final ConnectionLiaison data;
    
    private Map<ConnectionState, StateHandlerPipeline> handlers;

    public Connection(Socket socket, 
                      ConversationProvider provider, 
                      Map<ConnectionState, StateHandlerPipeline> handlers) 
            throws IOException, StateHandlerException
    {
        super(logger);
        this.state      = ConnectionState.OPEN;
        this.data       = new ConnectionLiaison(socket, provider, logger);
        this.handlers   = handlers;
        
        if (!handlers.containsKey(ConnectionState.CLOSE) ||
            !handlers.containsKey(ConnectionState.ERROR) ||
            !handlers.containsKey(ConnectionState.OPEN ) ||
            !handlers.containsKey(ConnectionState.WORK ) )
        {
            signalStop();
            String message = "not all of the required pipelines "
                    + "were in the handlers map";
            logger.fatal(message);
            throw new StateHandlerException(message);
        }
    }

    /**
     * 
     */
    @Override
    public void signalStop()
    {
        logger.debug("closing connection");
        try
        {
            data.socket.close();
        }
        catch(IOException ex)
        {
            logger.error("error while closing socket", ex);
        }
        super.signalStop();
    }
    
    /**
     * this attempts to let the Connection know to
     * initiate this conversation 
     * @param name
     * @return 
     */
    public boolean initConversation(String name,
                                    Map<String, Object> options,
                                    boolean replace)
    {
        synchronized(data)
        {
            if (data.conversations.containsKey(name))
            {
                if (replace)
                {
                    data.conversations.remove(name);
                }
                else
                {
                    return false;
                }
            }
            
            ConversationInit initing = new ConversationInit(name, options);
            if (data.toInit.contains(initing) && !replace)
            {
                return false;
            }
            data.toInit.add(initing);
        }
        return true;
    }
    
    /**
     * 
     * @param listener
     */
    public void registerReadCurator(MessageCurator listener) 
    {
        synchronized(data)
        {
            if (data.readers == null)
            {
                data.readers = new ArrayList<>();
            }
            data.readers.add(listener);
        }
    }
    
    /**
     * 
     * @param listener
     */
    public void registerWriteCurator(MessageCurator listener) 
    {
        synchronized(data)
        {
            if (data.writers == null)
            {
                data.writers = new ArrayList<>();
            }
            data.writers.add(listener);
        }
    }
    
    /**
     * 
     * @return 
     */
    public ConnectionState getConnectionState()
    {
        return state;
    }

    /**
     * 
     * @throws Exception 
     */
    @Override
    protected final void doSingleIteration() throws Exception
    {
        if (data.socket.getSocket().isClosed() || 
            !data.socket.getSocket().isConnected() ||
            state == ConnectionState.CLOSE)
        {
            signalStop();
            return;
        }
        
        StateHandlerPipeline running = handlers.get(state);
        runPipeline(running);
        
        if (state == ConnectionState.CLOSE)
        {
            runPipeline(handlers.get(ConnectionState.CLOSE));
            signalStop();
        }
    }
    
    /**
     * 
     * @param pipeline 
     */
    private void runPipeline(StateHandlerPipeline pipeline)
    {
        try
        {
            ConnectionState request = pipeline.runPipeline(data);
            if (request != null)
            {
                logger.debug("state change to " + request);
                state = request;
            }
        }
        catch(StateHandlerException ex)
        {
            state = ConnectionState.ERROR;
            logger.error("pipeline exception: turning to error mode",
                         ex);
        }
        catch(Throwable ex)
        {
            state = ConnectionState.CLOSE;
            logger.fatal("unexpected exception: closing connection", 
                         ex);
        }
    }
}
