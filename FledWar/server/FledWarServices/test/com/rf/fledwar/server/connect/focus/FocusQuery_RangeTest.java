/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.mongodb.BasicDBObject;
import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.BasicDAOException;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.server.FledWarServer;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class FocusQuery_RangeTest
{
    public static final String SPACE_DB_NAME = "test_cluster";
    
    public FocusQuery_RangeTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp() throws Exception
    {
        Properties system = FledWarServer.findProps(
                FledWarServer.FLED_WAR_PROPERTIES, 
                FledWarServer.FLED_WAR_DEFAULT_RESOURCE);
        system.setProperty(FledWarServer.START_LISTENER, "false");
        
        Properties galaxymodel = FledWarServer.findProps(
                FledWarServer.GALAXYMODEL_PROPERTIES, 
                FledWarServer.GALAXYMODEL_DEFAULT_RESOURCE);
        galaxymodel.setProperty(GalaxyModelEngine.START_BACKGROUND_THREADS, "false");
        galaxymodel.setProperty(GalaxyModelEngine.MONGO_SPACE_DB_NAME, SPACE_DB_NAME);
        
        Properties log4j = FledWarServer.findProps(
                FledWarServer.LOG4J_PROPERTIES,
                FledWarServer.LOG4J_DEFAULT_RESOURCE);
        
        FledWarServer.start(system, galaxymodel, log4j);
        
        CreateRandomSystemCluster cluster = new CreateRandomSystemCluster();
        cluster.setMinSystem(20);
        cluster.setMaxSystems(25);
        cluster.setClusterName("cluster");
        cluster.setMaxClusterRadius(SpaceConstents.LIGHTYEAR * 30);
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
        FledWarServer.shutdown();
    }
    
//    @Test
    public void testResultProjection() throws Exception
    {
        System.out.println("testResultProjection");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem system = new GalaxySystem((BasicDBObject) gsdao.getCollection().findOne(
                new BasicDBObject(GalaxySystem.NAME, "cluster-system2"), 
                new BasicDBObject().append(GalaxySystem.ORBITALS, 0)
            ));
        
        System.out.println(system.toPrettyString());
    }
    
    @Test
    public void testSingleCacheNamedFocus() throws Exception
    {
        System.out.println("testSingleCacheNamedFocus");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem system3 = gsdao.findByName("cluster-system3");
        
        Message message = new Message();
        message.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
        message.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
        message.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR);
        message.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
        
        FocusQuery view = new FocusQuery_Range();
        Object errors = view.init(new ConnectionLiaison(), message);
        if (errors != null)
        {
            System.err.println("errors: " + errors);
        }
        Assert.assertNull(errors);
        
//        view.check();
        Object check = view.check();
        Assert.assertTrue(check instanceof List);
        
        List<GalaxySystem> update = (List<GalaxySystem>) check;
        
        
//        System.err.println(String.format("center: %s", gsdao.findByName("cluster-center").getGalaxyOrbitStatus()));
//        
//        GalaxySystem system3check = gsdao.findByName("cluster-system3");
//        System.err.println(String.format("system3-g: %s",
//                                         system3check.getGalaxyOrbitStatus()));
//        System.err.println(String.format("system3-c: %s",
//                                         system3check.getCenterOrbitStatus()));
//        System.err.println(view.initial.toString());
        
        Assert.assertFalse(update.isEmpty());
    }
    
    @Test
    public void testValidUpdateCount() throws Exception
    {
        System.out.println("testValidUpdateCount");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        Message message = new Message();
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            message.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
            message.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
            message.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR);
            message.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
            message.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 5);
        }
        
        FocusQuery view = new FocusQuery_Range();
        Object errors = view.init(new ConnectionLiaison(), message);
        if (errors != null)
        {
            System.err.println("errors: " + errors);
        }
        Assert.assertNull(errors);
        
        List<GalaxySystem> update = (List<GalaxySystem>) view.check();
        Assert.assertFalse(update.isEmpty());
        
        // make this show an update to tests that the focus view
        // doesnt pull incorrectly
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            system3.triggerLastUpdate();
            system3.incrementUpdateCount();
            gsdao.update(system3);
        }
        
        // we set the FocusView.FOCUS_TIMEOUT to 5, so we should
        // get 4 timeouts (every 5th checks)
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        
        Assert.assertNotNull(view.check());
    }
    
    @Test
    public void testFocusLevel_NoOrbitals() throws Exception
    {
        System.out.println("testFocusLevel_NoOrbitals");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        Message message = new Message();
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            message.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
            message.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
            message.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR * 0.5);
            message.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
            message.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 5);
        }
        
        FocusQuery view = new FocusQuery_Range();
        Object errors = view.init(new ConnectionLiaison(), message);
        if (errors != null)
        {
            System.err.println("errors: " + errors);
        }
        Assert.assertNull(errors);
        
        {
            List<Map> update = (List<Map>) view.check();
            Assert.assertEquals(1, update.size());
            GalaxySystem checking = new GalaxySystem(update.get(0));
            Assert.assertEquals(0, checking.getOrbitalNames().size());
        }
        
        // make this show an update to tests that the focus view
        // doesnt pull incorrectly
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            system3.triggerLastUpdate();
            system3.incrementUpdateCount();
            gsdao.update(system3);
        }
        
        // we set the FocusView.FOCUS_TIMEOUT to 5, so we should
        // get 4 timeouts (every 5th checks)
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        
        {
            List<Map> update = (List<Map>) view.check();
            Assert.assertEquals(1, update.size());
            GalaxySystem checking = new GalaxySystem(update.get(0));
            Assert.assertEquals(0, checking.getOrbitalNames().size());
        }
    }
    
    @Test
    public void testFocusLevel_Orbitals() throws Exception
    {
        System.out.println("testFocusLevel_Orbitals");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        Message message = new Message();
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            message.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
            message.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
            message.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR * 0.5);
            message.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.ALL.toString());
            message.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 5);
        }
        
        FocusQuery view = new FocusQuery_Range();
        Object errors = view.init(new ConnectionLiaison(), message);
        if (errors != null)
        {
            System.err.println("errors: " + errors);
        }
        Assert.assertNull(errors);
        
        {
            List<Map> update = (List<Map>) view.check();
            Assert.assertEquals(1, update.size());
            GalaxySystem checking = new GalaxySystem(update.get(0));
            Assert.assertFalse(checking.getOrbitalNames().isEmpty());
        }
        
        // make this show an update to tests that the focus view
        // doesnt pull incorrectly
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            system3.triggerLastUpdate();
            system3.incrementUpdateCount();
            gsdao.update(system3);
        }
        
        // we set the FocusView.FOCUS_TIMEOUT to 5, so we should
        // get 4 timeouts (every 5th checks)
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        Assert.assertNull(view.check());
        
        {
            List<Map> update = (List<Map>) view.check();
            Assert.assertEquals(1, update.size());
            GalaxySystem checking = new GalaxySystem(update.get(0));
            Assert.assertFalse(checking.getOrbitalNames().isEmpty());
        }
    }
    
    @Test
    public void testMultiSystemFocus() throws Exception
    {
        System.out.println("testFocusLevel_Orbitals");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        Message message = new Message();
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            message.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
            message.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
            message.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR * 20);
            message.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
            message.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 5);
        }
        
        FocusQuery view = new FocusQuery_Range();
        Object errors = view.init(new ConnectionLiaison(), message);
        if (errors != null)
        {
            System.err.println("errors: " + errors);
        }
        Assert.assertNull(errors);
        
        
        List<Map> updates = (List<Map>) view.check();
        Assert.assertTrue(updates.size() > 1);
        
        for(Map rawupdate : updates)
        {
            GalaxySystem system = new GalaxySystem(rawupdate);
            Assert.assertTrue(system.getOrbitalNames().isEmpty());
            System.out.println(system.getName());
        }
    }
    
    @Test
    public void testSingleToMutliFocus() throws Exception
    {
        System.out.println("testSingleToMutliFocus");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        Message initmessage = new Message();
        Message updatemessage = new Message();
        {
            GalaxySystem system3 = gsdao.findByName("cluster-system3");
            initmessage.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
            initmessage.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
            initmessage.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR * 0.5);
            initmessage.putValue(FocusQuery_Range.NAME_CACHE_TIMEOUT, Integer.MAX_VALUE);
            initmessage.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.ALL.toString());
            initmessage.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 5);
            
            updatemessage.putValue(FocusQuery_Range.CENTER_ALPHA, system3.getGalaxyOrbitStatus().getAlpha());
            updatemessage.putValue(FocusQuery_Range.CENTER_DISTANCE, system3.getGalaxyOrbitStatus().getDistance());
            updatemessage.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR * 20);
            updatemessage.putValue(FocusQuery_Range.NAME_CACHE_TIMEOUT, 10);
            updatemessage.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
            updatemessage.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 3);
        }
        
        FocusQuery view = new FocusQuery_Range();
        
        {
            Object errors = view.init(new ConnectionLiaison(), initmessage);
            if (errors != null)
            {
                System.err.println("errors: " + errors);
            }
            Assert.assertNull(errors);
            
            List<Map> update = (List<Map>) view.check();
            Assert.assertEquals(1, update.size());
            GalaxySystem checking = new GalaxySystem(update.get(0));
            Assert.assertFalse(checking.getOrbitalNames().isEmpty());
        }
        
        {
            
            Object errors = view.updateFocus(updatemessage);
            if (errors != null)
            {
                System.err.println("errors: " + errors);
            }
            Assert.assertNull(errors);
            List<Map> updates = (List<Map>) view.check();
            Assert.assertTrue(updates.size() > 1);

            for(Map rawupdate : updates)
            {
                GalaxySystem system = new GalaxySystem(rawupdate);
                Assert.assertTrue(system.getOrbitalNames().isEmpty());
                System.out.println(system.getName());
            }
        }
    }
    
    @Test
    public void testNamedCache() throws Exception
    {
        System.out.println("testNamedCache");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        int count = gsdao.getCollection().find().count();
        
        Message message = new Message();
        {
            GalaxySystem center = gsdao.findByName("cluster-center");
            message.putValue(FocusQuery_Range.CENTER_ALPHA, center.getGalaxyOrbitStatus().getAlpha());
            message.putValue(FocusQuery_Range.CENTER_DISTANCE, center.getGalaxyOrbitStatus().getDistance());
            message.putValue(FocusQuery_Range.CENTER_RADIUS, SpaceConstents.LIGHTYEAR * 100);
            message.putValue(FocusQuery_Range.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
            message.putValue(FocusQuery_Range.FOCUS_TIMEOUT, 1);
            message.putValue(FocusQuery_Range.NAME_CACHE_TIMEOUT, 2);
        }
        
        FocusQuery view = new FocusQuery_Range();
        view.init(new ConnectionLiaison(), message);
        
        for(int i = 0; i < count - 1; i++)
        {
            Assert.assertNotNull(view.check());
            fakeUpdate(gsdao, "cluster-system" + i);
        }
        
        Assert.assertNotNull(view.check());
    }
    
    public void fakeUpdate(GalaxySystemDAO gsdao, String systemname) throws BasicDAOException
    {
        System.out.println("fake updating " + systemname);
        GalaxySystem system = gsdao.findByName(systemname);
        system.triggerLastUpdate();
        system.incrementUpdateCount();
        gsdao.update(system);
    }
}
