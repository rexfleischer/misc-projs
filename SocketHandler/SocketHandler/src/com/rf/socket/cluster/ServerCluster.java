/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.cluster;

import com.rf.socket.SocketHandler;
import com.rf.socket.server.ConnectionHandlerProvider;
import com.rf.socket.server.ServerSocketListener;
import com.rf.socket.util.Password;
import com.rf.socket.util.SafeYieldingThread;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * this is meant to help different ServerSocketListener instances
 * talk to each other. starting this will initialize an instance of
 * ServerSocketListener.
 * @author REx
 */
public class ServerCluster
{
    private static final int DEFAULT_PORT = 0;
    
    /**
     * the state used for the manager
     */
    public enum State
    {
        // a new instance, but not yet started
        NEW,
        
        // the manager is active
        RUNNING,
        
        // an error occurred... this may be a 
        // recoverable error, but the point is
        // it needs to be checked and the manager
        // may not be working correctly
        ERROR,
        
        // shutdown() or abort() has been called
        TERMINATED,
    }
    
    public static class ServerInfo implements Serializable
    {
        InetAddress manager;
        
        Password password;
    }
    
    /**
     * the entire cluster without "this"
     */
    private ArrayList<ServerInfo> cluster;
    
    /**
     * the socket used by this manager... this is used for
     * receiving messages
     */
    private ServerSocketListener manager;
    
    /**
     * the password for this manager
     */
    private final Password password;
    
    /**
     * the lock used to get and set state
     */
    private final Object STATE_LOCK = new Object();
    
    /**
     * the state of the server
     */
    private State state;
    
    /**
     * the current error if the state is ERROR
     */
    private Exception exception;
    
    private Map<Integer, CommandHandler> commands;
    
    public ServerCluster(List<CommandHandler> commands) 
    {
        cluster = new ArrayList<>();
        password = new Password();
        state = State.NEW;
        exception = null;
        
        this.commands = new HashMap<>();
        Iterator<CommandHandler> it = commands.iterator();
        while(it.hasNext())
        {
            CommandHandler command = it.next();
            this.commands.put(command.name.hashCode(), command);
        }
    }
    
    public InetAddress getManagerAddress()
            throws ServerClusterStateException
    {
        if (getState() != State.RUNNING)
        {
            throw new ServerClusterStateException(
                    "manager must be in the RUNNING state to call this");
        }
        return manager.getServerSocket().getInetAddress();
    }
    
    public State getState()
    {
        synchronized(STATE_LOCK)
        {
            return state;
        }
    }
    
    private void setState(State state)
    {
        synchronized(STATE_LOCK)
        {
            this.state = state;
        }
    }
    
    public Exception getException()
    {
        return exception;
    }
    
    public void fixAttempted()
    {
        setState(State.RUNNING);
        exception = null;
    }
    
    public void start(int appport, 
                      int managerport,
                      ConnectionHandlerProvider appprovider) 
            throws IOException, ServerClusterStateException
    {
        if (getState() != State.NEW)
        {
            throw new ServerClusterStateException(
                    "manager must be in the NEW state to call this");
        }
        manager = new ServerSocketListener(
                managerport,
                new ConnectionHandlerProviderImpl());
        manager.start();
    }
    
    public void start(ConnectionHandlerProvider appprovider)
            throws IOException, ServerClusterStateException
    {
        start(DEFAULT_PORT, DEFAULT_PORT, appprovider);
    }
    
    /**
     * this shuts down this server and broadcasts to the others
     * that this server is going off line.
     */
    public void shutdown()
            throws ServerClusterStateException
    {
        if (getState() != State.RUNNING && getState() != State.ERROR)
        {
            throw new ServerClusterStateException(
                    "manager must be in the RUNNING or ERROR state to call this");
        }
        
        broadcast("shutdown".hashCode(), new byte[1]);
    }
    
    public void broadcast(int command, byte[] message)
    {
        
    }
    
    /**
     * this shuts down the server without broadcasting or checking
     * statuses. it will not check state.
     */
    public void abort()
    {
        if (manager != null)
        {
            try
            {
                manager.shutdown();
            }
            catch(InterruptedException ex) {}
        }
        
        setState(State.TERMINATED);
    }
    
    private class ConnectionHandlerProviderImpl implements
            ConnectionHandlerProvider
    {
        @Override
        public SafeYieldingThread provide(Socket socket) throws IOException
        {
            return new ManagerMessageHandler(socket);
        }
    }
    
    
    private class ManagerMessageHandler extends SafeYieldingThread
    {
        private SocketHandler socket;
        
        private Message message;
        
        private CommandHandler command;
        
        private Map<String, Object> commandstate;
        
        private ManagerMessageHandler(Socket socket) throws IOException
        {
            this.socket = new SocketHandler(socket);
        }
        
        @Override
        protected void doSingleIteration() throws Exception
        {
            if (message == null)
            {
                // check to see if the message came in yet, and if
                // it did, then get the command and run it.
                byte[] rawmessage = socket.read();
                if (rawmessage == null)
                {
                    return;
                }
                
                message = new Message(rawmessage);
                if (!message.password.equals(password))
                {
                    throw new IllegalPasswordException();
                }
                
                command = commands.get(message.command);
                if (command == null)
                {
                    throw new CommandHandlerException(
                            String.format("unknown command key %s", 
                                          message.command));
                }
                
                commandstate = new HashMap<>();
            }
            else if (!command.execute(cluster, socket, message, commandstate))
            {
                signalStop();
                socket.stopReader();
            }
        }
    }
}
