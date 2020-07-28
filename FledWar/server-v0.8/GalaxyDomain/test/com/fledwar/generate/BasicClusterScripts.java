/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.generate;

import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.vto.galaxy.util.GalaxyOrientation;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.util.SpaceConstents;
import com.fledwar.vto.galaxy.util.SpaceFunctionUtil;
import java.util.ArrayList;
import java.util.List;
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
public class BasicClusterScripts
{

  @BeforeClass
  public static void setUpClass() throws Exception {
    Log4JHelper.startDefaultLogger();
    GroovyWrapper.init("../GroovyScripts/src/scriptroot/");
  }

  @AfterClass
  public static void tearDownClass() {
    GroovyWrapper.shutdown();
  }

  @Parameterized.Parameters
  public static List<Object[]> getData() {
    List<Object[]> result = new ArrayList<>();
    result.add(new Object[]{"random/cluster/simple.groovy"});
    return result;
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }
  String script;

  public BasicClusterScripts(String script) {
    this.script = script;
  }

  @Test
  public void testBasicGenerate() throws Exception {
    System.out.println(String.format("testing %s: %s",
                                     script, "testBasicGenerate"));

    for(int i = 0; i < 10; i++) {
      long start = System.currentTimeMillis();

      List<ScopeData> cluster = (List<ScopeData>) GroovyWrapper.runScript(
              script,
              "name", "test",
              "center_distance", SpaceConstents.LIGHTYEAR * 1000,
              "center_alpha", Math.PI,
              "cluster_radius", SpaceConstents.LIGHTYEAR * 10,
              "cluster_seperation", SpaceConstents.LIGHTYEAR * 3,
              "cluster_dalpha", 0.000000000001);
      Assert.assertNotNull(cluster);

      System.out.println("run time: " + (System.currentTimeMillis() - start));
      System.out.println("cluster size: " + cluster.size());
      for(ScopeData system : cluster) {
        System.out.println(String.format(
                "system name  : %s",
                system.scope.getName()));
        System.out.println(String.format(
                "system status: %s",
                system.scope.getObjectOrientation()));
        System.out.println();
      }

      GalaxyOrientation center_status = new GalaxyOrientation(
              Math.PI, SpaceConstents.LIGHTYEAR * 1000);


      for(int ii = 1; ii < cluster.size() - 1; ii++) {
        ScopeData testing = cluster.get(ii);
        GalaxyOrientation testing_status =
                testing.scope.getGalaxyOrientation();

//                // now we go through and get every value
//                try
//                {
//                    testing.getMeta().getUpdateCount();
//                    testing.getMeta().getLastUpdate();
//                    testing.getMeta().getState();
//                }
//                catch(Throwable th)
//                {
//                    Writer writer = new StringWriter();
//                    th.printStackTrace(new PrintWriter(writer));
//                    Assert.fail(String.format(
//                            "unable to get values for system %s: %s", 
//                            testing.getId(), 
//                            writer.toString()));
//                }

        for(int j = ii + 1; j < cluster.size(); j++) {
          ScopeData check = cluster.get(j);

          GalaxyOrientation check_status =
                  check.scope.getGalaxyOrientation();

          double distance = SpaceFunctionUtil.distanceBetweenTwoObjects(
                  testing_status.getAlpha(),
                  testing_status.getDistance(),
                  check_status.getAlpha(),
                  check_status.getDistance());

          if (distance < SpaceConstents.LIGHTYEAR * 2.9
              || distance > SpaceConstents.LIGHTYEAR * 20.1) {
            System.err.println(String.format(
                    "distance: %s", distance));
            System.err.println(String.format(
                    "testing name: %s",
                    testing.scope.getName()));
            System.err.println(String.format(
                    "testing orbit: %s",
                    testing.scope.getGalaxyOrientation()));
            System.err.println(String.format(
                    "check name: %s",
                    check.scope.getName()));
            System.err.println(String.format(
                    "check orbit: %s",
                    check.scope.getGalaxyOrientation()));
            System.err.println();
            Assert.fail("distance between systems in error");
          }
        }
      }


      for(int ii = 1; ii < cluster.size() - 1; ii++) {
        ScopeData testing = cluster.get(ii);
        GalaxyOrientation testing_status =
                testing.scope.getGalaxyOrientation();

        double distance_from_center = SpaceFunctionUtil
                .distanceBetweenTwoObjects(testing_status.getAlpha(),
                                           testing_status.getDistance(),
                                           center_status.getAlpha(),
                                           center_status.getDistance());
        if (distance_from_center > SpaceConstents.LIGHTYEAR * 10.1) {
          System.err.println(String.format(
                  "distance: %s", distance_from_center));
          System.err.println(String.format(
                  "testing name: %s",
                  testing.scope.getName()));
          System.err.println(String.format(
                  "testing orbit: %s",
                  testing.scope.getGalaxyOrientation()));
          Assert.fail("too far from center");
        }

//        for(int j = ii+1; j < cluster.size(); j++)
//        {
//            GalaxySystem check = cluster.get(j);
//
//            OrbitStatus check_status = check.getGalaxyOrientation();
//
//            double distance = SpaceFunctionUtil.distanceBetweenTwoObjects(
//                    testing_status.getAlpha(), 
//                    testing_status.getDistance(), 
//                    check_status.getAlpha(), 
//                    check_status.getDistance());
//
//            if (distance < SpaceConstents.LIGHTYEAR * 3 ||
//                distance > SpaceConstents.LIGHTYEAR * 20)
//            {
//                System.err.println(String.format(
//                        "distance: %s", distance));
//                System.err.println(String.format(
//                        "testing name: %s", testing.getName()));
//                System.err.println(String.format(
//                        "testing orbit: %s", testing.getOrbiting()));
//                System.err.println(String.format(
//                        "check name: %s", check.getName()));
//                System.err.println(String.format(
//                        "check orbit: %s", check.getOrbiting()));
//                System.err.println();
//                Assert.fail("distance between systems in error");
//            }
//        }
      }
//      Assert.assertEquals("test", instance.getName());
//
//      System.out.println(System.currentTimeMillis() - start);
//      System.out.println(instance.toPrettyString());
//      System.out.println(instance.getMass());
    }
  }
}
