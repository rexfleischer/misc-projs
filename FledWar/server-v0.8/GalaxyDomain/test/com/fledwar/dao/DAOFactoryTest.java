/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.configuration.Configuration;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.mongo.MongoConnect;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author REx
 */
public class DAOFactoryTest
{
    
    public DAOFactoryTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        Log4JHelper.startDefaultLogger();
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
    
    public DAOFactoryRegistry dao_registry;

    /**
     * Test of init method, of class DAOFactory.
     */
    @Test
    public void testInit() throws Exception
    {
        System.out.println("testInit");
        
        // if this test passes then we know that the init 
        // and close works
    }
}
