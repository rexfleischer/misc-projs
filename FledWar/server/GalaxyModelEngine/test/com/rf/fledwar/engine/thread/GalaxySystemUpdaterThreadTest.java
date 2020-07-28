/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.thread;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.HashMap;
import java.util.Map;
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
public class GalaxySystemUpdaterThreadTest
{
    
    public GalaxySystemUpdaterThreadTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        MongoConnect.initDefault("testspacedb");
    }
    
    @AfterClass
    public static void tearDownClass()
    {
        MongoConnect.closeMongo();
    }
    
    @Before
    public void setUp() throws Exception
    {
        GalaxySystemDAO.ensureAllIndexes();
    }
    
    @After
    public void tearDown()
    {
        ThreadManagerRegistry.stop();
        MongoConnect.getSpaceDB().dropDatabase();
    }
    
    public void createCluster(double alpha, String clustername) throws Exception
    {
        CreateRandomSystemCluster clustergenerator = new CreateRandomSystemCluster();
        clustergenerator.setClusterName(clustername);
        clustergenerator.setClusterOrbitStatus(new OrbitStatus(
                SpaceConstents.LIGHTYEAR * 10000,
                alpha, 
                SpaceConstents.GALAXY_ANGULAR_SPEED, 
                new OrbitPath(".center")));
        clustergenerator.exec();
    }

    /**
     * Test of doSingleIteration method, of class GalaxySystemUpdaterThread.
     */
    @Test
    public void testOkAfterInserts() throws Exception
    {
        System.out.println("testOkAfterInserts");
        createCluster(0.0, "testOkAfterInserts");
        
        Map<String, String> config = new HashMap<>();
        config.put("count", "1");
        config.put("sleeptime", "50");
        ThreadManagerRegistry.register(GalaxySystemUpdaterThread.class, config);
        ThreadManagerRegistry.start();
        
        Thread.sleep(1000);
        
        Assert.assertTrue(ThreadManagerRegistry.getThrowables().isEmpty());
        
        System.out.println("total updating:" + (new GalaxySystemDAO()).getCollection().count());
        
        DBCursor cursor = (new GalaxySystemDAO()).getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((BasicDBObject) cursor.next());
            System.out.println(system.getName() + ": " + system.getUpdateCount());
        }
    }
    
    @Test
    public void testOkBeforeAndAfterInserts() throws Exception
    {
        System.out.println("testOkBeforeAndAfterInserts");
        
        createCluster(0.0, "testOkBeforeAndAfterInserts1");
        
        Map<String, String> config = new HashMap<>();
        config.put("count", "1");
        config.put("sleeptime", "50");
        ThreadManagerRegistry.register(GalaxySystemUpdaterThread.class, config);
        ThreadManagerRegistry.start();
        
        createCluster(Math.PI, "testOkBeforeAndAfterInserts2");
        
        Thread.sleep(1000);
        
        Assert.assertTrue(ThreadManagerRegistry.getThrowables().isEmpty());
        
        System.out.println("total updating:" + (new GalaxySystemDAO()).getCollection().count());
        
        DBCursor cursor = (new GalaxySystemDAO()).getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((BasicDBObject) cursor.next());
            System.out.println(system.getName() + ": " + system.getUpdateCount());
        }
    }
}
