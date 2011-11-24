/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.presistance.util;

import java.nio.ByteBuffer;
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
    
    @Test
    public void testConstructors()
    {
        ByteArray instance = new ByteArray();
        assertEquals(ByteArray.DEFAULT, instance.compacity());
        assertEquals(0, instance.compacityUsed());
        instance = new ByteArray(128);
        assertEquals(128, instance.compacity());
        assertEquals(0, instance.compacityUsed());
        instance = new ByteArray(new byte[256]);
        assertEquals(256, instance.compacity());
        assertEquals(256, instance.compacityUsed());
        instance = new ByteArray(new byte[256], ByteArray.DEFAULT);
        assertEquals(ByteArray.DEFAULT, instance.compacity());
        assertEquals(256, instance.compacityUsed());
        
        instance = new ByteArray();
        instance.write("some bytes".getBytes(), 0);
        assertEquals("some bytes".getBytes().length, instance.compacityUsed());
        assertEquals(ByteArray.DEFAULT, instance.compacity());
        instance.insert("hello ".getBytes(), 0);
        assertEquals("hello some bytes".getBytes().length, instance.compacityUsed());
        
        ByteArray buffer = new ByteArray();
        buffer.write(0, instance.array(), 0, instance.compacityUsed());
        assertTrue(instance.bytesEqual(buffer));
        
        instance.minimize();
        assertEquals("hello some bytes".getBytes().length, instance.compacity());
        
        instance.insert("there ".getBytes(), "hello ".getBytes().length);
        assertEquals("hello there some bytes", new String(instance.array()));
        
        instance.delete("hello ".getBytes().length, "there ".getBytes().length);
        assertTrue(instance.bytesEqual(new ByteArray("hello some bytes".getBytes())));
    }
}
