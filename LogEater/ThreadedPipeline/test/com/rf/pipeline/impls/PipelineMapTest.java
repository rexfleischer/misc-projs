/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.pipeline.impls;

import com.rf.pipeline.Pipeline;
import com.rf.pipeline.PipelineException;
import com.rf.pipeline.PipelineFactory;
import com.rf.pipeline.config.PipeDefinition;
import com.rf.pipeline.config.QueueDefinition;
import com.rf.pipeline.pipes.PipeWorkFactory;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author REx
 */
public class PipelineMapTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    public PipelineMapTest()
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
    
    public Pipeline getPipeline() throws PipelineException
    {
        PipelineFactory factory = PipelineFactory.getFactory();
        
        {
            Map<String, String> outputs = new HashMap<String, String>();
            outputs.put("output", "1_to_2");
            PipeDefinition step1 = new PipeDefinition(
                    "com.rf.pipeline.impls.StepOne", 
                    "first step", 
                    "", 
                    outputs);
            factory.layDefinition(step1);
        }
        
        {
            QueueDefinition _1_to_2 = new QueueDefinition("1_to_2", "IN_MEMORY", "STRING");
            factory.layDefinition(_1_to_2);
        }
        
        {
            Map<String, String> outputs = new HashMap<String, String>();
            outputs.put("output", "2_to_3");
            PipeDefinition step2 = new PipeDefinition(
                    "com.rf.pipeline.impls.StepTwo", 
                    "second step", 
                    "1_to_2", 
                    outputs);
            factory.layDefinition(step2);
        }
        
        {
            QueueDefinition _2_to_3 = new QueueDefinition("2_to_3", "IN_MEMORY", "STRING");
            factory.layDefinition(_2_to_3);
        }
        
        {
            Map<String, String> outputs = new HashMap<String, String>();
            PipeDefinition step3 = new PipeDefinition(
                    "com.rf.pipeline.impls.StepThree", 
                    "third step", 
                    "2_to_3", 
                    outputs);
            factory.layDefinition(step3);
        }
        
        return factory.initPipeline();
    }

    /**
     * Test of setGlobalObject method, of class PipelineMap.
     */
//    @Test
    public void test_happypath() throws Exception
    {
        System.out.println("test_happypath");
        
        Pipeline pipeline = getPipeline();
        pipeline.setPipeObject("first step", "amount", 5);
        
        Assert.assertTrue(pipeline instanceof PipelineMap);
        PipelineMap instance = (PipelineMap) pipeline;
        Map<String, PipeWorkFactory> factories = instance.getPipes();
        
        factories.get("first step").getIterator().doWork();
        factories.get("second step").getIterator().doWork();
        factories.get("third step").getIterator().doWork();
    }
    
    @Test
    public void test_threads() throws Exception
    {
        System.out.println("test_threads");
        
        Pipeline pipeline = getPipeline();
        pipeline.setPipeObject("first step", "amount", 10000);
        
        pipeline.setThreadConfig("memory_target", "0.5");
        pipeline.setThreadConfig("cpu_target", "0.5");
        pipeline.setThreadConfig("thread_wait_time", "5");
        pipeline.setThreadConfig("max_threads", "3");
        pipeline.setThreadConfig("separate_manager_thread", "false");
        pipeline.setThreadConfig("returns_when_no_work", "true");
        pipeline.setThreadConfig("manager_thread_wait_time", "1");
        
        pipeline.initThreads();
        pipeline.stopThreads();
    }
    
    
//    @Test
    public void test_outputtonowhere() throws Exception
    {
        thrown.expect(PipelineException.class);
        
        System.out.println("test_outputtonowhere");
        PipelineFactory factory = PipelineFactory.getFactory();
        
        {
            Map<String, String> outputs = new HashMap<String, String>();
            outputs.put("output", "1_to_2");
            PipeDefinition step1 = new PipeDefinition(
                    "com.rf.pipeline.impls.StepOne", 
                    "first step", 
                    "", 
                    outputs);
            factory.layDefinition(step1);
        }
        
        {
            QueueDefinition _1_to_2 = new QueueDefinition("1_to_2", "IN_MEMORY", "STRING");
            factory.layDefinition(_1_to_2);
        }
        
        Pipeline pipeline = factory.initPipeline();
    }
}
