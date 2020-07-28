/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.script.rpn2.util;

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
public class OrderedVariableArrayTest
{
    
    public OrderedVariableArrayTest ()
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
     * Test of setVariable method, of class OrderedVariableArray.
     */
    @Test
    public void testSetVariable () throws Exception
    {
        OrderedVariableArray<Integer> instance = new OrderedVariableArray<Integer>(10);
        
        instance.setVariable("hello", 1);
        Assert.assertEquals(1, (int)instance.getVariable("hello"));
        
        instance.setVariable("hello", 1);
        Assert.assertEquals(1, (int)instance.getVariable("hello"));
        
        instance.setVariable("hello", 2);
        Assert.assertEquals(2, (int)instance.getVariable("hello"));
        
        instance.setVariable("hello", 1);
        instance.setVariable("hello", 1);
        instance.setVariable("hello", 7);
        Assert.assertEquals(7, (int)instance.getVariable("hello"));
        
        Assert.assertEquals(1, instance.getCompacityUsed());
        
        Assert.assertTrue(instance.hasVariable("hello"));
        
        
        instance.setVariable("something", 1);
        instance.setVariable("world", 2);
        instance.setVariable("OH_NO!", 3);
        
        Assert.assertEquals(4, instance.getCompacityUsed());
        
        Assert.assertEquals(7, (int)instance.getVariable("hello"));
        Assert.assertEquals(1, (int)instance.getVariable("something"));
        Assert.assertEquals(2, (int)instance.getVariable("world"));
        Assert.assertEquals(3, (int)instance.getVariable("OH_NO!"));
        
        instance.removeVariable("hello");
        Assert.assertEquals(1, (int)instance.getVariable("something"));
        Assert.assertEquals(2, (int)instance.getVariable("world"));
        Assert.assertEquals(3, (int)instance.getVariable("OH_NO!"));
        
        instance.removeVariable("something");
        Assert.assertEquals(2, (int)instance.getVariable("world"));
        Assert.assertEquals(3, (int)instance.getVariable("OH_NO!"));
        
        instance.removeVariable("world");
        Assert.assertEquals(3, (int)instance.getVariable("OH_NO!"));
        
        instance.removeVariable("OH_NO!");
        
        Assert.assertEquals(0, instance.getCompacityUsed());
        
        
        
        instance.setVariable("hello0", 1);
        instance.setVariable("hello1", 11);
        instance.setVariable("hello2", 12);
        instance.setVariable("hello3", 13);
        instance.setVariable("hello4", 14);
        instance.setVariable("hello5", 15);
        instance.setVariable("hello6", 16);
        instance.setVariable("hello7", 17);
        instance.setVariable("hello8", 18);
        instance.setVariable("hello9", 19);
        instance.setVariable("hello10", 10);
        instance.setVariable("hello11", 111);
        instance.setVariable("hello12", 122);
        instance.setVariable("hello13", 133);
        instance.setVariable("hello14", 144);
        instance.setVariable("hello15", 155);
        instance.setVariable("hello16", 166);
        instance.setVariable("hello17", 177);
        instance.setVariable("hello18", 188);
        instance.setVariable("hello19", 199);
        
        Assert.assertEquals(20, instance.getCompacityUsed());
        Assert.assertEquals(1, (int)instance.getVariable("hello0"));
        Assert.assertEquals(11, (int)instance.getVariable("hello1"));
        Assert.assertEquals(12, (int)instance.getVariable("hello2"));
        Assert.assertEquals(13, (int)instance.getVariable("hello3"));
        Assert.assertEquals(14, (int)instance.getVariable("hello4"));
        Assert.assertEquals(15, (int)instance.getVariable("hello5"));
        Assert.assertEquals(16, (int)instance.getVariable("hello6"));
        Assert.assertEquals(17, (int)instance.getVariable("hello7"));
        Assert.assertEquals(18, (int)instance.getVariable("hello8"));
        Assert.assertEquals(19, (int)instance.getVariable("hello9"));
        Assert.assertEquals(10, (int)instance.getVariable("hello10"));
        Assert.assertEquals(111, (int)instance.getVariable("hello11"));
        Assert.assertEquals(122, (int)instance.getVariable("hello12"));
        Assert.assertEquals(133, (int)instance.getVariable("hello13"));
        Assert.assertEquals(144, (int)instance.getVariable("hello14"));
        Assert.assertEquals(155, (int)instance.getVariable("hello15"));
        Assert.assertEquals(166, (int)instance.getVariable("hello16"));
        Assert.assertEquals(177, (int)instance.getVariable("hello17"));
        Assert.assertEquals(188, (int)instance.getVariable("hello18"));
        Assert.assertEquals(199, (int)instance.getVariable("hello19"));
    }
}
