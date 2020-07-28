/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Hook.ClassSecurity;
import com.rf.fledrest.Param;
import com.rf.fledrest.Requirement.Method.MethodReverse;
import com.rf.fledrest.io.RestContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
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
public class RoutedMethodTest
{
    
    public RoutedMethodTest()
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

    /**
     * Test of invoke method, of class RoutedMethod.
     */
    @Test
    public void test_happypath() throws Exception
    {
        System.out.println();
        System.out.println("RoutedMethodTest.test_happypath()");
        
        RestContext context = new RestContext();
        context.root = "place";
        context.path = "/something/please";
        context.pathParams = new HashMap<String, String>();
        context.formParams = new HashMap<String, InputStream>();
        
        RoutedMethod method = new RoutedMethod();
        method.security     = new ArrayList<Class<? extends ClassSecurity>>();
        method.methods      = new ArrayList<MethodReverse>();
        method.methods.add(MethodReverse.GET);
        method.methodpath   = "/something/please";
        method.params       = new RoutedParam[0];
        method.method       = RoutedMethodTest.class.getMethod("testMethod");
        method.constructor  = RoutedMethodTest.class.getConstructor();
        
        method.validate();
        Assert.assertNotNull(method.checkPathParams(context.path));
        
        method.invoke(context);
    }
    
    public void testMethod()
    {
        System.out.println("hello from testMethod");
    }
    
    @Test
    public void test_param() throws Exception
    {
        System.out.println();
        System.out.println("RoutedMethodTest.test_param()");
        
        RestContext context = new RestContext();
        context.root = "hello";
        context.path = "/world/hola";
        context.pathParams = new HashMap<String, String>();
        context.formParams = new HashMap<String, InputStream>();
        
        RoutedMethod method = new RoutedMethod();
        method.security     = new ArrayList<Class<? extends ClassSecurity>>();
        method.methods      = new ArrayList<MethodReverse>();
        method.methods.add(MethodReverse.GET);
        method.methodpath   = "/world/{note}";
        method.params       = new RoutedParam[1];
        method.params[0]    = new RoutedParam(Param.ReverseParam.Path, "note");
        method.method       = RoutedMethodTest.class.getMethod("helloworld", String.class);
        method.constructor  = RoutedMethodTest.class.getConstructor();
        
        method.validate();
        context.pathParams = method.checkPathParams(context.path);
        Assert.assertNotNull(context.pathParams);
        
        Object result = method.invoke(context);
        System.out.println(String.format("result: %s", result));
    }
    
    public void helloworld(String note)
    {
        System.out.println("hello world: " + note);
    }
    
    
    
    @Test
    public void test_lotsofparams() throws Exception
    {
        System.out.println();
        System.out.println("RoutedMethodTest.test_lotsofparams()");
        
        RestContext context = new RestContext();
        context.root = "hello";
        context.path = "/blank/blank2/blank3/black/blank1";
        context.pathParams = new HashMap<String, String>();
        context.formParams = new HashMap<String, InputStream>();
        
        RoutedMethod method = new RoutedMethod();
        method.security     = new ArrayList<Class<? extends ClassSecurity>>();
        method.methods      = new ArrayList<MethodReverse>();
        method.methods.add(MethodReverse.GET);
        method.methodpath   = "/blank/{note2}/{note3}/black/{note1}";
        method.params       = new RoutedParam[3];
        method.params[0]    = new RoutedParam(Param.ReverseParam.Path, "note1");
        method.params[1]    = new RoutedParam(Param.ReverseParam.Path, "note2");
        method.params[2]    = new RoutedParam(Param.ReverseParam.Path, "note3");
        method.method       = RoutedMethodTest.class.getMethod("helloworld", String.class, String.class, String.class);
        method.constructor  = RoutedMethodTest.class.getConstructor();
        
        method.validate();
        context.pathParams = method.checkPathParams(context.path);
        Assert.assertNotNull(context.pathParams);
        
        Object result = method.invoke(context);
        System.out.println(String.format("result: %s", result));
    }
    
    public void helloworld(String note1, String note2, String note3)
    {
        System.out.println(
                String.format("notes: 1-%s, 2-%s, 3-%s", note1, note2, note3));
    }
    
    
    
    @Test
    public void test_difftypesofparams() throws Exception
    {
        System.out.println();
        System.out.println("RoutedMethodTest.test_difftypesofparams()");
        
        RestContext context = new RestContext();
        context.root = "hello";
        context.path = "/blank/stuff";
        context.pathParams = new HashMap<String, String>();
        context.formParams = new HashMap<String, InputStream>();
        
        RoutedMethod method = new RoutedMethod();
        method.security     = new ArrayList<Class<? extends ClassSecurity>>();
        method.methods      = new ArrayList<MethodReverse>();
        method.methods.add(MethodReverse.GET);
        method.methodpath   = "/blank/{note1}";
        method.params       = new RoutedParam[2];
        method.params[0]    = new RoutedParam(Param.ReverseParam.Path, "note1");
        method.params[1]    = new RoutedParam(Param.ReverseParam.Request, null);
        method.method       = RoutedMethodTest.class.getMethod("helloworld", String.class, HttpServletRequest.class);
        method.constructor  = RoutedMethodTest.class.getConstructor();
        
        method.validate();
        context.pathParams = method.checkPathParams(context.path);
        Assert.assertNotNull(context.pathParams);
        
        Object result = method.invoke(context);
        System.out.println(String.format("result: %s", result));
    }
    
    public void helloworld(String note1, HttpServletRequest request)
    {
        System.out.println(
                String.format("notes: 1-%s, 2-%s", note1, request));
    }
    
    
    
    @Test
    public void test_returnsdata() throws Exception
    {
        System.out.println();
        System.out.println("RoutedMethodTest.test_returnsdata()");
        
        RestContext context = new RestContext();
        context.root = "hello";
        context.path = "/blank";
        context.pathParams = new HashMap<String, String>();
        context.formParams = new HashMap<String, InputStream>();
        
        RoutedMethod method = new RoutedMethod();
        method.security     = new ArrayList<Class<? extends ClassSecurity>>();
        method.methods      = new ArrayList<MethodReverse>();
        method.methods.add(MethodReverse.GET);
        method.methodpath   = "/blank";
        method.params       = new RoutedParam[0];
        method.method       = RoutedMethodTest.class.getMethod("returnsdata");
        method.constructor  = RoutedMethodTest.class.getConstructor();
        
        method.validate();
        context.pathParams = method.checkPathParams(context.path);
        Assert.assertNotNull(context.pathParams);
        
        Object result = method.invoke(context);
        Assert.assertEquals("hello world!", result);
        System.out.println(String.format("result: %s", result));
    }
    
    public String returnsdata()
    {
        return "hello world!";
    }
}
