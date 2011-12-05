/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.FledPresistanceException;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.filemanager.FileManager_FileSystemNoTree;
import java.io.File;
import java.util.Random;
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
    
    public static final String DIRECTORY = "C:/Users/REx/Desktop/fledhome/data";
    
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
        File[] files = (new File(DIRECTORY)).listFiles();
        for(File file : files)
        {
            file.delete();
        }
    }
    
    @After
    public void tearDown() {
        File[] files = (new File(DIRECTORY)).listFiles();
        for(File file : files)
        {
            file.delete();
        }
    }
    
    public Persistence getBPlusTree(int count) throws FledPresistanceException 
    {
        FileManager fileManager = new FileManager_FileSystemNoTree(DIRECTORY, 0);
//        FileManager fileManager = new FileManager_InMemory();
        Persistence instance = BPlusTree.createBPlusTree(fileManager, "test", count, null, null);
        return instance;
    }
    
//    @Test
    public void testSpeed() throws Exception
    {
        // start with 8
        int size = 8;
        int count = 1001;
        int check = 200;
        FileManager fileManager = new FileManager_FileSystemNoTree(DIRECTORY, 0);
        
        for(int kk = 0; kk < 6; kk++)
        {
            Persistence instance = BPlusTree.createBPlusTree(fileManager, "test", size, null, null);

            long start = System.currentTimeMillis();
            for(int i = 0; i < count; i++)
            {
                MockValue value = new MockValue();
                value.id = (long)(i + 1);
                value.content = "hello world number " + (i + 1);
                Object result = instance.insert((long)(i + 1), value, true);
                Assert.assertNull(result);
                if (i % check == 0)
                {
                    System.out.println("inserted " + i + " records with " + size);
                }
            }
            System.out.println("finshed inserts in " + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            for(int i = 0; i < count; i++)
            {
                Object result = instance.select((long)(i + 1));
                Assert.assertTrue(result instanceof MockValue);
                MockValue value = (MockValue) result;
                Assert.assertEquals(((long) (i + 1)), value.id);
                Assert.assertEquals("hello world number " + (i + 1), value.content);
                if (i % check == 0)
                {
                    System.out.println("read " + i + " records with " + size);
                }
            }
            System.out.println("finshed reads in " + (System.currentTimeMillis() - start));

            start = System.currentTimeMillis();
            for(int i = 0; i < count; i++)
            {
                Object result = instance.delete((long)(i + 1));
                Assert.assertTrue(result instanceof MockValue);
                MockValue value = (MockValue) result;
                Assert.assertEquals(((long) (i + 1)), value.id);
                Assert.assertEquals("hello world number " + (i + 1), value.content);
                if (i % check == 0)
                {
                    System.out.println("deleted " + i + " records with " + size);
                }
            }
            System.out.println("finshed deletes in " + (System.currentTimeMillis() - start));
            
            size *= 2;
        }
    }
    
//    @Test
    public void testTruncate() throws Exception
    {
        System.out.println("testTruncate");
        
        Persistence instance = getBPlusTree(4);
        
        int count = 101;
        
        for(int i = 0; i < count; i++)
        {
            MockValue value = new MockValue();
            value.id = (long)(i + 1);
            value.content = "hello world number " + (i + 1);
            instance.insert((long)(i + 1), value, true);
        }
        
        instance.truncate();
        
        File[] files = (new File(DIRECTORY)).listFiles();
        Assert.assertEquals(1, files.length);
    }
    
//    @Test
    public void runRandomInsertsAlot() throws Exception
    {
        // the reason we need to do this is because
        // random inserts are just that... random. so we need
        // to check it a few times to get a higher degree of confidence
        for(int i = 0; i < 5; i++)
        {
            System.out.println("testRandomInserts: " + (i + 1));
            testRandomInserts();
        }
    }
    
    @Test
    public void testRandomInserts() throws Exception
    {
        System.out.println("testRandomInserts");
        
        Persistence instance = getBPlusTree(64);
        Random random = new Random();
        
        int count = 10001;
        int check = 500;
        
        for(int i = 0; i < count; i++)
        {
            int randomValue = random.nextInt(200000) + 1;
            MockValue value = new MockValue();
            value.id = randomValue;
            value.content = "hello world number " + randomValue;
            instance.insert(randomValue, value, true);
            if (i % check == 0)
            {
                System.out.println("inserted " + i + " records");
            }
        }
        
        System.out.println("number of records: " + instance.size());
        
        ((BPlusTree) instance).assertOrder(Integer.MAX_VALUE);
        ((BPlusTree) instance).assertValues();
        
        instance.truncate();
    }
    
//    @Test
    public void testHappyPath() throws Exception {
        System.out.println("happy path");
        
        Persistence instance = getBPlusTree(64);
        
        int count = 10001;
        int check = 1000;
        
        for(int i = 0; i < count; i++)
        {
            MockValue value = new MockValue();
            value.id = (long)(i + 1);
            value.content = "hello world number " + (i + 1);
            Object result = null;
            if (i % check == 0)
            {
                System.out.println("inserted " + i + " records");
            }
            try
            {
                if (i == 136)
                {
                    int b = 0;
                    b++;
                }
                result = instance.insert((long)(i + 1), value, true);
//                ((BPlusTree) instance).assertOrder(1);
//                ((BPlusTree) instance).assertValues();
                Assert.assertNull(result);
            }
            catch(Exception ex)
            {
                System.out.println("insertion error on index " + i);
                ((BPlusTree) instance).dump();
                throw ex;
            }
//            ((BPlusTree) instance).dump();
        }
        
//        ((BPlusTree) instance).dump();
        
        for(int i = 0; i < count; i++)
        {
            Object result = null;
            try
            {
                result = instance.select((long)(i + 1));
                
//                ((BPlusTree) instance).assertOrder(1);
//                ((BPlusTree) instance).assertValues();
                Assert.assertTrue(result instanceof MockValue);
                MockValue value = (MockValue) result;
                Assert.assertEquals(((long) (i + 1)), value.id);
                Assert.assertEquals("hello world number " + (i + 1), value.content);
            }
            catch(Exception ex)
            {
                System.out.println("selection error on index " + i);
                ((BPlusTree) instance).dump();
                throw ex;
            }
            if (i % check == 0)
            {
                System.out.println("read " + i + " records");
            }
        }
        
        for(int i = 0; i < count; i++)
        {
            Object result = null;
            try
            {
                if (i == 63)
                {
                    int b = 0;
                    b++;
                }
                result = instance.delete((long)(i + 1));
                
//                ((BPlusTree) instance).dump();
//                ((BPlusTree) instance).assertOrder(1);
//                ((BPlusTree) instance).assertValues();
                Assert.assertTrue(result instanceof MockValue);
                MockValue value = (MockValue) result;
                Assert.assertEquals(((long) (i + 1)), value.id);
                Assert.assertEquals("hello world number " + (i + 1), value.content);
            }
            catch(Exception ex)
            {
                System.out.println("deletion error on index " + i);
                ((BPlusTree) instance).dump();
                throw ex;
            }
            if (i % check == 0)
            {
                System.out.println("deleted " + i + " records");
            }
        }
        ((BPlusTree) instance).dump();
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
