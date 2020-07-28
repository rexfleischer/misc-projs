/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.bytecode;

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
public class ByteCodeTest
{
    
    public ByteCodeTest ()
    {
    }

    @BeforeClass
    public static void setUpClass () throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass () throws Exception
    {
    }
    
    @Before
    public void setUp ()
    {
    }
    
    @After
    public void tearDown ()
    {
    }

    /**
     * Test of readBoolean method, of class ByteArray.
     */
    @Test
    public void testReadWriteBoolean() {
        System.out.println("readBoolean");
        ByteCode instance = new ByteCode();
        
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
        ByteCode instance = new ByteCode();
        
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
        ByteCode instance = new ByteCode();
        
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
        ByteCode instance = new ByteCode();
        
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
        ByteCode instance = new ByteCode();
        
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
        ByteCode instance = new ByteCode();
        
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
}
