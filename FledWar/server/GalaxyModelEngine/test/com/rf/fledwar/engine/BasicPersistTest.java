/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.rf.fledwar.engine.create.CreateRandomSystem;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.GalaxySystemState;
import com.rf.fledwar.model.Orbital;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.*;

/**
 *
 * @author REx
 */
public class BasicPersistTest
{
    
    public BasicPersistTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        MongoConnect.initDefault("testspacedb");
//        MongoConnect.getSpaceDB().dropDatabase();
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        MongoConnect.closeMongo();
    }
    
    @Before
    public void setUp() throws Exception
    {
        GalaxySystemDAO.ensureAllIndexes();
    }
    
    @After
    public void tearDown() throws Exception
    {
        MongoConnect.getSpaceDB().dropDatabase();
    }
    
    public void randomInserts(int amount) throws Exception
    {   
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        double birth = SpaceFunctionUtil.currentGameYear();
        for(int i = 0; i < amount; i++)
        {
            CreateRandomSystem creating = new CreateRandomSystem();
            creating.setCurrentYear(birth + i);
            creating.setSystemName(String.format("system%s", i));
            creating.exec();
            
            GalaxySystem newsystem = creating.getSystem();
            gsdao.insert(newsystem);
        }
    }
    
    public void printCollection() throws Exception
    {
        DBCursor cursor = (new GalaxySystemDAO()).getCollection().find();
        while(cursor.hasNext())
        {
            Map object = (BasicDBObject) cursor.next();
            if (object == null)
            {
                System.out.println("another freaking null");
            }
            else
            {
                System.out.println(new GalaxySystem(object));
            }
        }
    }
    
    @Test
    public void test_updatedCorrectly() throws Exception
    {
        System.out.println("test_updatedCorrectly");
        
        randomInserts(5);
        Map<ObjectId, Integer> updateCount = new HashMap<>();
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        assertValidDB();
//        printCollection();
        
        try
        {
            for(int i = 0; i < 100; i++)
            {
                Thread.sleep(10);
                GalaxySystem system = gsdao.findLastUpdatedAndFlag();
                if (updateCount.containsKey(system.getId()))
                {
                    int countat = updateCount.get(system.getId());
                    updateCount.put(system.getId(), countat + 1);
                }
                else
                {
                    updateCount.put(system.getId(), 1);
                }
                assertValidDB();
                gsdao.finishedWithUpdateAndUnflag(system);
            }
        }
        catch(Throwable ex)
        {
            printCollection();
            
            throw ex;
        }
        
        int count = -1;
        Iterator<ObjectId> it = updateCount.keySet().iterator();
        while(it.hasNext())
        {
            ObjectId key = it.next();
            int updatecount = updateCount.get(key);
            if (count == -1)
            {
                count = updatecount;
            }
            System.out.println(key + ": " + updatecount);
            
//            Assert.assertEquals(count, updatecount);
        }
    }
    
    @Test
    public void test_singleParams() throws Exception
    {
        System.out.println("test_singleParams");
        
        randomInserts(1);
        
        GalaxySystem system = new GalaxySystem(
                (BasicDBObject) (new GalaxySystemDAO()).getCollection().find().next());
        
//        System.out.println(system);
        
        Assert.assertEquals(system.getName(), "system0");
        Assert.assertEquals(GalaxySystemState.NONE, system.getState());
        
    }
    
    @Test
    public void test_basicSpaceObject() throws Exception
    {
        System.out.println("test_basicSpaceObject");
        
        randomInserts(100);
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        DBCursor cursor = gsdao.getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((BasicDBObject) cursor.next());
            
            assertValidVTO(system);
//            System.out.println(system.toPrettyString(2));
        }
    }
    
    @Test
    public void test_randomCluster() throws Exception
    {
        System.out.println("test_randomCluster");
        
        CreateRandomSystemCluster clustergenerator = new CreateRandomSystemCluster();
        clustergenerator.setClusterName("test");
        clustergenerator.setClusterOrbitStatus(new OrbitStatus(
                SpaceConstents.LIGHTYEAR * 10000,
                0.0, 
                SpaceConstents.GALAXY_ANGULAR_SPEED, 
                new OrbitPath(".center")));
        clustergenerator.exec();
        
        Assert.assertTrue((new GalaxySystemDAO()).getCollection().count() != 0);
        
//        DBCursor cursor = (new GalaxySystemDAO()).getCollection().find();
//        while(cursor.hasNext())
//        {
//            System.out.println((new GalaxySystem((BasicDBObject) cursor.next())).toPrettyString(2));
//        }
    }
    
    public void assertValidDB() throws Exception
    {
        DBCursor cursor = (new GalaxySystemDAO()).getCollection().find();
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((BasicDBObject) cursor.next());
        
            Assert.assertNotNull(system.getName());
            system.getState();

            Assert.assertNotNull(system.getId());

            boolean atLeastOne = false;
            Iterator<String> it = system.getOrbitalNames().iterator();
            while(it.hasNext())
            {
                String name = it.next();
                atLeastOne = true;

                Orbital orbital = system.getOrbital(name);

                Assert.assertNotNull(orbital.getName());
                Assert.assertTrue(orbital.getMass() > 0);
                Assert.assertNotNull(orbital.getOrbitalType());
            }

            Assert.assertTrue(atLeastOne);
        }
    }
    
    public void assertValidVTO(GalaxySystem system) throws Exception
    {
//        System.out.println(system);
        
        Assert.assertNotNull(system.getName());
        Assert.assertEquals(GalaxySystemState.NONE, system.getState());
        
        Assert.assertNotNull(system.getId());
        
        boolean atLeastOne = false;
        Iterator<String> it = system.getOrbitalNames().iterator();
        while(it.hasNext())
        {
            String name = it.next();
            atLeastOne = true;
            
            Orbital orbital = system.getOrbital(name);
            
            Assert.assertNotNull(orbital.getName());
            Assert.assertTrue(orbital.getMass() > 0);
            Assert.assertNotNull(orbital.getOrbitalType());
        }
        
        Assert.assertTrue(atLeastOne);
    }
}
