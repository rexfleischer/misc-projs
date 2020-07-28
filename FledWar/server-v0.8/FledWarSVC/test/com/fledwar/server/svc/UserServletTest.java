/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.svc;

import com.fledwar.dao.GameUserDAO;
import com.fledwar.server.FledWarServer;
import com.fledwar.vto.user.GameUser;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class UserServletTest
{

  @Before
  public void setUp() throws Exception
  {
    FledWarServer.start();
  }

  @After
  public void tearDown() throws Exception
  {
    FledWarServer.shutdown();
  }
  
  public GameUser getUser(String name) 
          throws Exception
  {
    try(GameUserDAO dao = FledWarServer.getDAOFactoryRegistry()
            .get(GameUserDAO.class)) {
      return dao.findByUsername(name);
    }
  }
  
  public Map login()
          throws Exception
  {
    Map login_input = new HashMap();
    login_input.put("username", new Object[]{"testing1"});
    login_input.put("password", new Object[]{"testing"});
    Map input = new HashMap();
    input.put("query", login_input);
    return (Map) FledWarServer.getEngine().command(
            "servlet/user/attempt_login.groovy", 
            input);
  }

  @Test
  public void testAttemptLogin() throws Exception
  {
    System.out.println("testAttemptLogin");
    
    System.out.println(login());
  }

  @Test
  public void testAttemptLogout() throws Exception
  {
    System.out.println("testAttemptLogout");
    
    login();
    
    GameUser user = getUser("testing1");
    Assert.assertNotNull(user);
    Assert.assertNotNull(user.getSessionId());
    
    Map input = new HashMap();
    input.put("user", user);
    Object result = FledWarServer.getEngine().command(
            "servlet/user/attempt_logout.groovy", 
            input);
    System.out.println(result);
    
    user = getUser("testing1");
    Assert.assertNotNull(user);
    Assert.assertNull(user.getSessionId());
    
  }

  @Test
  public void testGetStartLocation() throws Exception
  {
    System.out.println("testGetStartLocation");
    
    login();
    
    Map input = new HashMap();
    input.put("user", getUser("testing1"));
    Object result = FledWarServer.getEngine().command(
            "servlet/user/start_location.groovy", 
            input);
    System.out.println(result);
    
  }
}
