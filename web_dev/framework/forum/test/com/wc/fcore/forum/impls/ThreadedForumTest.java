/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wc.fcore.forum.impls;

import com.wc.fcore.forum.Forumable;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author REx
 */
public class ThreadedForumTest {

    public ThreadedForumTest() {
    }

    /**
     * Test of orderForum method, of class ThreadedForum.
     */
    @Test
    public void testOrderForum() throws Exception {

        for (int i=0; i<10; i++){

        }

        ThreadedForum instance = new ThreadedForum();
        ArrayList expResult = null;
        ArrayList result = instance.orderForum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}