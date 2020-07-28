/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.tree.BPlusTree;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.tree.TreeType;
import com.rf.fled.persistence.Provider;
import com.rf.fled.persistence.ProviderHint;
import com.rf.fled.persistence.transaction.TransactionType;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
    
    public static final String DIRECTORY = "C:/Users/REx/Desktop/fledhome/data/";
    
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
    
    public IPersistence getBPlusTree(int count) throws FledPersistenceException 
    {
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put(ProviderHint.RECORDS_PER_PAGE.name(), 64);
        return Provider.createPersistence(
                DIRECTORY, 
                "test",
                TreeType.BPLUSTREE,
                RecordCacheType.NONE, 
                FileManagerType.FILE_SYSTEM_NO_TREE, 
                FileManagerCacheType.NONE, 
                TransactionType.NONE,
                hints);
    }
    
//    @Test
    public void testTruncate() throws Exception
    {
        System.out.println("testTruncate");
        
        IPersistence instance = getBPlusTree(4);
        
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
    
//    @Test
    public void testRandomInserts() throws Exception
    {
        System.out.println("testRandomInserts");
        
        IPersistence instance = getBPlusTree(64);
        Random random = new Random();
        
        int count = 1001;
        int check = 100;
        
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
        
        IPersistence instance = getBPlusTree(64);
        
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
    
//    @Test
    public void testSaveAndLoadPersistence() throws Exception
    {
        System.out.println("testSaveAndLoadPersistence");
        
        IPersistence instance = getBPlusTree(8);
        
        // load 1000 random stuff
        Random random = new Random();
        int count = 1001;
        int check = 100;
        
        for(int i = 0; i < count; i++)
        {
            int randomValue = random.nextInt(20000) + 1;
            MockValue value = new MockValue();
            value.id = randomValue;
            value.content = "hello world number " + randomValue;
            instance.insert(randomValue, value, true);
            if (i % check == 0)
            {
                System.out.println("inserted " + i + " records");
            }
        }
        
        long size =  instance.size();
        System.out.println("size of btree is " + size);
        
        instance = Provider.loadPersistence(
                DIRECTORY + "/test.fdb", 
                RecordCacheType.NONE, 
                TransactionType.NONE);
        
        System.out.println("size of btree after load is " + instance.size());
        
        Assert.assertEquals(size, instance.size());
        
        instance.truncate();
    }
    
    public class Counter
    {
        public static final long NULL = -1;
        
        private long max;
        
        private long counter;
        
        public Counter(long max)
        {
            this.max = max;
            counter  = 0;
        }
        
        public synchronized long getCount()
        {
            if (counter >= max)
            {
                return NULL;
            }
            if (counter % 100 == 0)
            {
                System.out.println("counted to " + counter);
            }
            return counter++;
        }
    }
    
    @Test
    public void testThreadInsertsAndRead() throws Exception
    {
        System.out.println("testThreadInsertsAndRead");
        final IPersistence instance = getBPlusTree(64);
        final Counter counter = new Counter(1000);
        final Random random = new Random();
        
        Thread[] inserts = new Thread[4];
        for(int i = 0; i < inserts.length; i++)
        {
            inserts[i] = new Thread()
            {
                @Override
                public void run()
                {
                    while(counter.getCount() != Counter.NULL)
                    {
                        int randomValue = random.nextInt(200000) + 1;
                        MockValue value = new MockValue();
                        value.id = randomValue;
                        value.content = "hello world number " + randomValue;
                        try
                        {
                            instance.insert(randomValue, value, true);
                        }
                        catch (FledPersistenceException ex)
                        {
                            throw new IllegalArgumentException("stuff", ex);
                        }
                    }
                }
            };
            inserts[i].start();
        }
        
        for(Thread thread : inserts)
        {
            thread.join();
        }
        
//        while(true)
//        {
//            boolean doRead = false;
//            for(int i = 0; i < inserts.length; i++)
//            {
//                if (inserts[i].isAlive())
//                {
//                    doRead = true;
//                    break;
//                }
//            }
//            if (!doRead)
//            {
//                break;
//            }
//            
//            // just check the first few and print the ids
//            KeyValuePair<Long, Object> tuple = new KeyValuePair<Long, Object>();
//            Browser<KeyValuePair<Long, Object>> browser = instance.browse();
//            if (browser == null)
//            {
//                continue;
//            }
//            String ids = "";
//            
//            browser.curr(tuple);
//            ids += tuple.getKey();
//            ids += ", ";
//            browser.next(tuple);
//            ids += tuple.getKey();
//            ids += ", ";
//            browser.next(tuple);
//            ids += tuple.getKey();
//            ids += " size: " + instance.size();
//            browser.finished();
//            
//            System.out.println("read 3 values of ids: " + ids);
//        }
        
        System.out.println("instance size: " + instance.size());
        instance.drop();
    }
}
