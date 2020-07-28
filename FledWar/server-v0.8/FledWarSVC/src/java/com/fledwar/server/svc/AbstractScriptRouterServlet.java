/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.svc;

import com.fledwar.blackbox.BlackboxRightsException;
import com.fledwar.blackbox.connection.BlackboxConnectionException;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.groovy.GroovyWrapperException;
import com.fledwar.server.FledWarServer;
import com.fledwar.util.JsonBuilder;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.SessionId;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public abstract class AbstractScriptRouterServlet extends HttpServlet
{
    // =====
    // keys for the response json
    // =====
    public static final String RESPONSE = "response";
    
    public static final String REQUEST = "request";
    
    public static final String ERROR = "error";
    
    
    // =====
    // keys for the standard query string
    // =====
    public static final String QUERY_ACTION = "action";
    
    public static final String QUERY_USERNAME = "username";
    
    public static final String QUERY_SESSION_ID = "session_id";
    
    
    // =====
    // variable names for the data handed into the scripts
    // =====
    public static final String SCRIPT_USER = "user";
    
    public static final String SCRIPT_ENGINE = "engine";
    
    public static final String SCRIPT_QUERY = "query";
    
    /**
     * for getting the rights format string 
     * for when the servlet tries to figure out
     * if the current user can run the requested
     * script
     * @return 
     */
    protected abstract String getRightsFormat();
    
    /**
     * for getting the script format string when
     * this servlet tries to find where the command
     * can be ran from
     * @return 
     */
    protected abstract String getScriptFormat();
    
    protected abstract Logger getLogger(); 
    
    @Override
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        String serialized;
        try
        {
            Object rr = routeRequest(request);
            serialized = JsonBuilder.ok()
                    .put(REQUEST, request.getQueryString())
                    .put(RESPONSE, rr)
                    .build();
        }
        catch(Exception ex)
        {
            serialized = JsonBuilder.not_ok()
                    .put(REQUEST, request.getQueryString())
                    .put(ERROR, ex.getMessage())
                    .build();
            getLogger().error(String.format("unable to handle request '%s'", 
                                            request.getQueryString()), 
                              ex);
        }
        
        response.setContentType("text/json;charset=UTF-8");
        try(PrintWriter out = response.getWriter()) 
        {
            out.println(serialized);
        }
        catch(Exception ex)
        {
            getLogger().error(String.format(
                    "unable to send result object %s", 
                    serialized), 
                         ex);
        }
    }
    
    /**
     * overload this method if you want to add security or something
     * @param request
     * @return
     * @throws Exception 
     */
    public Object routeRequest(HttpServletRequest request)
            throws Exception
    {
        GameUser user = checkSecrityAndGetUser(request);
        String action = request.getParameter(QUERY_ACTION).toLowerCase();
        
        String script = String.format(getScriptFormat(), action);
        return runGroovyScript(script, user, request);
    }
    
    /**
     * override this if you want to change how security is done
     * @param request
     * @return
     * @throws Exception 
     */
    protected GameUser checkSecrityAndGetUser(HttpServletRequest request)
            throws Exception
    {
        String username = request.getParameter(QUERY_USERNAME);
        String raw_session = request.getParameter(QUERY_SESSION_ID);
        
        if (username == null ||
            raw_session == null)
        {
            throw new BlackboxRightsException("invalid login data");
        }
        
        SessionId session_id = new SessionId(raw_session); 
        
        GameUser user;
        try(GameUserDAO dao = getGalaxyUserDAO())
        {
            user = dao.fetchSessionedUser(username, session_id);
        }
        
        if (user == null)
        {
            throw new BlackboxRightsException(
                    String.format("user not found [user:%s]", username));
        }
        
        String action = request.getParameter("action").toLowerCase();
        String action_right = String.format(getRightsFormat(), action);
        List<String> user_rights = user.getRights();
        if (!user_rights.contains(action_right))
        {
            throw new BlackboxRightsException(String.format(
                    "user does not have rights to run command "
                    + "[user:%s, command:%s]", 
                    username, action_right));
        }
        
        return user;
    }
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    protected GameUserDAO getGalaxyUserDAO()
            throws Exception
    {
        if (FledWarServer.getEngine() != null)
        {
            return FledWarServer.getEngine().getDAOFactoryRegistry()
                    .get(GameUserDAO.class);
        }
        
        throw new IllegalStateException("engine is shutdown");
    }   
    
    /**
     * 
     * @param script
     * @param user
     * @param request
     * @return
     * @throws BlackboxConnectionException
     * @throws BlackboxRightsException 
     */
    protected Object runGroovyScript(String script, 
                                     GameUser user,
                                     HttpServletRequest request)
            throws  BlackboxConnectionException,
                    BlackboxRightsException
    {
        Map input = new HashMap();
        input.put(SCRIPT_QUERY, request.getParameterMap());
        input.put(SCRIPT_USER,  user);
        input.put(SCRIPT_ENGINE,FledWarServer.getEngine());
        Object raw_result;
        try
        {
            raw_result = GroovyWrapper.runScript(script, input);
        }
        catch(GroovyWrapperException ex)
        {
            Throwable inner = ex.getCause();
            if (inner instanceof BlackboxRightsException)
            {
                throw ((BlackboxRightsException) inner);
            }
            throw new BlackboxConnectionException(String.format(
                    "unhandled exception in groovy script [user:%s, script:%s]", 
                    (user != null) ? user.getName() : "null", 
                    script),
                    ex);
        }
        return raw_result;
    }
}
