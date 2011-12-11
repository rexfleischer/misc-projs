/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.bplustree;

import com.rf.fled.persistence.Browser;
import com.rf.fled.persistence.filemanager.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.KeyValuePair;
import com.rf.fled.persistence.Persistence;
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
    
    public Persistence getBPlusTree(int count) throws FledPersistenceException 
    {
        FileManager fileManager = new FileManager_FileSystemNoTree(DIRECTORY, 0);
        Persistence instance = BPlusTree.createBPlusTree(fileManager, "test", count, null, null);
        return instance;
    }
    
    @Test
    public void happyPath() throws Exception
    {
        System.out.println("happyPath");
        
        Persistence instance = getBPlusTree(64);
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
        
        Browser<KeyValuePair<Long, Object>> browserForward = instance.browse();
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
        Assert.assertEquals(counter, instance.size());
        
        // now go backwards
        Browser<KeyValuePair<Long, Object>> browserBackward = 
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
        Assert.assertEquals(counter, instance.size());
        
        System.out.println("number of records: " + instance.size());
        
        
        instance.truncate();
    }
}
