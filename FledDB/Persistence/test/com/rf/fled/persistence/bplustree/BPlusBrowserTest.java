/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.IBrowser;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.IPersistence;
import com.rf.fled.persistence.Provider;
import com.rf.fled.persistence.ProviderHint;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.transaction.TransactionType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.tree.TreeType;
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
public class BPlusBrowserTest {
    
    public static final String DIRECTORY = "C:/Users/REx/Desktop/fledhome/data";
    
    public BPlusBrowserTest() {
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
    
    public IPersistence getBPlusTree(int count) throws FledPersistenceException 
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
    
    @Test
    public void happyPath() throws Exception
    {
        System.out.println("happyPath");
        
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
        
        KeyValuePair<Long, Object> iterator = null;
        long keyat = 0;
        int counter = 1;// start at one because of curr
        
        IBrowser<KeyValuePair<Long, Object>> browserForward = instance.browse();
        iterator = new KeyValuePair<Long, Object>();
        Assert.assertTrue(browserForward.curr(iterator));
        while(browserForward.next(iterator))
        {
            Assert.assertTrue(keyat < iterator.getKey());
            Assert.assertTrue(iterator.getValue() instanceof MockValue);
            keyat = iterator.getKey();
            counter++;
            if (counter % 100 == 0)
            {
                System.out.println("read " + counter + " records");
            }
        }
        browserForward.finished();
        Assert.assertEquals(counter, instance.size());
        
        // now go backwards
        IBrowser<KeyValuePair<Long, Object>> browserBackward = 
                instance.browse(Long.MAX_VALUE);// get end of tree
        iterator = new KeyValuePair<Long, Object>();
        counter = 1;
        Assert.assertTrue(browserBackward.curr(iterator));
        while(browserBackward.prev(iterator))
        {
            Assert.assertTrue(keyat > iterator.getKey());
            Assert.assertTrue(iterator.getValue() instanceof MockValue);
            keyat = iterator.getKey();
            counter++;
            if (counter % 100 == 0)
            {
                System.out.println("read " + counter + " records");
            }
        }
        browserForward.finished();
        Assert.assertEquals(counter, instance.size());
        
        System.out.println("number of records: " + instance.size());
        
        
        instance.truncate();
    }
}
