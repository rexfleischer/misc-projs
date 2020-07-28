/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.action;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.TestSuite;
import com.fledwar.blackbox.connection.BlackboxConnection;
import com.fledwar.blackbox.scope.SimulationScope;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.vto.galaxy.unit.GalaxyUnit;
import com.fledwar.vto.galaxy.unit.UnitAction;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.util.SpaceConstents;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.SessionId;
import junit.framework.Assert;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class UnitImpulseTest
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
  
//  @Test
  public void testCancelActionAfterStart() 
          throws Exception
  {
    System.out.println("testCancelActionAfterStart");
    
    
    GalaxyUnit unit1 = getUnit();
    ObjectOrientation or1 = unit1.getOrientation();
    
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance() - 10*SpaceConstents.AU
            }));
    
    Thread.sleep(2000);
    
    SimulationScope scope = engine.getBlackboxSimulation().
            getScope(unit1.getScope());
    ScopeData scope_data = scope.getScopeData();
    UnitAction action = scope_data.unit_actions.get(unit1.getId()).get(0);
    Assert.assertTrue(action.hasStarted());
    Assert.assertFalse(action.hasCanceled());
    
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.cancelaction",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "action_id", action.getId().toString()
            }));
    
    Thread.sleep(2000);
    Assert.assertTrue(action.hasStarted());
    Assert.assertTrue(action.hasFinished());
    Assert.assertTrue(scope_data.unit_actions.isEmpty());
  }
  
//  @Test
  public void testStactedActions() 
          throws Exception
  {
    System.out.println("testStactedActions");
    
    
    GalaxyUnit unit1 = getUnit();
    ObjectOrientation or1 = unit1.getOrientation();
    SimulationScope scope = engine.getBlackboxSimulation().
            getScope(unit1.getScope());
    ScopeData scope_data = scope.getScopeData();
    
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance() - 3*SpaceConstents.AU
            }));
    UnitAction action1 = scope_data.unit_actions.get(unit1.getId()).get(0);
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance()
            }));
    UnitAction action2 = scope_data.unit_actions.get(unit1.getId()).get(1);
    
    Assert.assertTrue(action1.hasStarted());
    Assert.assertFalse(action2.hasStarted());
    
    Thread.sleep(10000);
    
    Assert.assertTrue(action1.hasFinished());
    Assert.assertTrue(action2.hasFinished());
  }
  
  @Test
  public void testStactedActionsAndCanceled() 
          throws Exception
  {
    System.out.println("testStactedActionsAndCanceled");
    
    
    GalaxyUnit unit1 = getUnit();
    ObjectOrientation or1 = unit1.getOrientation();
    SimulationScope scope = engine.getBlackboxSimulation().
            getScope(unit1.getScope());
    ScopeData scope_data = scope.getScopeData();
    
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance() - 3*SpaceConstents.AU
            }));
    UnitAction action1 = scope_data.unit_actions.get(unit1.getId()).get(0);
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance()
            }));
    UnitAction action2 = scope_data.unit_actions.get(unit1.getId()).get(1);
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.impulse",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "end_alpha", or1.getAlpha(),
                         "end_dist", or1.getDistance() - SpaceConstents.AU
            }));
    UnitAction action3 = scope_data.unit_actions.get(unit1.getId()).get(2);
    
    Assert.assertTrue(action1.hasStarted());
    Assert.assertFalse(action2.hasStarted());
    Assert.assertFalse(action3.hasStarted());
    
    Assert.assertFalse(action1.hasCanceled());
    Assert.assertFalse(action2.hasCanceled());
    Assert.assertFalse(action3.hasCanceled());
    
    connection.action(GroovyWrapper.convertArrayToMap(
            new Object[]{"action_type", "unit.cancelaction",
                         "scope", unit1.getScope().toString(),
                         "unit_id", unit1.getId().toString(),
                         "action_id", action2.getId().toString()
            }));
    
    Assert.assertFalse(action1.hasCanceled());
    Assert.assertTrue(action2.hasCanceled());
    Assert.assertFalse(action3.hasCanceled());
    
    
    Thread.sleep(10000);
    
    Assert.assertTrue(action1.hasFinished());
    Assert.assertTrue(action2.hasFinished());
    Assert.assertTrue(action3.hasFinished());
  }
}
