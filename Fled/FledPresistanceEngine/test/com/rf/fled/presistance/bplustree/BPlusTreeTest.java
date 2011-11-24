/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.bplustree;

import com.rf.fled.presistance.filemanager.FlatFileManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author REx
 */
public class BPlusTreeTest {
    
    public BPlusTreeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void happyPathTest() throws Exception {
        System.out.println("happyPathTest");
        
        FlatFileManager instance = new FlatFileManager(System.getProperty("user.dir"));
        
        BPlusTree bplustree = new BPlusTree(
                instance, 
                new MockValueSerializer(), 
                "test",
                100);
        
        for (int i = 0; i < 1000; i++)
        {
            Object result = bplustree.insert(
                    i + 1000, 
                    new MockValue(i + 1000L, "something " + (i + 1000)), 
                    false);
            assertNull(result);
        }
        
        assertEquals(1000, bplustree.getCount());
        
        for (int i = 0; i < 500; i++)
        {
            Object result = bplustree.delete(i + 1000);
            assertNotNull(result);
            assertTrue(result instanceof MockValue);
            MockValue mock = (MockValue) result;
            assertEquals("something " + (i + 1000), mock.something);
            assertEquals((Long)(i + 1000L), mock._else);
        }
        
        for (int i = 500; i < 1000; i++)
        {
            Object result = bplustree.select(i + 1000);
            assertNotNull(result);
            assertTrue(result instanceof MockValue);
            MockValue mock = (MockValue) result;
            assertEquals("something " + (i + 1000), mock.something);
            assertEquals((Long)(i + 1000L), mock._else);
        }
    }
}
