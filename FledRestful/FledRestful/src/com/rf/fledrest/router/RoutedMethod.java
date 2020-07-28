/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.router;

import com.rf.fledrest.Hook.ClassSecurity;
import com.rf.fledrest.Requirement;
import com.rf.fledrest.io.RestContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author REx
 */
public class RoutedMethod
{
    private static Pattern pathCompiler = Pattern.compile("\\{(.*?)\\}");
    
    Pattern pathPattern;
    
    String[] pathMapping;
    
    public String methodpath;
    
    public List<Requirement.Method.MethodReverse> methods;
    
    public List<Class<? extends ClassSecurity>> security;
    
    public Method method;
    
    public RoutedParam[] params;
    
    public Constructor constructor;
    
    public void validate()
    {
        if (methodpath == null)
        {
            throw new IllegalStateException("methodpath must be specified");
        }
        if (methods == null || methods.isEmpty())
        {
            throw new IllegalStateException("at least one method must be specified");
        }
        if (security == null)
        {
            throw new IllegalStateException("security cannot be null");
        }
        if (method == null)
        {
            throw new IllegalStateException("method cannot be null");
        }
        if (params == null)
        {
            throw new IllegalStateException("params cannot be null");
        }
        for(int i = 0; i < params.length; i++)
        {
            if (params[i] == null)
            {
                throw new IllegalStateException(
                        "every param in the params array must be specified");
            }
        }
        if (constructor == null)
        {
            throw new IllegalStateException("pojoConstructor cannot be null");
        }
        
        
        Matcher matcher = pathCompiler.matcher(methodpath);
        String compiledPattern = null;
        
        List<String> paths = new ArrayList<String>();
        while(matcher.find())
        {
            paths.add(matcher.group(1));
        }
        
        if (paths.isEmpty())
        {
            pathMapping = new String[0];
            compiledPattern = methodpath;
        }
        else
        {
            compiledPattern = matcher.replaceAll("(.*)");
            
            pathMapping = new String[paths.size()];
            pathMapping = paths.toArray(pathMapping);
        }
        
        compiledPattern = String.format("^%s$", compiledPattern);
        
        pathPattern = Pattern.compile(compiledPattern);
    }
    
    /**
     * if this method returns null, then that means it is not a match. but
     * if it returns a map, then it is a match.
     * @param path
     * @return 
     */
    public Map<String, String> checkPathParams(String path)
    {
        if (path == null || path.isEmpty())
        {
            return null;
        }
        Matcher matcher = pathPattern.matcher(path);
        if (!matcher.find())
        {
            return null;
        }
        
        Map<String, String> result = new HashMap<String, String>();
        for(int i = 0; i < matcher.groupCount(); i++)
        {
            result.put(pathMapping[i], matcher.group(i + 1));
        }
        return result;
    }
    
    public Object invoke(RestContext context) throws RestRouterException
    {
        Object pojo;
        try
        {
            pojo = constructor.newInstance();
        }
        catch (InstantiationException ex)
        {
            throw new RestRouterException("coult not create pojo object", ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new RestRouterException("coult not create pojo object", ex);
        }
        catch (IllegalArgumentException ex)
        {
            throw new RestRouterException("coult not create pojo object", ex);
        }
        catch (InvocationTargetException ex)
        {
            throw new RestRouterException("coult not create pojo object", ex);
        }
        
        Object[] args = new String[params.length];
        for(int i = 0; i < params.length; i++)
        {
            switch(params[i].type)
            {
                case Path:
                    args[i] = context.pathParams.get(params[i].name);
                    break;
                    
                case Form:
                    args[i] = context.formParams.get(params[i].name);
                    break;
                    
                case Request:
                    args[i] = context.request;
                    break;
                    
                case Response:
                    args[i] = context.response;
                    break;
                    
                default:
                    throw new RestRouterException(
                            "unable to find param type on invoke");
            }
        }
        try
        {
            return method.invoke(pojo, args);
        }
        catch (IllegalAccessException ex)
        {
            throw new RestRouterException(ex);
        }
        catch (IllegalArgumentException ex)
        {
            throw new RestRouterException(ex);
        }
        catch (InvocationTargetException ex)
        {
            throw new RestRouterException(ex);
        }
    }
}
