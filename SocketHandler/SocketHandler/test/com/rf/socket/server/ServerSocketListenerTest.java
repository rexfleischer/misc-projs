/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.server;

import com.rf.socket.SocketHandler;
import com.rf.socket.util.SafeYieldingThread;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class ServerSocketListenerTest 
{
    public static final String HOST = "localhost";
    
    public static final int PORT = 0;
    
    public static final int CLIENT_COUNT = 10;
    
    public ServerSocketListenerTest() 
    {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    @Before
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() 
    {
    }
    
    int realport;

    /**
     * Test of signalStop method, of class ServerSocketListener.
     */
    @Test
    public void test_happypath() throws Throwable
    {
        System.out.println("test_happypath");
        
        ServerSocketListener server = new ServerSocketListener(
                PORT,
                new ConnectionHandlerProvider() 
                {
                    @Override
                    public SafeYieldingThread provide(Socket socket) 
                            throws IOException
                    {
                        return new ServerSideHandler(socket);
                    }
                });
        server.start();
        
        realport = server.getServerSocket().getLocalPort();
        
        ArrayList<ClientSideHandler> clients = new ArrayList<>();
        for(int i = 0; i < CLIENT_COUNT; i++)
        {
            clients.add(new ClientSideHandler());
        }
        
        for(int i = 0; i < CLIENT_COUNT; i++)
        {
            clients.get(i).start();
        }
        
        for(int i = 0; i < CLIENT_COUNT; i++)
        {
            clients.get(i).join();
        }
        
        server.shutdown();
    }
    
    public class ServerSideHandler extends SafeYieldingThread
    {
        SocketHandler socket;
        
        public ServerSideHandler(Socket socket) throws IOException
        {
            this.socket = new SocketHandler(socket);
            System.out.println(String.format(
                    "socket connected with client [%s]", socket));
        }
        
        @Override
        protected void doSingleIteration() throws Exception
        {
            Exception ex = socket.getException();
            if (ex != null)
            {
                throw ex;
            }
            
            byte[] read = socket.read();
            if (read != null)
            {
                String statement = new String(read);
                
                if (statement.equalsIgnoreCase("knock knock"))
                {
                    System.out.println(String.format(
                            "%s: whose there", socket));
                    socket.write("whose there".getBytes());
                }
                else if (statement.equalsIgnoreCase("a client!"))
                {
                    System.out.println(String.format(
                            "%s: a client who?", socket));
                    socket.write("a client who?".getBytes());
                }
                else if (statement.equalsIgnoreCase("a super client!"))
                {
                    System.out.println(String.format(
                            "%s: bye", socket));
                    socket.write("bye".getBytes());
                }
                else if (statement.equalsIgnoreCase("bye"))
                {
                    System.out.println(String.format(
                            "%s: closing", socket));
                    socket.close();
                    signalStop();
                }
                else
                {
                    throw new Exception(String.format(
                            "unknown statement [%s]", statement));
                }
            }
        }
    }
    
    public class ClientSideHandler extends SafeYieldingThread
    {
        boolean first = true;
        
        SocketHandler socket;
        
        public ClientSideHandler() throws IOException
        {
            socket = new SocketHandler(new Socket(HOST, realport));
            System.out.println(String.format(
                    "socket connected with server [%s]", socket));
        }

        @Override
        protected void doSingleIteration() throws Exception
        {
            Exception ex = socket.getException();
            if (ex != null)
            {
                throw ex;
            }
            
            if (first)
            {
                first = false;
                System.out.println(String.format("%s: knock knock", socket));
                socket.write("knock knock".getBytes());
            }
            else
            {
                byte[] read = socket.read();
                if (read != null)
                {
                    String statement = new String(read);
                    if ("whose there".equals(statement))
                    {
                        System.out.println(String.format("%s: a client!", socket));
                        socket.write("a client!".getBytes());
                    }
                    else if ("a client who?".equals(statement))
                    {
                        System.out.println(String.format("%s: a super client!", socket));
                        socket.write("a super client!".getBytes());
                    }
                    else if ("bye".equals(statement))
                    {
                        System.out.println(String.format("%s: bye", socket));
                        socket.write("bye".getBytes());
                        socket.close();
                        signalStop();
                    }
                    else
                    {
                        throw new Exception(String.format(
                                "unknown statement [%s]", statement));
                    }
                }
            }
        }
        
    }
}
