/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.io;

import com.rf.fledrest.Requirement;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author REx
 */
public class RestContext
{
    public Requirement.Method.MethodReverse method;
    
    public String root;
    
    public String path;
    
    public Map<String, String> pathParams;
    
    public Map<String, InputStream> formParams;
    
    public HttpServletRequest request;
    
    public HttpServletResponse response;
}
