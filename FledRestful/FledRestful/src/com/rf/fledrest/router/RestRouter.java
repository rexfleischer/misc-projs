/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Hook;
import com.rf.fledrest.Param;
import com.rf.fledrest.io.RestContext;
import com.rf.fledrest.io.RestResponse;
import com.rf.fledrest.Requirement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class RestRouter
{
    private Map<String, List<RoutedClass>> routes;
    
    /**
     * 
     * @param classes the classes that will be analyzed to route
     */
    public RestRouter(String[] classes) 
            throws  ClassNotFoundException,
                    RestRouterException
    {
        routes = new HashMap<String, List<RoutedClass>>();
        
        for(String clazzName : classes)
        {
            Class clazz = Class.forName(clazzName);
            Annotation[] classAnnotations = clazz.getAnnotations();
            
            /**
             * first we need to find the class level meta data
             * in order to run
             */
            String basepath = null;
            List<Class<? extends Hook.ClassSecurity>> classSecurity = 
                    new ArrayList<Class<? extends Hook.ClassSecurity>>();
            for(Annotation annotation : classAnnotations)
            {
                if (annotation instanceof Requirement.Path.Variable)
                {
                    basepath = ((Requirement.Path.Variable) annotation).value();
                }
                else if (annotation instanceof Hook.Security)
                {
                    classSecurity.add(((Hook.Security) annotation).value());
                }
                else
                {
                    throw new RestRouterException(
                            String.format("invalid annotation type: %s", annotation));
                }
            }
            if (basepath == null || basepath.isEmpty())
            {
                throw new RestRouterException(
                        "a basepath must be specified by every route pojo");
            }
            
            /**
             * now we need to find the method level meta data
             */
            List<RoutedMethod> methods = new ArrayList<RoutedMethod>();
            for(Method method : clazz.getDeclaredMethods())
            {
                Annotation[] methodAnnotations = method.getAnnotations();
                
                String methodpath = null;
                List<Class<? extends Hook.ClassSecurity>> methodSecurity = 
                        new ArrayList<Class<? extends Hook.ClassSecurity>>();
                List<Requirement.Method.MethodReverse> requestMethods = 
                        new ArrayList<Requirement.Method.MethodReverse>();
                for(Annotation annotation : methodAnnotations)
                {
                    if (annotation instanceof Requirement.Path.Variable)
                    {
                        methodpath = ((Requirement.Path.Variable) annotation).value();
                    }
                    else if (annotation instanceof Requirement.Path.Index)
                    {
                        methodpath = "/";
                    }
                    else if (annotation instanceof Hook.Security)
                    {
                        methodSecurity.add(((Hook.Security) annotation).value());
                    }
                    else if (annotation instanceof Requirement.Method.GET)
                    {
                        requestMethods.add(Requirement.Method.MethodReverse.GET);
                    }
                    else if (annotation instanceof Requirement.Method.POST)
                    {
                        requestMethods.add(Requirement.Method.MethodReverse.POST);
                    }
                    else if (annotation instanceof Requirement.Method.PUT)
                    {
                        requestMethods.add(Requirement.Method.MethodReverse.PUT);
                    }
                    else if (annotation instanceof Requirement.Method.DELETE)
                    {
                        requestMethods.add(Requirement.Method.MethodReverse.DELETE);
                    }
                }
                
                if (requestMethods.isEmpty())
                {
                    requestMethods.add(Requirement.Method.MethodReverse.GET);
                }
                
                if (methodpath == null)
                {
                    continue;
                }
                
                List<RoutedParam> params = new ArrayList<RoutedParam>();
                for(Annotation[] annotation : method.getParameterAnnotations())
                {
                    if (annotation.length != 1)
                    {
                        throw new RestRouterException(
                                "all params must have exactly one annotation");
                    }
                    
                    if (annotation[0] instanceof Param.Path)
                    {
                        params.add(new RoutedParam(
                                Param.ReverseParam.Path, 
                                ((Param.Path) annotation[0]).value()));
                    }
                    else if (annotation[0] instanceof Param.Form)
                    {
                        params.add(new RoutedParam(
                                Param.ReverseParam.Form, 
                                ((Param.Path) annotation[0]).value()));
                    }
                    else if (annotation[0] instanceof Param.Request)
                    {
                        params.add(new RoutedParam(
                                Param.ReverseParam.Request, 
                                null));
                    }
                    else if (annotation[0] instanceof Param.Response)
                    {
                        params.add(new RoutedParam(
                                Param.ReverseParam.Response, 
                                null));
                    }
                    else
                    {
                        throw new RestRouterException(
                                "invalid annotation instance type for method param");
                    }
                }
                
                RoutedMethod routedMethod   = new RoutedMethod();
                routedMethod.methodpath     = methodpath;
                routedMethod.methods        = requestMethods;
                routedMethod.security       = methodSecurity;
                routedMethod.method         = method;
                routedMethod.params         = new RoutedParam[params.size()];
                routedMethod.params         = params.toArray(routedMethod.params);
                try
                {
                    routedMethod.constructor= clazz.getConstructor();
                }
                catch (NoSuchMethodException ex)
                {
                    throw new RestRouterException(
                            "could not get the constructor for a pojo", ex);
                }
                catch (SecurityException ex)
                {
                    throw new RestRouterException(
                            "could not get the constructor for a pojo", ex);
                }
                routedMethod.validate();
                methods.add(routedMethod);
            }
            
            
            
            RoutedClass routed = new RoutedClass();
            routed.basepath = basepath;
            routed.security = classSecurity;
            routed.methods  = methods;
            if (routes.containsKey(basepath))
            {
                routes.get(basepath).add(routed);
            }
            else
            {
                List<RoutedClass> adding = new ArrayList<RoutedClass>();
                adding.add(routed);
                routes.put(basepath, adding);
            }
        }
    }
    
    public RestResponse route(RestContext context)
            throws RestRouterException
    {
        if (context == null)
        {
            throw new RestRouterException("context cannot be null");
        }
        if (context.root == null || context.root.isEmpty())
        {
            throw new RestRouterException("context.root cannot be null or empty");
        }
        if (context.path == null || context.path.isEmpty())
        {
            throw new RestRouterException("context.path cannot be null or empty");
        }
        if (context.method == null)
        {
            throw new RestRouterException("context.method cannot be null");
        }
        
        List<RoutedClass> root = routes.get(context.root);
        if (root == null)
        {
            return RestResponse.get404();
        }
        
        RoutedClass clazz = null;
        RoutedMethod method = null;
        for(int i = 0; 
            i < root.size() && method == null;
            i++)
        {
            clazz = root.get(i);
            for(int j = 0; 
                j < clazz.methods.size() && method == null; 
                j++)
            {
                RoutedMethod check = clazz.methods.get(j);
                if (!check.methods.contains(context.method))
                {
                    continue;
                }
                context.pathParams = check.checkPathParams(context.path);
                if (context.pathParams != null)
                {
                    method = check;
                }
            }
        }
        
        if (method == null)
        {
            return RestResponse.get404();
        }
        
        Object result = null;
        try
        {
            result = method.invoke(context);
        }
        catch(Exception ex)
        {
            throw new RestRouterException(
                    "uncaught error occurred during invoke of restful method", ex);
        }
        return RestResponse.get200(result);
    }
}
