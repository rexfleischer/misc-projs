/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.filemanager.FileManagerCacheType;
import com.rf.fled.persistence.filemanager.FileManagerType;
import com.rf.fled.persistence.tree.RecordCacheType;
import com.rf.fled.persistence.tree.TreeType;
import com.rf.fled.persistence.Provider;
import com.rf.fled.persistence.ProviderHint;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
    
    public static final String DIRECTORY = "C:/Users/REx/Desktop/fledhome/data";
    
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
    
    public Persistence buildSimpleTransactionWraping(int count) throws FledPersistenceException
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
                TransactionType.IN_MEMORY,
                hints);
    }
    
//    @Test
    public void testTransactionWraping() throws Exception
    {
        System.out.println("testTransactionWraping");
        Persistence instance = buildSimpleTransactionWraping(64);
        
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
    
    @Test
    public void testTransactionalInserts() throws Exception
    {
        System.out.println("testTransactionWraping");
        Persistence instance = buildSimpleTransactionWraping(64);
        
        Random random = new Random();
        
        int count = 10001;
        int check = 1000;
        int commit = 10000;
        
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
}
