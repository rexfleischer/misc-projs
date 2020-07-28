/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.update;

import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.engine.create.CreateRandomSystemCluster;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.Orbital;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class UpdateGalaxySystemOrbitTest
{
    
    public UpdateGalaxySystemOrbitTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        MongoConnect.initDefault("testspace");
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
        
        CreateRandomSystemCluster cluster = new CreateRandomSystemCluster();
        cluster.setMinSystem(20);
        cluster.setMaxSystems(25);
        cluster.setClusterName("test");
        cluster.setMaxClusterRadius(SpaceConstents.LIGHTYEAR * 30);
        cluster.setClusterOrbitStatus(
                new OrbitStatus(SpaceConstents.LIGHTYEAR * 500, 
                                0.0, 
                                SpaceConstents.GALAXY_ANGULAR_SPEED, 
                                new OrbitPath(".center")));
        cluster.exec();
    }
    
    @After
    public void tearDown() throws Exception
    {
        MongoConnect.getSpaceDB().dropDatabase();
    }

    /**
     * Test of setSystem method, of class UpdateGalaxySystemOrbit.
     */
//    @Test
    public void testUpdateLargeDistance_Center() throws Exception
    {
        System.out.println("testUpdateLargeDistance");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem center = gsdao.findByName("test-center");
        
        System.out.println(center.toPrettyString());
        
        UpdateGalaxySystemOrbit updater = new UpdateGalaxySystemOrbit();
        updater.setSystem(center);
        updater.setTimeScale(100);
        updater.setCenterOrbitStatus(SpaceConstents.GALAXY_CENTER);
        
        updater.exec();
        
        System.out.println(center.toPrettyString());
    }
    
//    @Test
    public void testUpdateLargeDistance_SystemAroundCenter() throws Exception
    {
        System.out.println("testUpdateLargeDistance_SystemAroundCenter");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem updating = gsdao.findByName("test-system1");
        GalaxySystem center = gsdao.findByName("test-center");
        
        System.out.println(String.format("galaxy status before: %s", updating.getGalaxyOrbitStatus()));
        System.out.println(String.format("center status before: %s", updating.getCenterOrbitStatus()));
        
        UpdateGalaxySystemOrbit updater = new UpdateGalaxySystemOrbit();
        updater.setSystem(updating);
        updater.setTimeScale(10000);
        updater.setCenterOrbitStatus(center.getGalaxyOrbitStatus());
        
        updater.exec();
        
        System.out.println(String.format("galaxy status before: %s", updating.getGalaxyOrbitStatus()));
        System.out.println(String.format("center status before: %s", updating.getCenterOrbitStatus()));
    }
    
    @Test
    public void testUpdateGalaxySystem() throws Exception
    {
        System.out.println("testUpdateGalaxySystem");
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem updating = gsdao.findByName("test-system1");
        
        ArrayList<String> befores = new ArrayList<>();
        {
            Iterator<String> it = updating.getOrbitalNames().iterator();
            while(it.hasNext())
            {
                String name = it.next();
                Orbital orbital = updating.getOrbital(name);
                befores.add(String.format("before %s: %s", name, orbital.getOrbitStatus()));
            }
        }
        
        UpdateGalaxySystemOrbit updater = new UpdateGalaxySystemOrbit();
        updater.setSystem(updating);
        updater.setTimeScale(10000);
        updater.setCenterOrbitStatus(null);
        
        updater.exec();
        
        ArrayList<String> afters = new ArrayList<>();
        {
            Iterator<String> it = updating.getOrbitalNames().iterator();
            while(it.hasNext())
            {
                String name = it.next();
                Orbital orbital = updating.getOrbital(name);
                afters.add(String.format("after  %s: %s", name, orbital.getOrbitStatus()));
            }
        }
        
        for(int i = 0; i < befores.size(); i++)
        {
            System.out.println(befores.get(i));
            System.out.println(afters.get(i));
        }
    }
}
