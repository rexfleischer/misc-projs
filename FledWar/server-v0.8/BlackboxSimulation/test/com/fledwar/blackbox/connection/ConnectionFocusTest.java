/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.connection;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.TestSuite;
import com.fledwar.blackbox.scope.SimulationFeatureDrop;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.util.JsonHelper;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.SessionId;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class ConnectionFocusTest
{
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
  
  public ObjectId getScopeOfUser()
          throws Exception
  {
    try(GalaxyUnitDAO dao = engine.getDAOFactoryRegistry()
            .get(GalaxyUnitDAO.class)) {
      return dao.findAll().get(0).getScope();
    }
  }
  
  public GameUser getUserFromMongo()
          throws Exception
  {
    try(GameUserDAO dao = engine.getDAOFactoryRegistry()
            .get(GameUserDAO.class)) {
      return dao.findOneFromId(connection.getUser().getId());
    }
  }
  
  BlackboxEngine engine;
  
  BlackboxConnection connection;
  
  @Test
  public void testFocusSingleSystem() throws Exception {
    System.out.println("testQuerySingleSystem");
    
    ObjectId scope_id = getScopeOfUser();
    
    TestSuite.TestCallbacker callback = new TestSuite.TestCallbacker();
    Map input = new HashMap();
    input.put("focus_type", "galaxy_scope");
    input.put("scope_id", scope_id);
    input.put("callback", callback);
    SimulationFeatureDrop drop = connection.focus(input);
    
    {
      Object update = callback.getDrop(3000);
      Assert.assertNotNull(update);
//      System.out.println(update);
    }
    
    {
      // cancel and clear drops
      drop.cancel();
      while(callback.getDrop(300) != null) {}
      
      Object update = callback.getDrop(3000);
      Assert.assertNull(update);
    }
  }

  @Test
  public void testDropFocusAction() throws Exception {
    System.out.println("testDropFocusAction");
    
    ObjectId scope_id = getScopeOfUser();

    String drop_key;
    SimulationFeatureDrop drop;
    {
      TestSuite.TestCallbacker callback = new TestSuite.TestCallbacker();
      Map input = new HashMap();
      input.put("focus_type", "galaxy_scope");
      input.put("scope_id", scope_id);
      input.put("callback", callback);
      drop = connection.focus(input);
      drop_key = drop.getDropKey();
      Object update = callback.getDrop(3000);
      Assert.assertNotNull(update);
//      System.out.println(update);
    }

    Assert.assertNotNull(drop_key);
    Assert.assertNotNull(connection.getFocusDrops().get(drop_key));

    // script the drop
    {
      Map input = new HashMap();
      input.put("action_type", "end_focus");
      input.put("drop_key", drop_key);
      Map response = connection.action(input);
      System.out.println(response);
    }
    
    Assert.assertNull(connection.getFocusDrops().get(drop_key));
  }
  
  @Test
  public void testFocusSystem() throws Exception {
    System.out.println("testFocusSystem");
    
    ObjectId scope_id = getScopeOfUser();
    
    TestSuite.CounterCallback callback = new TestSuite.CounterCallback();
    Map input = new HashMap();
    input.put("focus_type", "galaxy_scope");
    input.put("scope_id", scope_id);
    input.put("callback", callback);
    SimulationFeatureDrop drop = connection.focus(input);
    
    Thread.sleep(10000);
    
    System.out.println(JsonHelper.toPrettyJson(callback.point_updates));
    System.out.println(JsonHelper.toPrettyJson(callback.unit_updates));
    
    Assert.assertTrue(callback.unit_updates.size() > 4);
    Assert.assertTrue(callback.point_updates.size() > 4);
  }
}
