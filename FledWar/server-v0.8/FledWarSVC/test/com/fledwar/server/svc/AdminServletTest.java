/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.svc;

import com.fledwar.server.FledWarServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author REx
 */
@RunWith(Parameterized.class)
public class AdminServletTest
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

  @Parameterized.Parameters
  public static List<Object[]> getData()
  {
    List<Object[]> result = new ArrayList<>();
    result.add(new Object[]{"servlet/admin/config.groovy", new HashMap()});
//    result.add(new Object[]{"servlet/admin/refresh.groovy", new HashMap()});
    result.add(new Object[]{"servlet/admin/reset.groovy", new HashMap()});
    result.add(new Object[]{"servlet/admin/status.groovy", new HashMap()});
    result.add(new Object[]{"servlet/admin/users_logged_in.groovy", new HashMap()});
    return result;
  }
  
  
  private String script;
  
  private Map script_input;

  public AdminServletTest(String script, Map script_input)
  {
    this.script = script;
    this.script_input = script_input;
  }

  @Test
  public void testResetAll() throws Exception
  {
    System.out.println("testResetAll");
    
    Object result = FledWarServer.getEngine().command(script, script_input);
    
//    Assert.assertNotNull(result);
    System.out.println(result);
  }
}
