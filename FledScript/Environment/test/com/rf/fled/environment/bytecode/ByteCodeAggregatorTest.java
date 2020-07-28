/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.bytecode;

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
public class ByteCodeAggregatorTest
{
    
    public ByteCodeAggregatorTest ()
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
     * Test of getTotal method, of class ByteCodeAggregator.
     */
    @Test
    public void testGetTotal () throws Exception
    {
        System.out.println("getTotal");
        ByteCodeAggregator instance = new ByteCodeAggregator();
        
        assertEquals(0, instance.getTotal());
        
        byte[] bytes = new byte[100];
        instance.write(bytes);
        assertEquals(100, instance.getTotal());
        
        instance.write(bytes);
        instance.write(bytes);
        instance.write(bytes);
        instance.write(bytes);
        instance.write(bytes);
        assertEquals(600, instance.getTotal());
        
        instance.write(bytes);
        assertEquals(700, instance.getTotal());
    }

    /**
     * Test of finished method, of class ByteCodeAggregator.
     */
    @Test
    public void testFinished () throws Exception
    {
        System.out.println("finished");
        ByteCodeAggregator instance = new ByteCodeAggregator();
        
        for(int i = 0; i < 1000; i++)
        {
            instance.writeInt(i);
        }
        
        ByteCode code = instance.finished();
        
        for(int i = 0; i < 1000; i++)
        {
            Assert.assertEquals(i, code.readInt(i * 4));
        }
    }
}
