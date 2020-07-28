/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.tokenizer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author REx
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    com.rf.fled.tokenizer.TokenTypeManagerTestParse.class,
    com.rf.fled.tokenizer.rule.TokenRuleParserTest.class,
    com.rf.fled.tokenizer.rule.TokenTypeManagerTest.class
})
public class TestEverything
{

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }
    
}
