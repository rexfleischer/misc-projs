/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.tree.TreeType;
import com.rf.fled.persistence.Provider;
import com.rf.fled.persistence.ProviderHint;
import java.io.File;
import java.util.ArrayList;
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
public class TransactionPersistenceTest {
    
    public static final String DIRECTORY = "C:/Users/REx/Desktop/fledhome/data/";
    
    public TransactionPersistenceTest() {
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
    }
    
    public IPersistence buildSimpleTransactionWraping(int count) throws FledPersistenceException
    {
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put(ProviderHint.RECORDS_PER_PAGE.name(), count);
        return Provider.createPersistence(
                DIRECTORY, 
                "test",
                TreeType.BPLUSTREE,
                RecordCacheType.NONE, 
                FileManagerType.FILE_SYSTEM_NO_TREE, 
                FileManagerCacheType.NONE, 
                TransactionType.SINGLE_WRITER_IN_MEMORY,
                hints);
    }
    
//    @Test
    public void testTransactionWraping() throws Exception
    {
        System.out.println("testTransactionWraping");
        IPersistence instance = buildSimpleTransactionWraping(64);
        
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
        
        instance.truncate();
    }
    
//    @Test
    public void testTransactionalInserts() throws Exception
    {
        System.out.println("testTransactionWraping");
        IPersistence instance = buildSimpleTransactionWraping(64);
        
        Random random = new Random();
        
        int count = 100001;
        int check = 1000;
        int commit = 10001;
        
        instance.beginTransaction();
        for(int i = 0; i < count; i++)
        {
            int randomValue = random.nextInt(200000000) + 1;
            MockValue value = new MockValue();
            value.id = randomValue;
            value.content = "hello world number " + randomValue;
            instance.insert(randomValue, value, true);
            if (i % check == 0)
            {
                System.out.println("inserted " + i + " records");
            }
            if (i % commit == 0)
            {
                instance.commit();
                instance.beginTransaction();
            }
        }
        instance.commit();
        
        System.out.println("number of records: " + instance.size());
        
        instance.truncate();
    }
    
    public class Counter
    {
        public static final long NULL = -1;
        
        private long max;
        
        private long counter;
        
        private int check;
        
        public Counter(long max, int check)
        {
            this.max    = max;
            this.check  = check;
            counter     = 0;
        }
        
        public synchronized long getCount()
        {
            if (counter >= max)
            {
                return NULL;
            }
            if (counter % check == 0)
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
        final IPersistence instance = buildSimpleTransactionWraping(64);
        final Counter counter = new Counter(100001, 100);
        final Random random = new Random();
        
        Thread[] inserts = new Thread[3];
        for(int i = 0; i < inserts.length; i++)
        {
            inserts[i] = new Thread()
            {
                @Override
                public void run()
                {
                    try 
                    {
                        ArrayList<MockValue> values = new ArrayList<MockValue>();
                        long countAt = 0;
                        while(true)
                        {
                            for(int i = 0; i < 100; i++)
                            {
                                countAt = counter.getCount();
                                if (countAt != Counter.NULL)
                                {
                                    int randomValue = random.nextInt(2000000) + 1;
                                    MockValue value = new MockValue();
                                    value.id = randomValue;
                                    value.content = "hello world number " + randomValue;
                                    values.add(value);
                                }
                            }
                            if (values.isEmpty())
                            {
                                break;
                            }
                            else
                            {
                                instance.beginTransaction();
                                try
                                {
                                    while(!values.isEmpty())
                                    {
                                        MockValue value = values.remove(0);
                                        instance.insert(value.id, value, true);
                                    }
                                    instance.commit();
                                }
                                catch(Exception ex)
                                {
                                    instance.rollback();
                                    throw new IllegalArgumentException("error at " + countAt, ex);
                                }
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        throw new IllegalArgumentException(ex.getMessage(), ex);
                    }
                }
            };
            inserts[i].start();
        }
        
        for(int i = 0; i < inserts.length; i++)
        {
            inserts[i].join();
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
        
        
        KeyValuePair<Long, Object> iterator = null;
        long keyat = 0;
        int _counter = 1;// start at one because of curr
        
        IBrowser<KeyValuePair<Long, Object>> browserForward = instance.browse();
        iterator = new KeyValuePair<Long, Object>();
        Assert.assertTrue(browserForward.curr(iterator));
        while(browserForward.next(iterator))
        {
            Assert.assertTrue(keyat < iterator.getKey());
            Assert.assertTrue(iterator.getValue() instanceof MockValue);
            keyat = iterator.getKey();
            _counter++;
            if (_counter % 100 == 0)
            {
                System.out.println("read " + _counter + " records");
            }
        }
        browserForward.finished();
        Assert.assertEquals(_counter, instance.size());
        
        System.out.println("instance size: " + instance.size());
        instance.truncate();
    }
}
