/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.generate;

import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.vto.galaxy.system.PointPlanet;
import com.fledwar.vto.galaxy.system.SystemPoint;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class SpecificGenerateTests
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
    
    @Before
    public void setUp() throws Exception
    {
        
    }
    
    @After
    public void tearDown() throws Exception
    {
        
    }
    
    @Test
    public void testGeneratePlanetAndMoon() throws Exception
    {
        System.out.println("testGeneratePlanetAndMoon");
        
        for(int i = 0; i < 10; i++)
        {
            long start = System.currentTimeMillis();
            List<SystemPoint> points = (List<SystemPoint>) GroovyWrapper.runScript(
                    "random/generate/planet.groovy", 
                            "name", "test");
            System.out.println(System.currentTimeMillis() - start);
//            System.out.println(instance.toPrettyString());
            Assert.assertNotNull(points);
            
            PointPlanet planet = (PointPlanet) points.get(0);
            
            double planet_mass = planet.getMass();
            double planet_radius = planet.getRadius();
            List<ObjectId> children = planet.getChildren();
            
            Iterator<SystemPoint> it = points.iterator();
            it.next();
            while(it.hasNext())
            {
                SystemPoint moon = it.next();
                Assert.assertTrue(moon.getMass() * 0.01 <= planet_mass);
                Assert.assertTrue(moon.getObjectOrientation().getDistance() > 
                                  planet_radius * 10);
                Assert.assertTrue(moon.getObjectOrientation().getDistance() < 
                                  planet_radius * 200);
            }
        }
    }
}
