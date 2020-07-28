/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.user;

import com.fledwar.vto.user.SessionId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class SessionIdTest
{
    
    public SessionIdTest()
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
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of toString method, of class SessionId.
     */
    @Test
    public void testToString()
    {
        System.out.println("testToString");
        SessionId session_id = new SessionId();
        
        char[] expected = session_id.toCharArray();
        char[] actual = (new SessionId(expected)).toCharArray();
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test of toByteArray method, of class SessionId.
     */
    @Test
    public void testToByteArray()
    {
        System.out.println("toByteArray");
        SessionId session_id = new SessionId();
        
        byte[] bytes = session_id.toByteArray();
        Assert.assertArrayEquals(bytes, (new SessionId(bytes)).toByteArray());
    }
}
