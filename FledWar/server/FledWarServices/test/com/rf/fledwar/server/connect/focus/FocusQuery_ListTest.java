/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.server.FledWarServer;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class FocusQuery_ListTest
{
    public static final String SPACE_DB_NAME = "test_cluster";
    
    public FocusQuery_ListTest()
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

    /**
     * Test of check method, of class FocusQuery_List.
     */
    @Test
    public void testCheck() throws Exception
    {
        System.out.println("check");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        Message message = new Message();
        message.putValue(FocusQuery_List.FOCUS_TIMEOUT, 2);
        message.putValue(FocusQuery_List.LIST_IDS, Arrays.asList(
                "cluster-system1", 
                "cluster-system2", 
                "cluster-system3"));
        message.putValue(FocusQuery_List.VIEW_LEVEL, ViewLevel.NO_ORBITALS.toString());
        
        FocusQuery view = new FocusQuery_List();
        Object errors = view.init(new ConnectionLiaison(), message);
        if (errors != null)
        {
            System.err.println("errors: " + errors);
        }
        Assert.assertNull(errors);
        
        
    }
}
