/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

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
public class RecordFileTest {
    
    public RecordFileTest() {
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

    /**
     * Test of fileSize method, of class RecordFile.
     */
    @Test
    public void testFileSize() {
        System.out.println("fileSize");
        RecordFile instance = new RecordFile();
        
        Assert.assertEquals(76, instance.fileSize());
        
        byte[] bytes = new byte[10];
        instance.insert(bytes, 0);
        Assert.assertEquals(86, instance.fileSize());
        
        bytes = new byte[12];
        instance.write(bytes, 0);
        Assert.assertEquals(88, instance.fileSize());
        
        bytes = new byte[100];
        instance.insert(bytes, 0);
        Assert.assertEquals(188, instance.fileSize());
        
        bytes = new byte[100];
        instance.remove(0);
        Assert.assertEquals(88, instance.fileSize());
        
        bytes = new byte[100];
        instance.remove(0);
        Assert.assertEquals(76, instance.fileSize());
    }

    /**
     * Test of compacityUsed method, of class RecordFile.
     */
    @Test
    public void testCompacityUsed() {
        System.out.println("compacityUsed");
        RecordFile instance = new RecordFile();
        
        Assert.assertEquals(0, instance.compacityUsed());
        
        byte[] bytes = new byte[10];
        instance.insert(bytes, 0);
        Assert.assertEquals(1, instance.compacityUsed());
        
        bytes = new byte[12];
        instance.write(bytes, 0);
        Assert.assertEquals(1, instance.compacityUsed());
        
        bytes = new byte[100];
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        instance.insert(bytes, 0);
        Assert.assertEquals(10, instance.compacityUsed());
        
        bytes = new byte[100];
        instance.remove(0);
        Assert.assertEquals(9, instance.compacityUsed());
        
        bytes = new byte[100];
        instance.remove(0);
        Assert.assertEquals(8, instance.compacityUsed());
    }

    /**
     * Test of compacity method, of class RecordFile.
     */
    @Test
    public void testCompacity() {
        System.out.println("compacity");
        RecordFile instance = new RecordFile();
        
        Assert.assertEquals(RecordFile.DEFAULT_RECORDS, instance.compacity());
        
        instance = new RecordFile(1000);
        Assert.assertEquals(1000, instance.compacity());
    }

    /**
     * Test of recordSize method, of class RecordFile.
     */
    @Test
    public void testRecordSize() {
        System.out.println("recordSize");
        RecordFile instance = new RecordFile();
        
        Assert.assertEquals(0, instance.recordSize(0));
        
        byte[] bytes = new byte[10];
        instance.insert(bytes, 0);
        Assert.assertEquals(10, instance.recordSize(0));
        
        bytes = new byte[12];
        instance.write(bytes, 0);
        Assert.assertEquals(12, instance.recordSize(0));
        
        bytes = new byte[100];
        instance.insert(bytes, 0);
        Assert.assertEquals(100, instance.recordSize(0));
        Assert.assertEquals(12, instance.recordSize(1));
    }

    /**
     * Test of setMeta method, of class RecordFile.
     */
    @Test
    public void testSetAndGetMeta() {
        System.out.println("testSetAndGetMeta");
        RecordFile instance = new RecordFile();
        
        String meta = "happy happy joy joy!";
        String first = "first";
        
        instance.setMeta(meta.getBytes());
        instance.write(first.getBytes(), 0);
        
        Assert.assertEquals(meta, new String(instance.getMeta()));
        Assert.assertEquals(first, new String(instance.read(0)));
    }

    /**
     * Test of write method, of class RecordFile.
     */
    @Test
    public void testWrite() {
        System.out.println("write");
        RecordFile instance = new RecordFile();
        
        String first = "hello";
        String second = "hello 2";
        String third = "hello 3";
        
        instance.write(first.getBytes(), 0);
        instance.write(second.getBytes(), 1);
        instance.write(third.getBytes(), 2);
        
        Assert.assertEquals(first, new String(instance.read(0)));
        Assert.assertEquals(second, new String(instance.read(1)));
        Assert.assertEquals(third, new String(instance.read(2)));
        
        instance.write(third.getBytes(), 1);
        
        Assert.assertEquals(first, new String(instance.read(0)));
        Assert.assertEquals(third, new String(instance.read(1)));
        Assert.assertEquals(third, new String(instance.read(2)));
    }

    /**
     * Test of remove method, of class RecordFile.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        RecordFile instance = new RecordFile();
        
        String first = "hello";
        String second = "hello 2";
        String third = "hello 3";
        
        instance.write(first.getBytes(), 0);
        instance.write(second.getBytes(), 1);
        instance.write(third.getBytes(), 2);
        
        Assert.assertEquals(first, new String(instance.read(0)));
        Assert.assertEquals(second, new String(instance.read(1)));
        Assert.assertEquals(third, new String(instance.read(2)));
        
        instance.write(third.getBytes(), 1);
        
        Assert.assertEquals(first, new String(instance.read(0)));
        Assert.assertEquals(third, new String(instance.read(1)));
        Assert.assertEquals(third, new String(instance.read(2)));
        
        instance.remove(0);
        
        Assert.assertEquals(third, new String(instance.read(0)));
        Assert.assertEquals(third, new String(instance.read(1)));
    }

    /**
     * Test of isFull method, of class RecordFile.
     */
    @Test
    public void testIsFull() {
        System.out.println("isFull");
        RecordFile instance = new RecordFile();
        
        String first = "hello";
        
        for(int i = 0; i < instance.compacity(); i++)
        {
            instance.write((first + i).getBytes(), i);
        }
        
        for(int i = 0; i < instance.compacity(); i++)
        {
            Assert.assertEquals(first + i, new String(instance.read(i)));
        }
        
        Assert.assertEquals(true, instance.isFull());
        
        instance.remove(0);
        
        Assert.assertEquals(false, instance.isFull());
    }

    /**
     * Test of getBytes method, of class RecordFile.
     */
    @Test
    public void testGetBytes() {
        System.out.println("getBytes");
        RecordFile instance = new RecordFile();
        
        String first = "hello";
        String second = "hello 2";
        String third = "hello 3";
        String meta = "hello, I'm meta!";
        
        instance.setMeta(meta.getBytes());
        instance.write(first.getBytes(), 0);
        instance.write(second.getBytes(), 1);
        instance.write(third.getBytes(), 2);
        
        byte[] bytes = instance.getBytes();
        
        instance = new RecordFile(bytes);
        
        Assert.assertEquals(RecordFile.DEFAULT_RECORDS, instance.compacity());
        Assert.assertEquals(meta, new String(instance.getMeta()));
        Assert.assertEquals(first, new String(instance.read(0)));
        Assert.assertEquals(second, new String(instance.read(1)));
        Assert.assertEquals(third, new String(instance.read(2)));
    }

    /**
     * Test of getCopy method, of class RecordFile.
     */
    @Test
    public void testGetCopy() {
        System.out.println("getCopy");
        RecordFile instance = new RecordFile();
        
        String first = "hello";
        String second = "hello 2";
        String third = "hello 3";
        String meta = "hello, I'm meta!";
        
        instance.setMeta(meta.getBytes());
        instance.write(first.getBytes(), 0);
        instance.write(second.getBytes(), 1);
        instance.write(third.getBytes(), 2);
        
        instance = instance.getCopy();
        
        Assert.assertEquals(RecordFile.DEFAULT_RECORDS, instance.compacity());
        Assert.assertEquals(meta, new String(instance.getMeta()));
        Assert.assertEquals(first, new String(instance.read(0)));
        Assert.assertEquals(second, new String(instance.read(1)));
        Assert.assertEquals(third, new String(instance.read(2)));
    }
}
