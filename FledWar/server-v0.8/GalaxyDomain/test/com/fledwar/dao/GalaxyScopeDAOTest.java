/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.configuration.Configuration;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.mongo.MongoConnect;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.mongodb.MongoException;
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
public class GalaxyScopeDAOTest
{
    
    public GalaxyScopeDAOTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        Log4JHelper.startDefaultLogger();
        GroovyWrapper.init("../GroovyScripts/src/scriptroot/");
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp() throws Exception
    {
        MongoConnect.initDefault();
        
        Configuration daofactory = new Configuration();
        daofactory.loadResource("/com/fledwar/dao/daofactory.groovy.config");
        dao_registry = new DAOFactoryRegistry(daofactory);
    }
    
    @After
    public void tearDown() throws Exception
    {
        dao_registry.shutdown();
        MongoConnect.getDB("test_db").dropDatabase();
        MongoConnect.closeMongo();
    }
    
    private ScopeData generateRandomSystem(int index) throws Exception
    {
        return (ScopeData) GroovyWrapper.runScript(
                "random/system/system.groovy",
                "name", "test"+index);
    }
    
    
    
    public DAOFactoryRegistry dao_registry;
    
    
    

    @Test
    public void testSimpleInsert() throws Exception
    {
        System.out.println("testSimpleInsert");
        
        GalaxyPointDAO gpdao = dao_registry
                .getDAOFactory(GalaxyPointDAO.class);
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        ScopeData system = generateRandomSystem(0);
        
        gsdao.insert(system.scope);
        gpdao.insertAll(system.points.values());
    }

    @Test
    public void testAlotsOfInserts() throws Exception
    {
        System.out.println("testAlotsOfInserts");
        
        GalaxyPointDAO gpdao = dao_registry
                .getDAOFactory(GalaxyPointDAO.class);
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        for(int i = 0; i < 10; i++)
        {
          ScopeData system = generateRandomSystem(i);

          gsdao.insert(system.scope);
          gpdao.insertAll(system.points.values());
        }
    }
    
    @Test
    public void testSimpleUpdate() throws Exception
    {
        System.out.println("testSimpleUpdate");
        
        GalaxyPointDAO gpdao = dao_registry
                .getDAOFactory(GalaxyPointDAO.class);
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        ScopeData system = generateRandomSystem(0);
        
        gsdao.insert(system.scope);
        gpdao.insertAll(system.points.values());
        
        system.scope.setName("some other name");
        gsdao.update(system.scope);
    }
    
    @Test(expected=MongoException.class)
    public void testDoubleInsert() throws Exception
    {
        System.out.println("testDoubleInsert");
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        ScopeData system = generateRandomSystem(0);
        
        gsdao.insert(system.scope);
        gsdao.insert(system.scope);
        
    }

    @Test
    public void testSimpleFind() throws Exception
    {
        System.out.println("testSimpleFind");
        
        GalaxyPointDAO gpdao = dao_registry
                .getDAOFactory(GalaxyPointDAO.class);
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        ScopeData system = generateRandomSystem(0);
        
        gsdao.insert(system.scope);
        gpdao.insertAll(system.points.values());
        
        GalaxyScope selected = gsdao.findOneFromId(system.scope.getId());
        
        Assert.assertEquals(system.scope.getId(), selected.getId());
    }
    
    @Test
    public void testRemoveById() throws Exception
    {
        System.out.println("testRemoveById");
        
        GalaxyPointDAO gpdao = dao_registry
                .getDAOFactory(GalaxyPointDAO.class);
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        ScopeData system = generateRandomSystem(0);
        
        gsdao.insert(system.scope);
        gpdao.insertAll(system.points.values());
        Assert.assertNotNull(gsdao.findOneFromId(system.scope.getId()));
        
        gsdao.removeFromId(system.scope.getId());
        Assert.assertNull(gsdao.findOneFromId(system.scope.getId()));
    }

//    /**
//     * Test of findByName method, of class GalaxySystemDAO.
//     */
//    @Test
//    public void testFindByName() throws Exception
//    {
//        System.out.println("testFindByName");
//        GalaxySystemDAO gsdao = dao_registry
//                .getDAOFactory(GalaxySystemDAO.class);
//        
//        for(int i = 0; i < 10; i++)
//        {
//            GalaxySystem system = generateRandomSystem(i);
////            System.out.println(system.getName());
//            gsdao.insert(system);
//        }
//        
//        Assert.assertEquals("test3", gsdao.findByName("test3").getName());
//    }
    
    @Test
    public void testGetAllIds() throws Exception
    {
        System.out.println("testFindByName");
        
        GalaxyPointDAO gpdao = dao_registry
                .getDAOFactory(GalaxyPointDAO.class);
        GalaxyScopeDAO gsdao = dao_registry
                .getDAOFactory(GalaxyScopeDAO.class);
        
        for(int i = 0; i < 10; i++)
        {
          ScopeData system = generateRandomSystem(0);

          gsdao.insert(system.scope);
          gpdao.insertAll(system.points.values());
        }
        
        System.out.println(gsdao.getAllIds());
    }
}
