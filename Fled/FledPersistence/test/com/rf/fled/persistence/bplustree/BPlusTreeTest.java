/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.exceptions.FledIOException;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.config.FledProperties;
import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.fileio.BaseFileManager;
import java.io.File;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        File[] files = (new File("C:/Users/REx/Desktop/fledhome/data")).listFiles();
        for(File file : files)
        {
            file.delete();
        }
    }
    
    @After
    public void tearDown() {
    }
    
    public Persistence getBPlusTree() throws FledIOException
    {
        FileManager fileManager = new BaseFileManager(
                new FledProperties(), 0, "C:/Users/REx/Desktop/fledhome/data");
        Persistence instance = BPlusTree.createBPlusTree(fileManager, "test");
        return instance;
    }
    
    @Test
    public void testHappyPath() throws Exception {
        System.out.println("happy path");
        
        Persistence instance = getBPlusTree();
        
        for(int i = 0; i < 100; i++)
        {
            MockValue value = new MockValue();
            value.id = (long)i;
            value.content = "hello world number " + (i + 1);
            Object result = instance.insert((long)(i + 1), value, true);
            Assert.assertNull(result);
        }
        
        for(int i = 0; i < 100; i++)
        {
            Object result = instance.select((long)(i + 1));
            Assert.assertTrue(result instanceof MockValue);
            MockValue value = (MockValue) result;
            Assert.assertEquals(((long) i), value.id);
            Assert.assertEquals("hello world number " + (i + 1), value.content);
        }
        
        for(int i = 0; i < 100; i++)
        {
            Object result = instance.delete((long)(i + 1));
            Assert.assertTrue(result instanceof MockValue);
            MockValue value = (MockValue) result;
            Assert.assertEquals(((long) i), value.id);
            Assert.assertEquals("hello world number " + (i + 1), value.content);
        }
    }

//    /**
//     * Test of size method, of class BPlusTree.
//     */
//    @Test
//    public void testSize() throws Exception {
//        System.out.println("size");
//        
//        Persistence instance = getBPlusTree();
//        
//        long expResult = 0L;
//        long result = instance.size();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getContext method, of class BPlusTree.
//     */
//    @Test
//    public void testGetContext() throws Exception {
//        System.out.println("getContext");
//        
//        Persistence instance = getBPlusTree();
//        String expResult = "";
//        String result = instance.getContext();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of browse method, of class BPlusTree.
//     */
//    @Test
//    public void testBrowse_long() throws Exception {
//        System.out.println("browse");
//        long id = 0L;
//        Persistence instance = getBPlusTree();
//        Browser expResult = null;
//        Browser result = instance.browse(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of browse method, of class BPlusTree.
//     */
//    @Test
//    public void testBrowse_0args() throws Exception {
//        System.out.println("browse");
//        Persistence instance = getBPlusTree();
//        Browser expResult = null;
//        Browser result = instance.browse();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of select method, of class BPlusTree.
//     */
//    @Test
//    public void testSelect() throws Exception {
//        System.out.println("select");
//        long id = 0L;
//        Persistence instance = getBPlusTree();
//        Object expResult = null;
//        Object result = instance.select(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insert method, of class BPlusTree.
//     */
//    @Test
//    public void testInsert() throws Exception {
//        System.out.println("insert");
//        long id = 0L;
//        Object record = null;
//        boolean replace = false;
//        Persistence instance = getBPlusTree();
//        Object expResult = null;
//        Object result = instance.insert(id, record, replace);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class BPlusTree.
//     */
//    @Test
//    public void testDelete() throws Exception {
//        System.out.println("delete");
//        long id = 0L;
//        Persistence instance = getBPlusTree();
//        Object expResult = null;
//        Object result = instance.delete(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of beginTransaction method, of class BPlusTree.
//     */
//    @Test
//    public void testBeginTransaction() throws Exception {
//        System.out.println("beginTransaction");
//        Persistence instance = getBPlusTree();
//        instance.beginTransaction();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of commit method, of class BPlusTree.
//     */
//    @Test
//    public void testCommit() throws Exception {
//        System.out.println("commit");
//        Persistence instance = getBPlusTree();
//        instance.commit();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of rollback method, of class BPlusTree.
//     */
//    @Test
//    public void testRollback() throws Exception {
//        System.out.println("rollback");
//        Persistence instance = getBPlusTree();
//        instance.rollback();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
