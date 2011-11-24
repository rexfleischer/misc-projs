/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.fileio;

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
public class RecordFile_MemByteArrayTest {
    
    public RecordFile_MemByteArrayTest() {
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
    
    private RecordFile_MemByteArray getNew()
    {
        return new RecordFile_MemByteArray(
                System.getenv("user.dir") + "/test", 20);
    }

    /**
     * Test of fileSize method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testFileSize() throws Exception {
        System.out.println("fileSize");
        RecordFile_MemByteArray instance = getNew();
        
        instance.setMeta("hello".getBytes());
        
        assertEquals(92 + "hello".getBytes().length, instance.fileSize());
    }

    /**
     * Test of numOfRecords method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testNumOfRecords() throws Exception {
        System.out.println("numOfRecords");
        RecordFile_MemByteArray instance = getNew();
        
        instance.insert("new object".getBytes(), 0);
        instance.insert("well, hello mr. poopy".getBytes(), 1);
        
        assertEquals("new object", new String(instance.read(0)));
        assertEquals("well, hello mr. poopy", new String(instance.read(1)));
    }

    /**
     * Test of recordCompacity method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testRecordCompacity() throws Exception {
        System.out.println("recordCompacity");
        RecordFile_MemByteArray instance = getNew();
        
        assertEquals(20, instance.recordCompacity());
    }

    /**
     * Test of recordSize method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testRecordSize() throws Exception {
        System.out.println("numOfRecords");
        RecordFile_MemByteArray instance = getNew();
        
        instance.insert("new object".getBytes(), 0);
        instance.insert("well, hello mr. poopy".getBytes(), 1);
        
        assertEquals("new object".getBytes().length, instance.read(0).length);
    }

    /**
     * Test of setMeta method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testSetAndGetMeta() throws Exception {
        System.out.println("setAndGetMeta");
        RecordFile_MemByteArray instance = getNew();
        
        instance.insert("new object".getBytes(), 0);
        instance.insert("well, hello mr. poopy".getBytes(), 1);
        
        instance.setMeta("pooy stuff".getBytes());
        
        assertEquals("new object", new String(instance.read(0)));
        assertEquals("well, hello mr. poopy", new String(instance.read(1)));
        
        instance.setMeta("pooy stuff squared".getBytes());
        
        assertEquals("new object", new String(instance.read(0)));
        assertEquals("well, hello mr. poopy", new String(instance.read(1)));
    }

    /**
     * Test of remove method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testRemove() throws Exception {
        System.out.println("remove");
        RecordFile_MemByteArray instance = getNew();
        
        instance.insert("new object".getBytes(), 0);
        instance.insert("well, hello mr. poopy".getBytes(), 1);
        instance.insert("new object skjfdlksjf".getBytes(), 2);
        instance.insert("well, hello mr. poopy lkfsjdlfkjs".getBytes(), 3);
        
        instance.remove(2);
        
        assertEquals("well, hello mr. poopy lkfsjdlfkjs", new String(instance.read(2)));
    }

    /**
     * Test of insert method, of class RecordFile_MemByteArray.
     */
    @Test
    public void testInsert() throws Exception {
        System.out.println("insert");
        RecordFile_MemByteArray instance = getNew();
        
        instance.insert("new object".getBytes(), 0);
        instance.insert("well, hello mr. poopy".getBytes(), 1);
        instance.insert("new object skjfdlksjf".getBytes(), 2);
        instance.insert("well, hello mr. poopy lkfsjdlfkjs".getBytes(), 3);
        
        instance.insert("new over here".getBytes(), 1);
        
        assertEquals("new over here", new String(instance.read(1)));
    }
}
