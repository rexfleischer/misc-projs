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
public class UserServlet extends AbstractScriptRouterServlet
{
    private static final Logger logger = Logger.getLogger(UserServlet.class);
    
    public static final String CONFIG_ROOT = "servlets.user";
    
    public static final String CONFIG_RIGHTS = "rights_format";
    
    public static final String CONFIG_SCRIPT = "script_format";
    
    public static final String CONFIG_LOGIN = "login_action";
    
    private String rights_format;
    
    private String script_format;
    
    private String login_action;

    @Override
    public void init() throws ServletException
    {
        super.init();
        
        Configuration user_config;
        try
        {
            user_config = FledWarServer
                .getConfiguration()
                .getAsConfiguration(CONFIG_ROOT);
        }
        catch(IOException ex)
        {
            throw new ServletException(ex);
        }
        
        rights_format = user_config.getAsString(CONFIG_RIGHTS);
        script_format = user_config.getAsString(CONFIG_SCRIPT);
        login_action  = user_config.getAsString(CONFIG_LOGIN);
    }

    @Override
    protected GameUser checkSecrityAndGetUser(HttpServletRequest request) 
            throws Exception
    {
        String action = request.getParameter(QUERY_ACTION);
        if (login_action.equalsIgnoreCase(action))
        {
            // never a user when trying to log in
            return null;
        }
        else
        {
            return super.checkSecrityAndGetUser(request);
        }
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
