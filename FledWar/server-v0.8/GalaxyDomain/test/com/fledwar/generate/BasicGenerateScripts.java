/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.generate;

import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.SystemPoint;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author REx
 */
@RunWith(Parameterized.class)
public class BasicGenerateScripts
{
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        Log4JHelper.startDefaultLogger();
        GroovyWrapper.init("../GroovyScripts/src/scriptroot/");
    }
    
    @AfterClass
    public static void tearDownClass()
    {
        GroovyWrapper.shutdown();
    }
    
    @Parameters
    public static List<Object[]> getData()
    {
        List<Object[]> result = new ArrayList<>();
        result.add(new Object[]{"random/generate/star.groovy", 5.967E29, 2E34});
        result.add(new Object[]{"random/generate/planet.groovy", 1E22, 1E28});
        result.add(new Object[]{"random/generate/moon.groovy", 1E19, 1E24});
        result.add(new Object[]{"random/generate/astroid_belt.groovy", 1E21, 1E28});
        result.add(new Object[]{"random/generate/astroid.groovy", 1E9, 1E13});
        result.add(new Object[]{"random/generate/cloud.groovy", 1E7, 1E10});
        return result;
    }
    
    @Before
    public void setUp() throws Exception
    {
        
    }
    
    @After
    public void tearDown() throws Exception
    {
        
    }
    
    String script;
    
    double min_mass;
    
    double max_mass;

    public BasicGenerateScripts(String script, double min_mass, double max_mass)
    {
        this.script = script;
        this.min_mass = min_mass;
        this.max_mass = max_mass;
    }
    
    @Test
    public void testBasicGenerate() throws Exception
    {
        System.out.println(String.format("testing %s: %s", 
                                         script, "testBasicGenerate"));
        
        for(int i = 0; i < 10; i++)
        {
            long start = System.currentTimeMillis();
            List<SystemPoint> generated = (List<SystemPoint>) 
                    GroovyWrapper.runScript(script, "name", "test");
            SystemPoint instance = generated.get(0);
            
            Assert.assertNotNull(instance);
            Assert.assertEquals("test", instance.getName());
            
            System.out.println(System.currentTimeMillis() - start);
//            System.out.println(instance.toPrettyString());
//            System.out.println(instance.getMass());
        }
    }
    
    @Test
    public void testMassGenerate() throws Exception
    {
        System.out.println(String.format("testing %s: %s", 
                                         script, "testMassGenerate"));
        
        Equation equation = Equation.square(min_mass, max_mass);
        
        for(int i = 0; i < 10; i++)
        {
            long start = System.currentTimeMillis();
            double mass = equation.find(((double) i) / 10.0);
            List<SystemPoint> generated = (List<SystemPoint>) 
                    GroovyWrapper.runScript(script, "name", "test", 
                                                    "mass", mass);
            
            SystemPoint instance = generated.get(0);
            Assert.assertNotNull(instance);
            Assert.assertEquals("test", instance.getName());
            Assert.assertEquals(mass, instance.getMass(), 0.1);
            
            System.out.println(System.currentTimeMillis() - start);
//            System.out.println(instance.toPrettyString());
//            System.out.println(instance.getMass());
        }
    }
    
    @Test
    public void testMaxMassGenerate() throws Exception
    {
        System.out.println(String.format("testing %s: %s", 
                                         script, "testMaxMassGenerate"));
        
        Equation equation = Equation.square(min_mass, max_mass);
        
        for(int i = 0; i < 10; i++)
        {
            long start = System.currentTimeMillis();
            double _max_mass = equation.find(((double) i) / 10.0);
            List<SystemPoint> generated = (List<SystemPoint>) 
                    GroovyWrapper.runScript(script, "name", "test", 
                                                    "max_mass", _max_mass);
            
            SystemPoint instance = generated.get(0);
            Assert.assertNotNull(instance);
            Assert.assertTrue(_max_mass >= instance.getMass());
            
            System.out.println(System.currentTimeMillis() - start);
//            System.out.println(instance.toPrettyString());
//            System.out.println(instance.getMass());
        }
    }
}
