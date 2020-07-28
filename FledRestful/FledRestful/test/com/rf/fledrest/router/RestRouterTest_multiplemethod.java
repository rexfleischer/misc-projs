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
public class RestRouterTest_multiplemethod
{
    
    public RestRouterTest_multiplemethod()
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
        return new RestRouter(new String[]{"com.rf.fledrest.router._multiplemethod"});
    }
    
    public RestContext getContext(String root, 
                                  String path, 
                                  MethodReverse method,
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
        context.method = method;
        return context;
    }
    
    @Test
    public void test_happytest_get() throws Exception
    {
        System.out.println("test_happytest_get");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "/", MethodReverse.GET, null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(200, Response.status);
        Assert.assertEquals("hello world! get", Response.data);
    }
    
    @Test
    public void test_happytest_post() throws Exception
    {
        System.out.println("test_happytest_post");
        
        RestRouter router = getRouter();
        RestContext context = getContext("hello", "/", MethodReverse.POST, null);
        
        RestResponse Response = router.route(context);
        Assert.assertEquals(200, Response.status);
        Assert.assertEquals("hello world! post", Response.data);
    }
}
