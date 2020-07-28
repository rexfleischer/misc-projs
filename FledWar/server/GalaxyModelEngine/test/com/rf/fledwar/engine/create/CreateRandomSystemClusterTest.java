/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.mongodb.DBCursor;
import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import org.apache.log4j.PropertyConfigurator;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class CreateRandomSystemClusterTest
{
    public static final double MAX_SYSTEM_FROM_CENTER = SpaceConstents.LIGHTYEAR * 30;
    
    public CreateRandomSystemClusterTest()
    {
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
        Properties log4j = new Properties();
        log4j.put("log4j.rootLogger", "DEBUG, CA");
        log4j.put("log4j.appender.CA", "org.apache.log4j.ConsoleAppender");
        log4j.put("log4j.appender.CA.layout", "org.apache.log4j.PatternLayout");
        log4j.put("log4j.appender.CA.layout.ConversionPattern", "%d{HH:mm:ss.SSS} [%t] %-5p %c %x - %m%n");
        PropertyConfigurator.configure(log4j);
        
        
        Properties galaxymodel = new Properties();
        galaxymodel.put("gm.timescale", "100.0");
        galaxymodel.put("gm.start.mongo", "true");
        galaxymodel.put("gm.start.bgthreads", "false");
        galaxymodel.put("gm.mongo.hosts", "localhost:27017");
        galaxymodel.put("gm.mongo.spacedbname", "testspacemodel");
        galaxymodel.put("gm.mongo.insureindex", "true");
        galaxymodel.put("gm.mongo.insurenull", "true");
        galaxymodel.put("gm.mongo.insuretime", "true");
        GalaxyModelEngine.start(galaxymodel);
        
        
        CreateRandomSystemCluster cluster = new CreateRandomSystemCluster();
        cluster.setMinSystem(20);
        cluster.setMaxSystems(25);
        cluster.setClusterName("cluster");
        cluster.setMaxClusterRadius(MAX_SYSTEM_FROM_CENTER);
        cluster.setClusterOrbitStatus(
                new OrbitStatus(SpaceConstents.LIGHTYEAR * 5000, 
                                0.0, 
                                SpaceConstents.GALAXY_ANGULAR_SPEED, 
                                new OrbitPath(".center")));
        cluster.exec();
    }
    
    @After
    public void tearDown() throws Exception
    {
        MongoConnect.getSpaceDB().dropDatabase();
        MongoConnect.closeMongo();
    }

    
    @Test
    public void testCenterSystemOrbitStatus() throws Exception
    {
        System.out.println("testCenterSystemOrbitStatus");
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        GalaxySystem center = gsdao.findByName("cluster-center");
        
        Assert.assertEquals(center.getCenterOrbitStatus(), center.getGalaxyOrbitStatus());
    }
    
    @Test
    public void testGalaxyOrbitStatusOfSystems() throws Exception
    {
        System.out.println("testGalaxyOrbitStatusOfSystems");
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        GalaxySystem center = gsdao.findByName("cluster-center");
        OrbitStatus centerstatus = center.getGalaxyOrbitStatus();
        ObjectId centerid = center.getId();
        
        DBCursor cursor = gsdao.getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((Map) cursor.next());
            if (!system.getId().equals(centerid))
            {
                OrbitStatus systemgalaxy = system.getGalaxyOrbitStatus();
                Assert.assertEquals(null, systemgalaxy.getOrbitPath().getSystem());
                
                Assert.assertTrue(systemgalaxy.getAlpha() >= 0.0);
                Assert.assertTrue(systemgalaxy.getAlpha() <= (2 * Math.PI));
                
                double distanceFromCenter = SpaceFunctionUtil
                        .distanceBetweenTwoObjects(systemgalaxy.getAlpha(),
                                                   systemgalaxy.getDistance(), 
                                                   centerstatus.getAlpha(), 
                                                   centerstatus.getDistance());
//                System.out.println(systemgalaxy);
//                System.out.println(String.format("distance from center: %s", distanceFromCenter));
                Assert.assertTrue(distanceFromCenter <= MAX_SYSTEM_FROM_CENTER);
            }
        }
    }
    
    @Test
    public void testCenterOfAllSystemStatus() throws Exception
    {
        System.out.println("testCenterOfAllSystemStatus");
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        GalaxySystem center = gsdao.findByName("cluster-center");
        ObjectId centerid = center.getId();
        
        DBCursor cursor = gsdao.getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((Map) cursor.next());
            if (!system.getId().equals(centerid))
            {
                OrbitStatus systemcenter = system.getCenterOrbitStatus();
                Assert.assertEquals(centerid, systemcenter.getOrbitPath().getSystem());
                
                Assert.assertTrue(systemcenter.getAlpha() >= 0.0);
                Assert.assertTrue(systemcenter.getAlpha() <= (2 * Math.PI));
//                System.out.println(String.format("distance from center: %s", systemcenter.getDistance()));
                Assert.assertTrue(systemcenter.getDistance() > 0.0);
                Assert.assertTrue(systemcenter.getDistance() <= MAX_SYSTEM_FROM_CENTER);
            }
        }
    }
    
    @Test
    public void testValidDistanceFromSystem() throws Exception
    {
        System.out.println("testValidDistanceFromSystem");
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        DBCursor cursor = gsdao.getCollection().find();
        List<GalaxySystem> systems = new ArrayList<>(cursor.size());
        while(cursor.hasNext())
        {
            systems.add(new GalaxySystem((Map) cursor.next()));
        }
        
        for(int i = 0; i < systems.size(); i++)
        {
            GalaxySystem checking = systems.get(i);
            for(int j = i + 1; j < systems.size(); j++)
            {
                GalaxySystem system = systems.get(j);
                
                if (checking.getCenterOrbitStatus().getOrbitPath().getSystem() == null ||
                    system.getCenterOrbitStatus().getOrbitPath().getSystem() == null)
                {
                    continue;
                }
                
                double distance_g = SpaceFunctionUtil.distanceBetweenTwoObjects(
                        checking.getGalaxyOrbitStatus().getAlpha(), 
                        checking.getGalaxyOrbitStatus().getDistance(), 
                        system.getGalaxyOrbitStatus().getAlpha(), 
                        system.getGalaxyOrbitStatus().getDistance());
                
                double distance_c = SpaceFunctionUtil.distanceBetweenTwoObjects(
                        checking.getCenterOrbitStatus().getAlpha(), 
                        checking.getCenterOrbitStatus().getDistance(), 
                        system.getCenterOrbitStatus().getAlpha(), 
                        system.getCenterOrbitStatus().getDistance());
                
//                System.out.println(String.format("%s-g: %s", checking.getId(), checking.getGalaxyOrbitStatus()));
//                System.out.println(String.format("%s-g: %s", system.getId(), system.getGalaxyOrbitStatus()));
                System.out.println(String.format("g-distance: %s", distance_g));
//                System.out.println(String.format("%s-c: %s", checking.getId(), checking.getCenterOrbitStatus()));
//                System.out.println(String.format("%s-c: %s", system.getId(), system.getCenterOrbitStatus()));
                System.out.println(String.format("c-distance: %s", distance_c));
                
                double abs_diff = Math.abs(distance_c - distance_g);
                System.out.println(String.format("abs_diff: %s", abs_diff));
                Assert.assertTrue(abs_diff < 3.5E7);
                
                Assert.assertTrue(distance_g > SpaceConstents.LIGHTYEAR);
                Assert.assertTrue(distance_g < (MAX_SYSTEM_FROM_CENTER * 2));
                
                
            }
        }
    }
}
