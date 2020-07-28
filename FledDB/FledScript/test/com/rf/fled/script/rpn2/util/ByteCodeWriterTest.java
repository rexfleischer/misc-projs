/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.util;

import com.rf.fled.script.rpn2.environment.FunctionSet_Lang;
import com.rf.fled.script.tokenizer.InfixToRPNConverter;
import junit.framework.Assert;
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
public class ByteCodeWriterTest
{
    
    public ByteCodeWriterTest ()
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
     * Test of setTag_Permanent method, of class ByteCodeWriter.
     */
    @Test
    public void testSetTag_Permanent ()
    {
        System.out.println("setTag_Permanent");
        ByteCodeWriter instance = new ByteCodeWriter(new InfixToRPNConverter(new FunctionSet_Lang()));
        
        for (int i = 0; i < 10; i++)
        {
            instance.writeOperation(ByteCodeOperation.TYPE_B);
        }
        
        instance.setTag_Permanent("hello");
        
        for (int i = 0; i < 10; i++)
        {
            instance.writeOperation(ByteCodeOperation.TYPE_B);
        }
        
        instance.writeGotoTag("hello");
        
        for (int i = 0; i < 10; i++)
        {
            instance.writeOperation(ByteCodeOperation.TYPE_B);
        }
        
        byte[] finished = instance.finished();
        
        Assert.assertEquals(36, finished.length);
        
        ByteArray expected = new ByteArray(new byte[]{
            0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02, 0x00,
            0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02, 0x76, 0x00,0x00,0x00,0x0a, 
            0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,});
        
        ByteArray actual = new ByteArray(finished);
        
        Assert.assertTrue(expected.bytesEqual(actual));
    }

    /**
     * Test of setTag_Temp method, of class ByteCodeWriter.
     */
    @Test
    public void testSetTag_Temp () throws Exception
    {
        System.out.println("setTag_Temp");
        ByteCodeWriter instance = new ByteCodeWriter(new InfixToRPNConverter(new FunctionSet_Lang()));
        
        
        
        for (int i = 0; i < 10; i++)
        {
            instance.writeOperation(ByteCodeOperation.TYPE_B);
        }
        
        instance.setTag_Temp("hello");
        
        for (int i = 0; i < 10; i++)
        {
            instance.writeOperation(ByteCodeOperation.TYPE_B);
        }
        
        instance.resolveTag("hello");
        
        for (int i = 0; i < 10; i++)
        {
            instance.writeOperation(ByteCodeOperation.TYPE_B);
        }
        
        byte[] finished = instance.finished();
        
        Assert.assertEquals(36, finished.length);
        
        ByteArray expected = new ByteArray(new byte[]{
            0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02, 0x76, 0x00,0x00,0x00,0x19,
            0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02, 0x00, 
            0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,});
        
        ByteArray actual = new ByteArray(finished);
        
        Assert.assertTrue(expected.bytesEqual(actual));
    }
}
