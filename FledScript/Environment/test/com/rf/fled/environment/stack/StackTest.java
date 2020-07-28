/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.environment.stack;

import com.rf.fled.environment.memory.Stack;
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
public class StackTest
{
    
    public StackTest ()
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
     * Test of pushCall method, of class Stack.
     */
    @Test
    public void test_happypath () throws Exception
    {
        System.out.println("test_happypath");
        Stack instance = new Stack();
        
        Assert.assertEquals(0, instance.memAlloced());
        Assert.assertEquals(Stack.DEFAULT_SIZE - Stack.HEADER, instance.memAvailable());
        
        int[] toPut = new int[16];
        int callAlloc = toPut.length * 4;
        for(int i = 0; i < toPut.length; i++)
        {
            toPut[i] = i + 1;
        }
        
        instance.pushCall(100, callAlloc);
        Assert.assertEquals(callAlloc + Stack.HEADER, instance.memAlloced());
        
        for(int i = 0; i < toPut.length; i++)
        {
            instance.writeInteger(i * 4, toPut[i]);
        }
        
        Assert.assertEquals(callAlloc + Stack.HEADER, instance.memAlloced());
        
        for(int i = 0; i < toPut.length; i++)
        {
            Assert.assertEquals(toPut[i], instance.readInteger(i * 4));
        }
        
        
        
        instance.pushCall(123456, 1048);
        Assert.assertEquals(Stack.HEADER * 2 + 1048 + callAlloc, instance.memAlloced());
        for(int i = 0; i < toPut.length; i++)
        {
            instance.writeInteger(i * 4, toPut[i]);
        }
        
        
        instance.pushCall(123457, 1048);
        Assert.assertEquals(Stack.HEADER * 3 + 1048 * 2 + callAlloc, instance.memAlloced());
        for(int i = 0; i < toPut.length; i++)
        {
            instance.writeInteger(i * 4, toPut[i]);
        }
        for(int i = 0; i < toPut.length; i++)
        {
            Assert.assertEquals(toPut[i], instance.readInteger(i * 4));
        }
        
        
        Assert.assertEquals(123457, instance.popCall());
        
        
        for(int i = 0; i < toPut.length; i++)
        {
            Assert.assertEquals(toPut[i], instance.readInteger(i * 4));
        }
        Assert.assertEquals(123456, instance.popCall());
        for(int i = 0; i < toPut.length; i++)
        {
            Assert.assertEquals(toPut[i], instance.readInteger(i * 4));
        }
        
    }
}
