/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence.transaction;

import com.rf.fled.persistence.FileManager;
import com.rf.fled.persistence.FledPersistenceException;
import com.rf.fled.persistence.Persistence;
import com.rf.fled.persistence.bplustree.BPlusTree;
import com.rf.fled.persistence.filemanager.FileManager_FileSystemNoTree;
import java.io.File;
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
        FileManager fileManager = new FileManager_FileSystemNoTree(DIRECTORY, 0);
//        FileManager fileManager = new FileManager_InMemory();
        Persistence instance = BPlusTree.createBPlusTree(fileManager, "test", count, null, null);
        Persistence result = new TransactionPersistence(fileManager, instance);
        return result;
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
        
        int count = 1001;
        int check = 100;
        int commit = 100;
        
        instance.beginTransaction();
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
