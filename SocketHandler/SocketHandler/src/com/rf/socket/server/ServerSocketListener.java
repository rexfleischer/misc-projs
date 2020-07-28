/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.server;

import com.rf.socket.util.ExceptionUtil;
import com.rf.socket.util.SafeYieldingThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * all this does is accept and pass on the connection to a thread...
 * eventually, some form of load balancing will have to occur as well.
 * @author REx
 */
public class ServerSocketListener extends SafeYieldingThread
{
    private final int port;
    
    private ServerSocket serversocket;
    
    private final ConnectionHandlerProvider provider;
    
    private final ArrayList<SafeYieldingThread> connections;
    
    public ServerSocketListener(int port, ConnectionHandlerProvider provider) 
            throws IOException
    {
        super();
        this.port = port;
        this.stop = false;
        this.provider = provider;
        this.connections = new ArrayList<>();
        connect();
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
            boolean onerror = false;
            try
            {
                final Socket socket = serversocket.accept();
                final SafeYieldingThread thread = provider.provide(socket);
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
                    connections.add(thread);
                    thread.start();
                }
            }
            catch(SocketTimeoutException ex)
            {
                // if we get here, everything is ok...
            }
            catch(IOException ex)
            {
                ExceptionUtil.logError(ex);
                onerror = true;
            }

            // if there was an error, try to recover... else, just
            // exit the thread.
            if (onerror)
            {
                closeKindaQuietly();
                connect();
            }
        }
        catch(IOException ex)
        {
            closeKindaQuietly();
            throw ex;
        }
    }
    
    private void connect()
            throws IOException
    {
        serversocket = new ServerSocket(port);
        serversocket.setSoTimeout(100);
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
                ExceptionUtil.logError(ex);
            }
        }
    }
}
