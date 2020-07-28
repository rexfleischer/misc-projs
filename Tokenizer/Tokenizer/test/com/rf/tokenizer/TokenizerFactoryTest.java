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
public class TokenizerFactoryTest
{
    
    public TokenizerFactoryTest()
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
     * Test of addTokenType method, of class TokenizerFactory.
     */
    @Test
    public void testAddTokenType() throws Exception
    {
        System.out.println("addTokenType");
        String definition = "";
        TokenizerFactory instance = new TokenizerFactory();
        instance.addTokenType(definition);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
