/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.persistence;

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
public class ByteArrayTest {
    
    public ByteArrayTest() {
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

//    /**
//     * Test of read method, of class ByteArray.
//     */
//    @Test
//    public void testRead_byteArr_int() {
//        System.out.println("read");
//        byte[] dest = null;
//        int start = 0;
//        ByteArray instance = new ByteArray();
//        instance.read(dest, start);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of read method, of class ByteArray.
//     */
//    @Test
//    public void testRead_4args() {
//        System.out.println("read");
//        int start = 0;
//        byte[] dest = null;
//        int destPos = 0;
//        int length = 0;
//        ByteArray instance = new ByteArray();
//        instance.read(start, dest, destPos, length);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of readBoolean method, of class ByteArray.
     */
    @Test
    public void testReadWriteBoolean() {
        System.out.println("readBoolean");
        ByteArray instance = new ByteArray();
        
        instance.writeBoolean(true, 0);
        instance.writeBoolean(false, 1);
        instance.writeBoolean(true, instance.compacity());
        
        assertEquals(true, instance.readBoolean(0));
        assertEquals(false, instance.readBoolean(1));
        assertEquals(true, instance.readBoolean(instance.compacity() - 1));
        
        instance.writeBoolean(true, 1);
        assertEquals(true, instance.readBoolean(1));
    }

    /**
     * Test of readShort method, of class ByteArray.
     */
    @Test
    public void testReadWriteShort() {
        System.out.println("readShort");
        ByteArray instance = new ByteArray();
        
        instance.writeShort((short) 12, 0);
        instance.writeShort((short) 15, 2);
        instance.writeShort((short) 13, instance.compacity());
        
        assertEquals(12, instance.readShort(0));
        assertEquals(15, instance.readShort(2));
        assertEquals(13, instance.readShort(instance.compacity() - 2));
        
        instance.writeShort((short) 14, 2);
        assertEquals(14, instance.readShort(2));
    }

    /**
     * Test of readInt method, of class ByteArray.
     */
    @Test
    public void testReadWriteInt() {
        System.out.println("readInt");
        ByteArray instance = new ByteArray();
        
        instance.writeInt(12, 0);
        instance.writeInt(1234, 4);
        instance.writeInt(13, instance.compacity());
        
        assertEquals(12, instance.readInt(0));
        assertEquals(1234, instance.readInt(4));
        assertEquals(13, instance.readInt(instance.compacity() - 4));
        
        instance.writeInt(14, 4);
        assertEquals(14, instance.readInt(4));
    }

    /**
     * Test of readLong method, of class ByteArray.
     */
    @Test
    public void testReadWriteLong() {
        System.out.println("readLong");
        ByteArray instance = new ByteArray();
        
        instance.writeLong(12, 0);
        instance.writeLong(12345678, 8);
        instance.writeLong(13, instance.compacity());
        
        assertEquals(12, instance.readLong(0));
        assertEquals(12345678, instance.readLong(8));
        assertEquals(13, instance.readLong(instance.compacity() - 8));
        
        instance.writeLong(14, 8);
        assertEquals(14, instance.readLong(8));
    }

    /**
     * Test of insert method, of class ByteArray.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        ByteArray instance = new ByteArray();
        
        instance.writeInt(12, 0);
        instance.writeInt(15, 4);
        instance.writeInt(13, instance.compacity());
        
        instance.insert("hello".getBytes(), 4);
        
        byte[] dest = new byte["hello".getBytes().length];
        instance.read(dest, 4);
        
        assertEquals(12, instance.readInt(0));
        assertEquals("hello", new String(dest));
        assertEquals(15, instance.readInt(4 + "hello".getBytes().length));
        assertEquals(13, instance.readInt(instance.compacity() - 4));
    }

    /**
     * Test of insertSome method, of class ByteArray.
     */
    @Test
    public void testInsertSome() {
        System.out.println("insertSome");
        ByteArray instance = new ByteArray();
        
        instance.writeInt(12, 0);
        instance.writeInt(15, 4);
        instance.writeInt(13, instance.compacity());
        
        instance.insertSome("hello".getBytes(), 0, 4);
        
        byte[] dest = new byte["hello".getBytes().length];
        instance.read(dest, 0);
        
        assertEquals("hello", new String(dest));
        assertEquals(15, instance.readInt(5));
        assertEquals(13, instance.readInt(instance.compacity() - 4));
    }

//    /**
//     * Test of write method, of class ByteArray.
//     */
//    @Test
//    public void testWrite_byteArr_int() {
//        System.out.println("write");
//        byte[] src = null;
//        int pos = 0;
//        ByteArray instance = new ByteArray();
//        instance.write(src, pos);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of write method, of class ByteArray.
//     */
//    @Test
//    public void testWrite_4args() {
//        System.out.println("write");
//        int start = 0;
//        byte[] src = null;
//        int srcPos = 0;
//        int length = 0;
//        ByteArray instance = new ByteArray();
//        instance.write(start, src, srcPos, length);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class ByteArray.
//     */
//    @Test
//    public void testDelete() {
//        System.out.println("delete");
//        int pos = 0;
//        int size = 0;
//        ByteArray instance = new ByteArray();
//        instance.delete(pos, size);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resize method, of class ByteArray.
//     */
//    @Test
//    public void testResize() {
//        System.out.println("resize");
//        int newLength = 0;
//        ByteArray instance = new ByteArray();
//        instance.resize(newLength);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of compacity method, of class ByteArray.
//     */
//    @Test
//    public void testCompacity() {
//        System.out.println("compacity");
//        ByteArray instance = new ByteArray();
//        int expResult = 0;
//        int result = instance.compacity();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of compacityUsed method, of class ByteArray.
//     */
//    @Test
//    public void testCompacityUsed() {
//        System.out.println("compacityUsed");
//        ByteArray instance = new ByteArray();
//        int expResult = 0;
//        int result = instance.compacityUsed();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of array method, of class ByteArray.
//     */
//    @Test
//    public void testArray() {
//        System.out.println("array");
//        ByteArray instance = new ByteArray();
//        byte[] expResult = null;
//        byte[] result = instance.array();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of minimize method, of class ByteArray.
//     */
//    @Test
//    public void testMinimize() {
//        System.out.println("minimize");
//        ByteArray instance = new ByteArray();
//        instance.minimize();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of copyUsedBytes method, of class ByteArray.
//     */
//    @Test
//    public void testCopyUsedBytes() {
//        System.out.println("copyUsedBytes");
//        ByteArray instance = new ByteArray();
//        byte[] expResult = null;
//        byte[] result = instance.copyUsedBytes();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of bytesEqual method, of class ByteArray.
//     */
//    @Test
//    public void testBytesEqual() {
//        System.out.println("bytesEqual");
//        ByteArray array = null;
//        ByteArray instance = new ByteArray();
//        boolean expResult = false;
//        boolean result = instance.bytesEqual(array);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
