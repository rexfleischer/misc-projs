/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.entry;

import com.rf.fledrest.Requirement;
import com.rf.fledrest.io.RestContext;
import com.rf.fledrest.io.RestResponse;
import com.rf.fledrest.loader.FledRestClassLoader;
import com.rf.fledrest.router.RestRouter;
import com.rf.fledrest.router.RestRouterException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author REx
 */
public class FledRest extends HttpServlet
{
    private static Exception error = null;
    
    private static RestRouter router = null;
    
    @Override
    public void init() throws ServletException
    {
        super.init();
        if (router == null)
        {
            synchronized(FledRest.class)
            {
                /**
                 * now we check again to make sure
                 * that multiple threads arent trying
                 * to init it all at once
                 */
                if (router == null)
                {
                    try
                    {
                        FledRestClassLoader loader = FledRestClassLoader
                                .getLoader(getServletContext());
                        String[] classList = loader.load();
                        router = new RestRouter(classList);
                    }
                    catch(Exception ex)
                    {
                        error = ex;
                    }
                }
            }
        }
    }
    
    private void handleRoute(Requirement.Method.MethodReverse method, 
                             HttpServletRequest request, 
                             HttpServletResponse response)
            throws ServletException, IOException
    {
        if (router == null)
        {
            throw new ServletException(
                    "cannot handle request because the router has not been init",
                    error);
        }
        
        RestResponse restful = null;
        
        RestContext context = new RestContext();
        context.method      = method;
        context.request     = request;
        context.response    = response;
        
        String query = request.getRequestURI();
        int second = query.indexOf("/", 1);
        if (second < 0)
        {
            restful = RestResponse.get404();
        }
        else
        {
            context.path    = query.substring(second);
            context.root    = query.substring(1, second);
        }
        
        context.formParams  = new HashMap<String, InputStream>();
        Enumeration<String> enumeration = request.getParameterNames();
        while(enumeration.hasMoreElements())
        {
            String name = enumeration.nextElement();
            Part part = request.getPart(name);
            context.formParams.put(name, (part != null ? part.getInputStream() : null));
        }
        
        if (restful == null)
        {
            try
            {
                restful = router.route(context);
            }
            catch(RestRouterException ex)
            {
                restful = RestResponse.get500(ex);
            }
        }
        
        if (restful == null)
        {
            restful = RestResponse.get500(null);
        }
        
        response.setContentType(restful.contentType);
        response.setStatus(restful.status);
        
        if (restful.data != null)
        {
            PrintWriter out = response.getWriter();
            try
            {
                    out.print(restful.data);
            }
            finally
            {            
                out.close();
            }
        }
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        handleRoute(Requirement.Method.MethodReverse.GET, request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        handleRoute(Requirement.Method.MethodReverse.POST, request, response);
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        handleRoute(Requirement.Method.MethodReverse.PUT, request, response);
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        handleRoute(Requirement.Method.MethodReverse.DELETE, request, response);
    }
}
