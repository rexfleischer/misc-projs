/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.server;

import com.rf.fledwar.socket.connection.Connection;
import com.rf.fledwar.socket.util.SafeYieldingThread;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * all this does is accept and pass on the connection to a thread...
 * eventually, some form of load balancing will have to occur as well.
 * @author REx
 */
public class ServerSocketListener extends SafeYieldingThread
{
    private static Logger logger = Logger.getLogger(ServerSocketListener.class);
    
    private final ArrayList<Connection> connections;
    
    private final ServerSocket serversocket;
    
    private final Constructor<? extends Connection> provider;
    
    private final Object[] providerparams;
    
    public ServerSocketListener(Class<? extends Connection> provider, 
                                Class ... extra)
            throws IOException, NoSuchMethodException
    {
        this(0, provider, extra);
    }
    
    public ServerSocketListener(int port, 
                                Class<? extends Connection> provider, 
                                Class ... providerinput) 
            throws IOException, NoSuchMethodException
    {
        super(logger);
        this.stop = false;
        this.connections = new ArrayList<>();
        
        Class[] paramtypes = new Class[providerinput.length + 1];
        providerparams = new Object[providerinput.length + 1];
        paramtypes[0] = Socket.class;
        for(int i = 0; i < providerinput.length; i++)
        {
            if (providerinput[i] == null)
            {
                throw new NullPointerException(
                        "every object in 'extra' must be not null");
            }
            paramtypes[i + 1] = providerinput[i];
        }
        
        this.provider = provider.getConstructor(paramtypes);
        
        StringBuilder builder = new StringBuilder();
        builder.append("started server with following options:\n");
        builder.append("  -- port: ");
        builder.append(port);
        builder.append("\n");
        builder.append("  -- provider: ");
        builder.append(provider);
        builder.append("\n");
        builder.append("  -- provider constructor: ");
        builder.append(Arrays.toString(paramtypes));
        builder.append("\n");
        logger.info(builder.toString());
        
        
        serversocket = new ServerSocket(port);
        serversocket.setSoTimeout(100);
        
        logger.info(String.format("connected with server socket:%s", 
                                  serversocket));
    }
    
    public final void setProviderParams(Object ... params)
    {
        System.arraycopy(params, 0, providerparams, 1, params.length);
    }
    
    @Override
    public void signalStop()
    {
        // being that nothing can see the threads on connections
        // and connections cannot communicate with this object, 
        // there is no risk of deadlock when calling signalStop()
        // on the managed threads...
        synchronized(this)
        {
            if (!stop)
            {
                stop = true;

                for(SafeYieldingThread thread : connections)
                {
                    thread.signalStop();
                }
            }
        }
    }
    
    public synchronized String getConnections()
    {
        return connections.toString();
    }
    
    /**
     * 
     * @return 
     */
    public ServerSocket getServerSocket()
    {
        return serversocket;
    }
    
    /**
     * this should be called instead of join(). this will make sure
     * to end this thread and all of the managed threads.
     * 
     * this will call signalStop() if it hasnt be called.
     */
    public void shutdown() throws InterruptedException
    {
        synchronized(this)
        {
            if (!stop)
            {
                signalStop();
            }
            
            for(SafeYieldingThread thread : connections)
            {
                thread.join();
            }
            
            join();
        }
    }

    @Override
    protected void doSingleIteration() throws Exception
    {
        try
        {
            try
            {
                final Socket socket = serversocket.accept();
                providerparams[0] = socket;
                final Connection connection = provider.newInstance(providerparams);
                synchronized(this)
                {
                    // just to make sure
                    if (stop)
                    {
                        throw new IllegalStateException("stop signalled");
                    }
                    
                    // we do these within here because we dont want
                    // signalStop() to be called which could cause
                    // a ConcurrentModificationException or weirdness
                    // within the starting thread.... 
                    //
                    // just let it happen...
                    connections.add(connection);
                    connection.start();
                    logger.info(String.format("new connection with [%s:%s]", 
                                              socket.getInetAddress(),
                                              socket.getPort()));
                }
            }
            catch(SocketTimeoutException ex)
            {
                // if we get here, everything is ok...
            }
            
            // go through and gc the connections
            Iterator<Connection> it = connections.iterator();
            while(it.hasNext())
            {
                Connection connection = it.next();
                if (!connection.isAlive())
                {
                    it.remove();
                }
            }
        }
        catch(Throwable ex)
        {
            logger.fatal("unexpected exception", ex);
            closeKindaQuietly();
            throw ex;
        }
    }
    
    private void closeKindaQuietly()
    {
        if (serversocket != null)
        {
            try
            {
                serversocket.close();
            }
            catch(IOException ex)
            {
                logger.error("could not close connection:", ex);
            }
        }
    }
}
