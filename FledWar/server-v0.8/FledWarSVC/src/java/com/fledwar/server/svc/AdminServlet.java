/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.svc;

import com.fledwar.configuration.Configuration;
import com.fledwar.server.FledWarServer;
import com.fledwar.vto.user.GameUser;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class AdminServlet extends AbstractScriptRouterServlet
{
    private static final Logger logger = Logger.getLogger(AdminServlet.class);
    
    public static final String CONFIG_ROOT = "servlets.admin";
    
    public static final String CONFIG_RIGHTS = "rights_format";
    
    public static final String CONFIG_SCRIPT = "script_format";
    
    public static final String CONFIG_SECURE = "secure";
    
    private String rights_format;
    
    private String script_format;
    
    private boolean secure;

    @Override
    public void init() throws ServletException
    {
        super.init();
        
        Configuration admin_config;
        try
        {
            admin_config = FledWarServer
                .getConfiguration()
                .getAsConfiguration(CONFIG_ROOT);
        }
        catch(IOException ex)
        {
            throw new ServletException(ex);
        }
        
        secure = admin_config.getAsBoolean(CONFIG_SECURE);
        rights_format = admin_config.getAsString(CONFIG_RIGHTS);
        script_format = admin_config.getAsString(CONFIG_SCRIPT);
    }

    @Override
    protected GameUser checkSecrityAndGetUser(HttpServletRequest request) 
            throws Exception
    {
        boolean contains_fields = 
                request.getParameterMap().containsKey("username") &&
                request.getParameterMap().containsKey("session_id");
        if (secure || contains_fields)
        {
            return super.checkSecrityAndGetUser(request);
        }
        return null;
    }

    @Override
    protected String getRightsFormat()
    {
        return rights_format;
    }

    @Override
    protected String getScriptFormat()
    {
        return script_format;
    }

    @Override
    protected Logger getLogger()
    {
        return logger;
    }
}
