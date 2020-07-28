/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.generate;

import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.vto.galaxy.system.SystemPoint;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import com.fledwar.vto.galaxy.util.ScopeData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author REx
 */
@RunWith(Parameterized.class)
public class BasicSystemScripts
{

  @BeforeClass
  public static void setUpClass() throws Exception
  {
    Log4JHelper.startDefaultLogger();
    GroovyWrapper.init("../GroovyScripts/src/scriptroot/");
  }

  @AfterClass
  public static void tearDownClass()
  {
    GroovyWrapper.shutdown();
  }

  @Parameterized.Parameters
  public static List<Object[]> getData()
  {
    List<Object[]> result = new ArrayList<>();
    result.add(new Object[]{"random/system/unary.groovy"});
    result.add(new Object[]{"random/system/binary.groovy"});
    result.add(new Object[]{"random/system/system.groovy"});
    return result;
  }

  @Before
  public void setUp() throws Exception
  {

  }

  @After
  public void tearDown() throws Exception
  {

  }

  String script;

  public BasicSystemScripts(String script)
  {
    this.script = script;
  }

  @Test
  public void testBasicGenerate() throws Exception
  {
    System.out.println(String.format("testing %s: %s",
                                     script, "testBasicGenerate"));

    for (int i = 0; i < 10; i++) {
      long start = System.currentTimeMillis();
      ScopeData instance = (ScopeData) GroovyWrapper.runScript(
              script,
              "name", "test");
      Assert.assertNotNull(instance);
      Assert.assertEquals("test", instance.scope.getName());

      Assert.assertNotNull(instance.points);
      Assert.assertFalse(instance.points.isEmpty());
      for (Entry<ObjectId, SystemPoint> entry : instance.points.entrySet()) {
        SystemPoint point = entry.getValue();
        
        ObjectOrientation orientation = point.getObjectOrientation();
        Double radius = point.getAsDouble("radius");
        if (point.getMass() <= 0 ||
            point.getId() == null ||
            point.getScope() == null ||
            (radius != null ? radius <= 0 : false) ||
            orientation.getDistance() < 0.0 ||
            orientation.getDeltaAlpha() < 0.0
            ) {
          Assert.fail(point.toPrettyString());
        }
        
//        Assert.assertTrue(point.getMass() > 0);
//        Assert.assertNotNull(point.getId());
//        Assert.assertNotNull(point.getScope());
//        if (point.get("radius") != null) {
//          double radius = point.getAsDouble("radius");
//          Assert.assertTrue(radius > 0);
//        }
//        
//        Assert.assertTrue(orientation.getDistance() >= 0.0);
//        Assert.assertTrue(orientation.getDeltaAlpha() >= 0.0);
      }

      System.out.println(System.currentTimeMillis() - start);
      System.out.println(instance.toPrettyString());
//            System.out.println(instance.getMass());
    }
  }

}
