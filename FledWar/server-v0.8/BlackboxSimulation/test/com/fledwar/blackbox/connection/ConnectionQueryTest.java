/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.connection;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.TestSuite;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.util.SpaceConstents;
import com.fledwar.vto.user.SessionId;
import java.util.HashMap;
import java.util.List;
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
public class ConnectionQueryTest
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
  
  BlackboxEngine engine;
  
  BlackboxConnection connection;
  
  @Test
  public void testQueryConfig() throws Exception
  {
    System.out.println("testScript");
    
    Map actual_input = GroovyWrapper.convertArrayToMap(
            new Object[]{"query_type", "config"});
    Object result = connection.query(actual_input);
    Assert.assertNotNull(result);
    System.out.println(result);
  }
  
  @Test
  public void testQueryUserdata() throws Exception
  {
    System.out.println("testScript");
    
    Map actual_input = GroovyWrapper.convertArrayToMap(
            new Object[]{"query_type", "userdata"});
    Object result = connection.query(actual_input);
    Assert.assertNotNull(result);
    System.out.println(result);
  }
  
  @Test 
  public void testQuerySingleSystem() throws Exception {
    System.out.println("testQuerySingleSystem");
    
    ObjectId scope_id = getScopeOfUser();
    
    {
      Map input = new HashMap();
      input.put("query_type", "system");
      input.put("scope_id", scope_id);
      Map result = (Map) connection.query(input);
      Assert.assertNotNull(result);
      Assert.assertNotNull(result.get("scope"));
      Assert.assertNotNull(result.get("points"));
      Assert.assertNotNull(result.get("units"));
    }
    
    {
      Map input = new HashMap();
      input.put("query_type", "system");
      input.put("scope_id", scope_id);
      input.put("scope", true);
      Map result = (Map) connection.query(input);
      Assert.assertNotNull(result);
      Assert.assertNotNull(result.get("scope"));
      Assert.assertNull(result.get("points"));
      Assert.assertNull(result.get("units"));
    }
    
    {
      Map input = new HashMap();
      input.put("query_type", "system");
      input.put("scope_id", scope_id);
      input.put("scope", true);
      input.put("points", true);
      Map result = (Map) connection.query(input);
      Assert.assertNotNull(result);
      Assert.assertNotNull(result.get("scope"));
      Assert.assertNotNull(result.get("points"));
      Assert.assertNull(result.get("units"));
    }
  }
  
  @Test
  public void testQuerySystemLayout() throws Exception
  {
    System.out.println("testScript");
    
    ObjectId scope_id = getScopeOfUser();
    
    Map input = new HashMap();
    input.put("query_type", "system_layout");
    input.put("scope_id", scope_id);
    input.put("radius", SpaceConstents.LIGHTYEAR * 5);
    List<GalaxyScope> result = (List<GalaxyScope>) connection.query(input);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.size() > 1);
  }
  
}
