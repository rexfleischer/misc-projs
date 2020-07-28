/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.connection;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.TestSuite;
import com.fledwar.blackbox.TestSuite.TestCallbacker;
import com.fledwar.blackbox.scope.SimulationFeatureDrop;
import com.fledwar.vto.user.SessionId;
import java.util.HashMap;
import java.util.Map;
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
public class ConnectionLoginTest
{

  public ConnectionLoginTest() {
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
    SessionId session_id = engine.userLogin("testing", "testing");
    connection = engine.getBlackboxConnection("testing", session_id);
  }

  @After
  public void tearDown() throws Exception 
  {
    if (connection != null) {
      connection.logout();
    }
    TestSuite.shutdownEngine(engine);
  }
  
  BlackboxEngine engine;
  
  BlackboxConnection connection;
  
  /**
   * Test of focus method, of class BlackboxConnection.
   */
  @Test
  public void testUserLoginAndActionRoutes() throws Exception 
  {
    System.out.println("testUserLoginAndActionRoutes");
    
    {
      Map input = new HashMap();
      input.put("action_type", "test");
      Map result = connection.action(input);
      Assert.assertNotNull(result);
      System.out.println(result);
    }
    
    {
      Map input = new HashMap();
      input.put("focus_type", "galaxy_scope");
      input.put("scope_id", engine.getBlackboxSimulation().getScopeIds().get(1));
      input.put("callback", new TestCallbacker());
      SimulationFeatureDrop new_drop = connection.focus(input);
      Assert.assertNotNull(new_drop);
      System.out.println(new_drop);
      
      String drop_key = new_drop.getDropKey();
      Assert.assertNotNull(connection.getFocusDrops().get(drop_key));
    }
    
    {
      Map input = new HashMap();
      input.put("query_type", "test");
      Object result = connection.query(input);
      Assert.assertEquals("hello world!", result);
    }
  }
}
