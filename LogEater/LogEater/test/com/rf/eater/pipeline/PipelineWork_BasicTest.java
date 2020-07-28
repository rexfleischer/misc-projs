/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.eater.pipeline;

import com.rf.eater.config.EaterProperties;
import com.rf.eater.consumer.WorkFactory;
import com.rf.eater.consumer.WorkIterator;
import com.rf.eater.persistence.PersistenceFactory;
import com.rf.eater.persistence.Persistence_InMemory;
import com.rf.eater.source.LogFile;
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
public class PipelineWork_BasicTest
{
    
    public PipelineWork_BasicTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
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
    
    public static final String TEST_FILE = "C:/Users/REx/Desktop/all_reps/20110508community_seattletimes.tar.gz";

    /**
     * Test of getPipeline method, of class PipelineWork_Basic.
     */
    @Test
    public void testGetPipeline() throws Exception
    {
        System.out.println("getPipeline");
        
        EaterProperties properties = new EaterProperties();
        properties.load(PipelineWork_BasicTest.class.getResourceAsStream("/resources/config.properties"));
        
        Pipeline pipeline = PipelineFactory.BASIC.getPipeline();
        
        pipeline.setSource(new LogFile(TEST_FILE));
        pipeline.setPersistenceFactory(
                PersistenceFactory.IN_MEMORY, 
                new HashMap<String, String>());
        pipeline.setAnalysisRegex(properties.getProperty("origin.community.regex"));
        pipeline.setAnalysisMapping(properties.getList("origin.community.mapping"));
        
        WorkFactory[] factories = pipeline.getPipeline();
        
        {
            WorkFactory source = factories[0];
            Assert.assertEquals(1, source.howMuchMore());
            Assert.assertEquals("sources", source.workType());

            WorkIterator it = source.getIterator();
            Assert.assertEquals("sources", it.workType());
            it.doWork();
            it.close();
        }
        
        {
            WorkFactory analysis = factories[1];
            Assert.assertEquals(10, analysis.howMuchMore());
            Assert.assertEquals("analyze", analysis.workType());
            
            WorkIterator it = analysis.getIterator();
            Assert.assertEquals("analyze", it.workType());
            it.doWork();
            it.close();
        }
        
        {
            WorkFactory persistence = factories[2];
            Assert.assertEquals(10, persistence.howMuchMore());
            Assert.assertEquals("persistence", persistence.workType());
            
            WorkIterator it = persistence.getIterator();
            Assert.assertEquals("persistence", it.workType());
            while(it.doWork()){}
            it.close();
            
            Persistence_InMemory instance = (Persistence_InMemory)
                    ((Pipeline_Persistence) persistence).getPersistence(0);
            
            for(Map<String, String> value : instance.values)
            {
                System.out.println(value);
            }
        }
    }
}
