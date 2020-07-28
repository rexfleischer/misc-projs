/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server;

import com.mongodb.DBCursor;
import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class FledWarServerTest
{
    
    public FledWarServerTest()
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
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    
//    @Test
    public void testResetDatabase() throws Exception
    {
        System.out.println("testResetDatabase");
        
        FledWarServer.start();
        MongoConnect.getSpaceDB().dropDatabase();
        FledWarServer.shutdown();
        
        
        FledWarServer.start();
        
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
        Thread.sleep(4000);
        FledWarServer.shutdown();
    }

//    @Test
    public void testStart_0args() throws Exception
    {
        System.out.println("testStart_0args");
        
        FledWarServer.start();
        
        Thread.sleep(4000);
        
        FledWarServer.shutdown();
    }
    
    
    @Test
    public void testPrintAllGalaxyOrbit() throws Exception
    {
        FledWarServer.start();
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        DBCursor cursor = gsdao.getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((Map) cursor.next());
            System.out.println(system.getGalaxyOrbitStatus());
        }
        
        FledWarServer.shutdown();
    }
}
