/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Requirement.Method.MethodReverse;
import com.rf.fledrest.io.RestContext;
import com.rf.fledrest.io.RestResponse;
import java.io.InputStream;
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
public class RestRouterTest_method
{
    
    public RestRouterTest_method()
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
    
    public RestRouter getRouter() 
            throws Exception
    {
        return new RestRouter(new String[]{"com.rf.fledrest.router._method"});
    }
    
    public RestContext getContext(String root, 
                                  String path, 
                                  Map<String, InputStream> forms) 
            throws Exception
    {
        if (forms == null)
        {
            forms = new HashMap<String, InputStream>();
        }
        RestContext context = new RestContext();
        context.root = root;
        context.path = path;
        context.pathParams = null;
        context.formParams = forms;
        context.method = MethodReverse.POST;
        return context;
    }

    @Test
    public void test_happytest1() throws Exception
    {
        System.out.println("test_happytest1");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "/", null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(200, Response.status);
        Assert.assertEquals("hello world!", Response.data);
    }

    @Test
    public void test_happytest2() throws Exception
    {
        System.out.println("test_happytest2");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "/world/something_here", null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(404, Response.status);
        Assert.assertEquals(null, Response.data);
    }

    @Test
    public void test_happytest3() throws Exception
    {
        System.out.println("test_happytest3");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "/world/ka ka", null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(404, Response.status);
        Assert.assertEquals(null, Response.data);
    }
    
    @Test
    public void test_class404() throws Exception
    {
        System.out.println("test_base404");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hell", "/world/blah", null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(404, Response.status);
        Assert.assertEquals(null, Response.data);
    }
    
    @Test
    public void test_method404() throws Exception
    {
        System.out.println("test_method404");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "/world2/blah", null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(404, Response.status);
        Assert.assertEquals(null, Response.data);
    }
    
    @Test(expected=RestRouterException.class)
    public void test_nomethod404() throws Exception
    {
        System.out.println("test_nomethod404");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "", null);
        
        RestResponse Response = router.route(context);
    }
}
