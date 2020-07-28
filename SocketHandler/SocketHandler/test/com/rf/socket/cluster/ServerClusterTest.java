/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.socket.cluster;

import com.rf.socket.cluster.ServerCluster.State;
import com.rf.socket.server.ConnectionHandlerProvider;
import java.net.InetAddress;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author REx
 */
public class ServerClusterTest
{
    
    public ServerClusterTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
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

    /**
     * Test of getManagerAddress method, of class ServerCluster.
     */
    @Test
    public void testGetManagerAddress() throws Exception
    {
        System.out.println("getManagerAddress");
        ServerCluster instance = null;
        InetAddress expResult = null;
        InetAddress result = instance.getManagerAddress();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getState method, of class ServerCluster.
     */
    @Test
    public void testGetState()
    {
        System.out.println("getState");
        ServerCluster instance = null;
        State expResult = null;
        State result = instance.getState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getException method, of class ServerCluster.
     */
    @Test
    public void testGetException()
    {
        System.out.println("getException");
        ServerCluster instance = null;
        Exception expResult = null;
        Exception result = instance.getException();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fixAttempted method, of class ServerCluster.
     */
    @Test
    public void testFixAttempted()
    {
        System.out.println("fixAttempted");
        ServerCluster instance = null;
        instance.fixAttempted();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class ServerCluster.
     */
    @Test
    public void testStart_3args() throws Exception
    {
        System.out.println("start");
        int appport = 0;
        int managerport = 0;
        ConnectionHandlerProvider appprovider = null;
        ServerCluster instance = null;
        instance.start(appport, managerport, appprovider);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class ServerCluster.
     */
    @Test
    public void testStart_ConnectionHandlerProvider() throws Exception
    {
        System.out.println("start");
        ConnectionHandlerProvider appprovider = null;
        ServerCluster instance = null;
        instance.start(appprovider);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shutdown method, of class ServerCluster.
     */
    @Test
    public void testShutdown() throws Exception
    {
        System.out.println("shutdown");
        ServerCluster instance = null;
        instance.shutdown();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of broadcast method, of class ServerCluster.
     */
    @Test
    public void testBroadcast()
    {
        System.out.println("broadcast");
        byte[] message = null;
        ServerCluster instance = null;
        instance.broadcast(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of abort method, of class ServerCluster.
     */
    @Test
    public void testAbort()
    {
        System.out.println("abort");
        ServerCluster instance = null;
        instance.abort();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
