/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledrest.io;

/**
 *
 * @author REx
 */
public class RestResponse
{
    /**
     * helper that builds a 404 (not found) response
     * @return 
     */
    public static RestResponse get404()
    {
        return new RestResponse(404, "text/html;charset=UTF-8", null);
    }

    public static RestResponse get500(Exception ex)
    {
        return new RestResponse(500, "text/html;charset=UTF-8", ex);
    }
    
    public static RestResponse get200(Object data)
    {
        return new RestResponse(200, "text/html;charset=UTF-8", data);
    }
    
    public static RestResponse get200(String contextType, Object data)
    {
        return new RestResponse(200, contextType, data);
    }
    
    public RestResponse(int status, String contextType, Object data)
    {
        this.status     = status;
        this.data       = data;
        
        if (contextType == null || contextType.isEmpty())
        {
            this.contentType = "text/html;charset=UTF-8";
        }
        else
        {
            this.contentType = contextType;
        }
    }
    
    public final int status;
    
    public final Object data;
    
    public final String contentType;
}
