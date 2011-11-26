///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fled.presistance.bplustree;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author REx
// */
//public class BPlusTreeTest {
//    
//    public BPlusTreeTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    @Test
//    public void happyPathTest() throws Exception {
//        System.out.println("happyPathTest");
//        
//        FlatFileManager instance = new FlatFileManager(System.getProperty("user.dir"));
//        
//        BPlusTree bplustree = new BPlusTree(
//                instance, 
//                new MockValueSerializer(), 
//                "test",
//                100);
//        
//        for (int i = 0; i < 1000; i++)
//        {
//            Object result = bplustree.insert(
//                    i + 1000, 
//                    new MockValue(i + 1000L, "something " + (i + 1000)), 
//                    false);
//            assertNull(result);
//        }
//        
//        assertEquals(1000, bplustree.getCount());
//        
//        for (int i = 0; i < 500; i++)
//        {
//            Object result = bplustree.delete(i + 1000);
//            assertNotNull(result);
//            assertTrue(result instanceof MockValue);
//            MockValue mock = (MockValue) result;
//            assertEquals("something " + (i + 1000), mock.something);
//            assertEquals((Long)(i + 1000L), mock._else);
//        }
//        
//        for (int i = 500; i < 1000; i++)
//        {
//            Object result = bplustree.select(i + 1000);
//            assertNotNull(result);
//            assertTrue(result instanceof MockValue);
//            MockValue mock = (MockValue) result;
//            assertEquals("something " + (i + 1000), mock.something);
//            assertEquals((Long)(i + 1000L), mock._else);
//        }
//    }
//
//    @Test
//    public void testBinaryTreeSearch() throws Exception 
//    {
//        for (int l = 1; l < 50; l++)
//        {
//            long[] array = new long[l];
//            long start = 1234;
//            for(int i = 0; i < l; i++)
//            {
//                array[i] = start;
//                start += 2;
//            }
//            start = 1233;
//            
//            if (l >= 100)
//            {
//                System.out.print(l + ": ");
//            }
//            else if (l >= 10)
//            {
//                System.out.print(" " + l + ": ");
//            }
//            else
//            {
//                System.out.print("  " + l + ": ");
//            }
//            
//            
//            
//            for (int i = 0; i < 2*l+1; i++)
//            {
//                System.out.print(findFirstLessOrEqualChild(array, start) + ",");
//                start++;
//            }
//            System.out.println();
//        }
//    }
//    
//    public static int findFirstLessOrEqualChild(long[] keys, long key)
//    {
//        int min = 0;
//        int max = keys.length - 1;
//
//        // do a binary search
//        while(max > min)
//        {
//            int mid = (max + min) / 2;
//            if (keys[mid] == key)
//            {
//                return mid;
//            }
//            if (keys[mid] > key) 
//            {
//                max = mid;
//            }
//            else 
//            {
//                min = mid + 1;
//            }
//        }
//        if (keys[max] > key && max > 0)
//        {
//            return max - 1;
//        }
//        return max;
//    }
//}
