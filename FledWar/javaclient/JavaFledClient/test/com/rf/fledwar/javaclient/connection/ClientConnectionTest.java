///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fledwar.javaclient.connection;
//
//import com.rf.fledwar.javaclient.logging.Log4JHelper;
//import com.rf.fledwar.socket.connection.ConnectionState;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import junit.framework.Assert;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
///**
// *
// * @author REx
// */
//public class ClientConnectionTest
//{
//    
//    public ClientConnectionTest()
//    {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() throws IOException
//    {
//        Log4JHelper.startLogger();
//    }
//    
//    @AfterClass
//    public static void tearDownClass()
//    {
//    }
//    
//    @Before
//    public void setUp() throws Exception
//    {
//        client = ClientConnectionFactory.getDefault(true);
//        while(client.getConnectionState() == ConnectionState.OPEN)
//        {
//            Thread.sleep(100);
//        }
//    }
//    
//    @After
//    public void tearDown() throws Exception
//    {
//        client.close();
//    }
//    
//    public ClientConnection client;
//
//    /**
//     * Test of get method, of class ClientConnection.
//     */
//    @Test
//    public void testGet() throws Exception
//    {
//        System.out.println("testGet");
//        
//        
//        List names = (List) client.blockingQuery(
//                "listsystemnames", null, 2000);
//        
//        Assert.assertNotNull(names);
//        Assert.assertTrue(!names.isEmpty());
//        
//        
//        
//        {
//            String name = names.get(2).toString();
//            System.out.println(String.format("focusing on %s", name));
//            Map<String, Object> options = new HashMap<>();
//            options.put("systemname", name);
//            client.setFocus("main", options);
//            long ending = System.currentTimeMillis() + 5000;
//            do
//            {
//                Map result = (Map) client.getFocus("main");
//                if (result != null)
//                {
//                    System.out.println(result);
//                }
//                Thread.sleep(10);
//            }
//            while(ending > System.currentTimeMillis());
//        }
//        
//        
//        
//        {
//            String name = names.get(3).toString();
//            System.out.println(String.format("focusing on %s", name));
//            Map<String, Object> options = new HashMap<>();
//            options.put("systemname", name);
//            client.setFocus("main", options);
//            long ending = System.currentTimeMillis() + 5000;
//            do
//            {
//                Map result = (Map) client.getFocus("main");
//                if (result != null)
//                {
//                    System.out.println(result);
//                }
//                Thread.sleep(10);
//            }
//            while(ending > System.currentTimeMillis());
//        }
//    }
//}
