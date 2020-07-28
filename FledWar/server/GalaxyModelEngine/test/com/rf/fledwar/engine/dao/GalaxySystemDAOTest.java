/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.dao;

import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class GalaxySystemDAOTest
{
    
    public GalaxySystemDAOTest()
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
    
    public void orderedInserts() throws Exception
    {
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        int COUNT = 100;
        double COUNT_D = (double) COUNT;
        
        for(int i = 0; i < COUNT; i++)
        {
            for(int j = 0; j < COUNT; j++)
            {
                GalaxySystem system = new GalaxySystem();
                system.getGalaxyOrbitStatus().setAlpha((j / COUNT_D) * (2 * Math.PI));
                system.getGalaxyOrbitStatus().setDistance(SpaceConstents.LIGHTYEAR * (100 + i));
                gsdao.insert(system);
            }
        }
    }

    /**
     * Test of getInRange method, of class GalaxySystemDAO.
     */
    @Test
    public void testGetInRange1() throws Exception
    {
        System.out.println("testGetInRange1");
        
        orderedInserts();
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        {
            List<GalaxySystem> query = gsdao.getInRange(0, Math.PI / 3, SpaceConstents.LIGHTYEAR * 99, SpaceConstents.LIGHTYEAR * 101);
            System.out.println("one: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
        
        {
            List<GalaxySystem> query = gsdao.getInRange(0, 2 * (Math.PI / 3), SpaceConstents.LIGHTYEAR * 99, SpaceConstents.LIGHTYEAR * 101);
            System.out.println("second: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
        
        {
            List<GalaxySystem> query = gsdao.getInRange(0, 2 * Math.PI / 3, SpaceConstents.LIGHTYEAR * 99, SpaceConstents.LIGHTYEAR * 102);
            System.out.println("third: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
    }
    
    @Test
    public void testGetInRange2() throws Exception
    {
        System.out.println("testGetInRange2");
        
        orderedInserts();
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        {
            List<GalaxySystem> query = gsdao.getInRange(0 - 1, (Math.PI / 3) - 1, SpaceConstents.LIGHTYEAR * 99, SpaceConstents.LIGHTYEAR * 101);
            System.out.println("one: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
        
        {
            List<GalaxySystem> query = gsdao.getInRange(0 - 1, (2 * (Math.PI / 3)) - 1, SpaceConstents.LIGHTYEAR * 99, SpaceConstents.LIGHTYEAR * 101);
            System.out.println("second: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
        
        {
            List<GalaxySystem> query = gsdao.getInRange(0 - 1, (2 * Math.PI / 3) - 1, SpaceConstents.LIGHTYEAR * 99, SpaceConstents.LIGHTYEAR * 102);
            System.out.println("third: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
    }
    
    @Test
    public void testGetInRange3() throws Exception
    {
        System.out.println("testGetInRange3");
        
        orderedInserts();
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        
        {
            List<GalaxySystem> query = gsdao.getFromPoint(Math.PI, SpaceConstents.LIGHTYEAR * 102, SpaceConstents.LIGHTYEAR);
            System.out.println("one: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
        
        {
            List<GalaxySystem> query = gsdao.getFromPoint(Math.PI, SpaceConstents.LIGHTYEAR * 102, SpaceConstents.LIGHTYEAR * 2);
            System.out.println("one: count=" + query.size());
            for(GalaxySystem system : query)
            {
//                System.out.println(system.getSystemOrbitStatus());
            }
        }
        
    }
}
