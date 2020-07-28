///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.util;
//
//import com.fledwar.thread.ScheduledFutureArray;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
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
//public class FutureArrayTest
//{
//    
//    public FutureArrayTest()
//    {
//    }
//    
//    @BeforeClass
//    public static void setUpClass()
//    {
//    }
//    
//    @AfterClass
//    public static void tearDownClass()
//    {
//    }
//    
//    @Before
//    public void setUp()
//    {
//    }
//    
//    @After
//    public void tearDown()
//    {
//    }
//
//    /**
//     * Test of cancelAll method, of class FutureArray.
//     */
//    @Test
//    public void testCancelAll() throws Exception
//    {
//        System.out.println("cancelAll");
//        
//        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
//        
//        ScheduledFutureArray instance1 = new ScheduledFutureArray();
//        ScheduledFutureArray instance2 = new ScheduledFutureArray();
//        
//        for(int i = 0; i < 10; i++)
//        {
//            ScheduledFuture future = executor.scheduleAtFixedRate(
//                    new RunnableTest(i), 
//                    50, 100, 
//                    TimeUnit.MILLISECONDS);
//            if (i % 2 == 0)
//            {
//                instance1.wrap(future);
//            }
//            else
//            {
//                instance2.wrap(future);
//            }
//        }
//        
//        Thread.sleep(1000);
//        instance1.cancelAll();
//        System.out.println("evens cancelled");
//        Thread.sleep(1000);
//        instance2.cancelAll();
//        
//    }
//    
//    public class RunnableTest implements Runnable
//    {
//        public int number;
//
//        public RunnableTest(int number)
//        {
//            this.number = number;
//        }
//        
//        @Override
//        public void run()
//        {
//            System.out.println("hello " + number);
//        }
//    }
//}
