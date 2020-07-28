/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest;

import com.rf.fledrest.router.*;
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
@RunWith (Suite.class)
@Suite.SuiteClasses (
{
    RestRouterTest_default.class,
    RestRouterTest_method.class,
    RestRouterTest_multiple.class,
    RestRouterTest_single.class,
    RestRouterTest_multiplemethod.class,
})
public class NewTestSuite
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
