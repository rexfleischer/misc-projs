/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class BlackboxEngineTest
{
    
    public BlackboxEngineTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp() throws Exception
    {
        engine = TestSuite.startEngine();
    }
    
    @After
    public void tearDown() throws Exception
    {
        TestSuite.shutdownEngine(engine);
    }
    
    BlackboxEngine engine;

    @Test
    public void testStart() throws Exception
    {
        System.out.println("testStart");
        
        Thread.sleep(10000);
    }
}
