/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.connection;

import com.fledwar.blackbox.BlackboxCommandException;
import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.BlackboxEngineException;
import com.fledwar.blackbox.BlackboxRightsException;
import com.fledwar.blackbox.scope.SimulationFeatureDrop;
import com.fledwar.dao.BasicDAOException;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.groovy.GroovyWrapperException;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.SessionId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class BlackboxConnection
{

  private static final Logger logger = Logger
          .getLogger(BlackboxConnection.class);
  
  public static final String RESULT = "result";
  
  public static final String FOCUS_TYPE = "focus_type";
  
  public static final String FOCUS_RIGHT_FORMAT = "focus-%s";
  
  public static final String FOCUS_GROOVY_FORMAT = "bbc/focus/%s.groovy";
  
  public static final String QUERY_TYPE = "query_type";
  
  public static final String QUERY_RIGHT_FORMAT = "query-%s";
  
  public static final String QUERY_GROOVY_FORMAT = "bbc/query/%s.groovy";
  
  public static final String ACTION_TYPE = "action_type";
  
  public static final String ACTION_RIGHT_FORMAT = "action-%s";
  
  public static final String ACTION_GROOVY_FORMAT = "bbc/action/%s.groovy";
  
  public static final String INPUT_ENGINE = "engine";
  
  public static final String INPUT_USER = "user";
  
  public static final String INPUT_CONNECTION = "connection";
  
  public static final String INPUT_FOCUS = "focus";
  
  public static final String INPUT_QUERY = "query";
  
  public static final String INPUT_ACTION = "action";
  
  public static final String INPUT_PARAMS = "params";
  
  
  
  private BlackboxEngine parent;
  
  private GameUser user;
  
  private String[] rights;
  
  private final Map<String, SimulationFeatureDrop> drops;
  
  private long last_validation;
  
  private long session_timeout;
  
  private long validate_connection_threashold;
  
  private boolean logged_out;
  

  public BlackboxConnection(BlackboxEngine parent,
                            String username,
                            SessionId session_id,
                            long session_timeout,
                            long validate_connection_threashold)
          throws BlackboxConnectionException
  {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(
              "attempting to reconnect with session "
              + "[user:%s, session:%s]",
              username,
              session_id));
    }

    this.drops = new HashMap<>();
    this.parent = parent;
    this.user = getUser(username, session_id);
    this.session_timeout = session_timeout;
    this.validate_connection_threashold = validate_connection_threashold;

    last_validation = System.currentTimeMillis();
    logged_out = false;

    List<String> raw_rights = user.getRights();
    rights = raw_rights.toArray(new String[raw_rights.size()]);
    Arrays.sort(rights);
  }

  public Map<String, SimulationFeatureDrop> getFocusDrops()
  {
    return drops;
  }

  public GameUser getUser()
  {
    return user;
  }

  public void endConnection()
  {
    synchronized(drops) {
      Iterator<SimulationFeatureDrop> it = drops.values().iterator();
      while(it.hasNext()) {
        it.next().cancel();
      }
      drops.clear();
      logged_out = true;
    }
  }

  public boolean isLoggedIn()
  {
    return !logged_out;
  }

  public void logout()
  {
    if (logged_out) {
      logger.warn("user already logged out and logout called again");
    }
    else {
      try {
        parent.userLogout(user.getName(), user.getSessionId());
      }
      catch(BlackboxEngineException ex) {
        logger.error(String.format("unable to log user out [user:%s]",
                                   user.getName()),
                     ex);
      }
    }

    endConnection();
    logged_out = true;
  }

  public SimulationFeatureDrop focus(Map focus)
          throws BlackboxConnectionException,
                 BlackboxRightsException,
                 BlackboxCommandException
  {
    validateUserSession();

    Objects.requireNonNull(focus, "focus");
    if (!focus.containsKey(FOCUS_TYPE)) {
      throw new BlackboxConnectionException(
              String.format("focus_type must be specified %s", focus));
    }

    // first we need to check the rights for this
    // type of focus
    String focus_type = focus.get(FOCUS_TYPE).toString();
    validateRight(String.format(FOCUS_RIGHT_FORMAT, focus_type));


    // now we need to get the script
    String groovy_script = String.format(FOCUS_GROOVY_FORMAT, focus_type);

    Map input_vars = new HashMap();
    input_vars.put(INPUT_FOCUS, focus);
    Object raw_result = command(groovy_script, input_vars);

    if (!(raw_result instanceof SimulationFeatureDrop)) {
      throw new BlackboxCommandException(String.format(
              "focus returned an illegal object instance "
              + "[user:%s, focus:%s, result:%s]",
              user.getName(), focus_type, raw_result));
    }

    if (raw_result == null) {
      throw new BlackboxConnectionException(String.format(
              "focus script did not return a feature drop "
              + "[user:%s, script:%s]",
              user.getName(), groovy_script));
    }
    SimulationFeatureDrop drop = (SimulationFeatureDrop) raw_result;

    synchronized(drops) {
      String drop_key = drop.getDropKey();
      if (drops.containsKey(drop_key)) {
        throw new BlackboxCommandException(String.format(
                "duplicate feature drop key [user:%s, drop key:%s]",
                user.getName(), drop_key));
      }

      drops.put(drop_key, drop);
    }

    return drop;
  }

  public Object query(Map query)
          throws BlackboxConnectionException,
                 BlackboxRightsException,
                 BlackboxCommandException
  {
    validateUserSession();

    Objects.requireNonNull(query, "query");
    if (!query.containsKey(QUERY_TYPE)) {
      throw new BlackboxConnectionException(
              String.format("query_type must be specified %s", query));
    }
    String query_type = query.get(QUERY_TYPE).toString();
    validateRight(String.format(QUERY_RIGHT_FORMAT, query_type));

    String groovy_script = String.format(QUERY_GROOVY_FORMAT, query_type);

    Map input_vars = new HashMap();
    input_vars.put(INPUT_QUERY, query);
    Object query_result = command(groovy_script, input_vars);

    return query_result;
  }

  public Map action(Map action)
          throws BlackboxConnectionException,
                 BlackboxRightsException,
                 BlackboxCommandException
  {
    validateUserSession();

    Objects.requireNonNull(action, "action");
    if (!action.containsKey(ACTION_TYPE)) {
      throw new BlackboxConnectionException(
              String.format("action_type must be specified %s", action));
    }

    // first we need to check that we have 
    // the rights to perform this action
    String action_type = action.get(ACTION_TYPE).toString();
    validateRight(String.format(ACTION_RIGHT_FORMAT, action_type));

    String groovy_script = String.format(ACTION_GROOVY_FORMAT, action_type);

    Map input_vars = new HashMap();
    input_vars.put(INPUT_ACTION, action);
    Object raw_result = command(groovy_script, input_vars);

    if (!(raw_result instanceof Map)) {
      throw new BlackboxCommandException(String.format(
              "action returned an illegal object instance "
              + "[user:%s, action:%s, result:%s]",
              user.getName(), action_type, raw_result));
    }
    if (raw_result == null) {
      throw new BlackboxConnectionException(String.format(
              "focus script did not return a feature drop "
              + "[user:%s, script:%s]",
              user.getName(), groovy_script));
    }

    Map result = (Map) raw_result;
    return result;
  }

  public Object command(String script, Map input)
          throws BlackboxConnectionException,
                 BlackboxRightsException
  {
    input.put(INPUT_CONNECTION, this);
    input.put(INPUT_ENGINE, parent);
    input.put(INPUT_USER, user);
    Object raw_result;
    try {
      raw_result = GroovyWrapper.runScript(script, input);
    }
    catch(GroovyWrapperException ex) {
      Throwable inner = ex.getCause();
      if (inner instanceof BlackboxRightsException) {
        throw ((BlackboxRightsException) inner);
      }
      throw new BlackboxConnectionException(String.format(
              "unhandled exception in groovy script [user:%s, script:%s]",
              user.getName(), script),
                                            ex);
    }
    return raw_result;
  }

  private void validateRight(String right_key)
          throws BlackboxRightsException
  {
    int right_index = Arrays.binarySearch(rights, right_key);
    if (right_index < 0) {
      throw new BlackboxRightsException(String.format(
              "user does not have rights for query type "
              + "[user:%s, right:%s]",
              user.getName(), right_key));
    }
  }

  private synchronized void validateUserSession()
          throws BlackboxConnectionException
  {
    if (logged_out) {
      throw new BlackboxConnectionException(
              "user is logged out or this connection "
              + "is no longer valid [1]");
    }

    long currtime = System.currentTimeMillis();
    if (last_validation + validate_connection_threashold >= currtime) {
      return;
    }
    last_validation = currtime;
    
    
    
    boolean valid_session = false;
    try(GameUserDAO dao = parent.getDAOFactoryRegistry()
                    .getDAOFactory(GameUserDAO.class)) {
      valid_session = dao.checkSessionedUser(
              user.getName(),
              user.getSessionId(),
              session_timeout);
    }
    catch(BasicDAOException ex) {
      throw new BlackboxConnectionException(
              String.format("unable to connect to db [%s]",
                            user.getName()),
              ex);
    }

    if (!valid_session) {
      endConnection();
      throw new BlackboxConnectionException(
              "user is logged out or this connection "
              + "is no longer valid [2]");
    }
  }

  private GameUser getUser(String username, SessionId session_id)
          throws BlackboxConnectionException
  {
    try(GameUserDAO dao = parent.getDAOFactoryRegistry()
                    .getDAOFactory(GameUserDAO.class)) {
      return dao.fetchSessionedUser(username, session_id);
    }
    catch(BasicDAOException ex) {
      throw new BlackboxConnectionException(
              String.format("unable to connect to db [%s]", username),
              ex);
    }
  }
}
