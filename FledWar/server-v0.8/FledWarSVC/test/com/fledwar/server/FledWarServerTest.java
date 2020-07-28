/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server;

import com.fledwar.server.svc.AdminServlet;
import com.fledwar.util.JsonHelper;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class FledWarServerTest
{
    
    public FledWarServerTest()
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
    public void setUp() throws Exception
    {
        FledWarServer.start();
        Thread.sleep(2000);
    }
    
    @After
    public void tearDown() throws Exception
    {
        FledWarServer.shutdown();
    }

    /**
     * Test of start method, of class FledWarServer.
     */
    @Test
    public void testStart() throws Exception
    {
        System.out.println("testStart");
        
    }
    
//    @Test
//    public void testResetAll() throws Exception
//    {
//        System.out.println("testResetAll");
//        Map result = JsonHelper.toJavaMap((new AdminServlet()).);
//        
//        Assert.assertNull(result.get("error"));
//    }
}
