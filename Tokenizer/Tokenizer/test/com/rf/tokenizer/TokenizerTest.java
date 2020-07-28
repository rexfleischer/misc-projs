/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.tokenizer;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author REx
 */
public class TokenizerTest
{
    
    public TokenizerTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of hasNext method, of class Tokenizer.
     */
    @Test
    public void testHasNext()
    {
        System.out.println("hasNext");
        Tokenizer instance = null;
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of next method, of class Tokenizer.
     */
    @Test
    public void testNext()
    {
        System.out.println("next");
        Tokenizer instance = null;
        Token expResult = null;
        Token result = instance.next();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
