/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.connection;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.TestSuite;
import com.fledwar.blackbox.scope.SimulationScope;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.vto.galaxy.unit.GalaxyUnit;
import com.fledwar.vto.galaxy.unit.UnitAction;
import com.fledwar.vto.galaxy.unit.UnitActionType;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.util.SpaceConstents;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.SessionId;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class ConnectionActionTest
{
  @Before
  public void setUp() throws Exception
  {
    engine = TestSuite.startEngine();
    SessionId session_id = engine.userLogin("rexf", "testing");
    connection = engine.getBlackboxConnection("rexf", session_id);
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
  
  public GalaxyUnit getUnit()
          throws Exception 
  {
    try(GalaxyUnitDAO dao = engine.getDAOFactoryRegistry()
            .get(GalaxyUnitDAO.class)) {
      return dao.findAll().get(0);
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
  public void testQueryPing() throws Exception
  {
    System.out.println("testQueryPing");
    
    Map actual_input = GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "ping"});
    Object result = connection.action(actual_input);
    Assert.assertNotNull(result);
    System.out.println(result);
  }
  
  @Test
  public void testSetSessionVar() throws Exception
  {
    System.out.println("testSetSessionVar");
    
    Map actual_input = GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "set_session_var",
                         "key", "test",
                         "value", "hello world!"});
    Object result = connection.action(actual_input);
    Assert.assertNotNull(result);
    System.out.println(result);
    
    GameUser user = getUserFromMongo();
    Assert.assertEquals("hello world!", user.getSessionVars().get("test"));
  }
  
  @Test
  public void testSetUserSetting() throws Exception
  {
    System.out.println("testSetUserSetting");
    
    Map actual_input = GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "set_user_setting",
                         "key", "start_system",
                         "value", getScopeOfUser().toString()});
    Object result = connection.action(actual_input);
    Assert.assertNotNull(result);
    System.out.println(result);
    
    GameUser user = getUserFromMongo();
    Assert.assertEquals(getScopeOfUser().toString(), 
                        user.getUserSettings().get("start_system"));
  }
  
  @Test
  public void testUnitImpulse1() throws Exception
  {
    System.out.println("testUnitImpulse1");
    
    GalaxyUnit unit1 = getUnit();
    ObjectOrientation or1 = unit1.getOrientation();
    
    Map actual_input = GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance() - 3.5*SpaceConstents.AU
            });
    Object result = connection.action(actual_input);
    Assert.assertNotNull(result);
    System.out.println(result);
    
    
    SimulationScope scope = engine.getBlackboxSimulation().
            getScope(unit1.getScope());
    ScopeData scope_data = scope.getScopeData();
    
    Assert.assertNotNull(scope_data.scope);
    Assert.assertNotNull(scope_data.points);
    Assert.assertNotNull(scope_data.units);
    Assert.assertNotNull(scope_data.unit_actions);
    
    Assert.assertNotNull(scope_data.units.get(unit1.getId()));
    Assert.assertNotNull(scope_data.unit_actions.get(unit1.getId()));
    Assert.assertFalse(scope_data.unit_actions.get(unit1.getId()).isEmpty());
    
    UnitAction action = scope_data.unit_actions.get(unit1.getId()).get(0);
    Assert.assertEquals(action.getUnitId(), unit1.getId());
    Assert.assertEquals(action.getScope(), scope.getScopeId());
    Assert.assertEquals(action.getActionType(), UnitActionType.IMPULSE);
    Assert.assertFalse(action.hasCanceled());
//    Assert.assertEquals(action.get("end_x"), 0.0);
//    Assert.assertEquals(action.get("end_y"), 0.0);
    
    // an entire action should complete in this time
    Thread.sleep(8000);
    
    Assert.assertTrue(action.hasStarted());
    Assert.assertTrue(action.hasFinished());
    Assert.assertTrue(scope_data.unit_actions.isEmpty());
  }
  
}
