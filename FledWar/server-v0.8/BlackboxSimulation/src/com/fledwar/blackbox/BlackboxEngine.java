/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox;

import com.fledwar.blackbox.connection.BlackboxConnection;
import com.fledwar.blackbox.connection.BlackboxConnectionException;
import com.fledwar.blackbox.simulation.BlackboxSimulation;
import com.fledwar.blackbox.simulation.BlackboxSimulationException;
import com.fledwar.configuration.Configuration;
import com.fledwar.configuration.ConfigurationException;
import com.fledwar.dao.BasicDAOException;
import com.fledwar.dao.DAOFactoryRegistry;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.groovy.GroovyWrapperException;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.SessionId;
import com.fledwar.vto.user.UserType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class BlackboxEngine
{
  private static final Logger logger = Logger.getLogger(BlackboxEngine.class);
  
  public static final String SIMULATION = "simulation";
  
  public static final String SCRIPT_KEY_ENGINE = "engine";
  
  public static final String START_SCRIPTS = "start_scripts";
  
  public static final String START_MASTER_SCRIPTS = "start_master_scripts";
  
  public static final String SHUTDOWN_SCRIPTS = "shutdown_scripts";
  
  public static final String CREATE_USER_SCRIPT = "create_user_script";
  
  public static final String CONNECTION = "connection";
  
  public static final String CONNECTION_VALIDATE_THREASHOLD =
          "validate_threashold";
  
  public static final String CONNECTION_SESISON_TIMEOUT =
          "session_timeout";
  
  
  long start_time;
  
  Configuration engine_config;
  
  DAOFactoryRegistry dao_registry;
  
  BlackboxSimulation simulation;
  
  ObjectId engine_id;
  
  int validate_connection_threashold;
  
  int session_timeout;
  
  public BlackboxEngine(Configuration engine_config,
                        DAOFactoryRegistry dao_registry)
          throws BlackboxEngineException
  {
    this.start_time = System.currentTimeMillis();
    this.engine_config = engine_config;
    this.dao_registry = dao_registry;

    if (isMaster()) {
      runScriptList(engine_config.getAsList(START_MASTER_SCRIPTS));
    }

    runScriptList(engine_config.getAsList(START_SCRIPTS));

    try {
      Configuration connection = engine_config
              .getAsConfiguration(CONNECTION);
      validate_connection_threashold = connection
              .getAsInteger(CONNECTION_VALIDATE_THREASHOLD);
      session_timeout = connection
              .getAsInteger(CONNECTION_SESISON_TIMEOUT);
      
      if (logger.isInfoEnabled()) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("configuring connections: \n");
        
        Iterator key_it = connection.keySet().iterator();
        while(key_it.hasNext()) {
          Object key = key_it.next();
          Object value = connection.get(key);
          buffer.append("    -- connection config: ");
          buffer.append(key);
          buffer.append(" => ");
          buffer.append(value);
          buffer.append("\n");
        }
        
        logger.info(buffer.toString());
      }
    }
    catch(ConfigurationException ex) {
      shutdown();
      throw new BlackboxEngineException(
              "unable to configure connections",
              ex);
    }

    try {
      simulation = new BlackboxSimulation(
              engine_config,
              engine_config.getAsConfiguration(SIMULATION),
              dao_registry);
    }
    catch(ConfigurationException ex) {
      shutdown();
      throw new BlackboxEngineException(
              "unable to start simulation because of configuration",
              ex);
    }
    catch(BlackboxSimulationException ex) {
      shutdown();
      throw new BlackboxEngineException(
              "unable to start simulation",
              ex);
    }
  }

  public DAOFactoryRegistry getDAOFactoryRegistry()
  {
    return dao_registry;
  }

  public BlackboxSimulation getBlackboxSimulation()
  {
    return simulation;
  }

  public Configuration getConfiguration()
  {
    return engine_config;
  }

  public ObjectId getEngineId()
  {
    return engine_id;
  }

  public long getStartTime()
  {
    return start_time;
  }

  public void shutdown()
  {
    logger.info("shutting down engine");

    boolean successful = true;

    try {
      if (simulation != null) {
        simulation.shutdown();
        simulation = null;
      }
    }
    catch(Exception ex) {
      successful = false;
      logger.error("unable to shutdown simulation", ex);
    }


    try {
      runScriptList(engine_config.getAsList(SHUTDOWN_SCRIPTS));
    }
    catch(Exception ex) {
      successful = false;
      logger.error("unable to run shutdown scripts", ex);
    }

    if (successful) {
      logger.info("shutdown engine without errors");
    }
    else {
      logger.warn("errors while engine shutdown");
    }
  }

  public Object command(String script, Map params)
          throws GroovyWrapperException
  {
    params.put(SCRIPT_KEY_ENGINE, this);
    return GroovyWrapper.runScript(script, params);
  }

  public BlackboxConnection getBlackboxConnection(String user,
                                                  SessionId session_id)
          throws BlackboxConnectionException
  {
    return new BlackboxConnection(this, user, session_id,
                                  session_timeout, validate_connection_threashold);
  }

  public void userLogout(String username, SessionId session_id)
          throws BlackboxEngineException
  {
    if (logger.isDebugEnabled()) {
      logger.info(String.format("user attempting logout [%s]", username));
    }

    try(GameUserDAO dao = dao_registry.getDAOFactory(GameUserDAO.class)) {
      dao.logout(username, session_id);
    }
    catch(BasicDAOException ex) {
      throw new BlackboxEngineException(
              "unable to logout user with GalaxyUserDAO",
              ex);
    }
  }

  public SessionId userLogin(String username,
                             String password)
          throws BlackboxEngineException
  {
    if (logger.isDebugEnabled()) {
      logger.info(String.format("user attempting login [%s]", username));
    }

    try(GameUserDAO dao = dao_registry.get(GameUserDAO.class)) {
      return dao.login(username, password);
    }
    catch(BasicDAOException ex) {
      throw new BlackboxEngineException(
              "unable to login user with GalaxyUserDAO",
              ex);
    }
  }

  public void createUser(String username,
                         String password,
                         String email,
                         UserType type)
          throws BlackboxEngineException
  {
    if (logger.isInfoEnabled()) {
      logger.info("attempting to create user");
      logger.info(String.format("new user: username->%s",
                                username));
      logger.info(String.format("new user: password->%s",
                                password));
      logger.info(String.format("new user: email   ->%s",
                                email));
    }

    try(GameUserDAO dao = dao_registry.getDAOFactory(GameUserDAO.class)) {
      GameUser user_check = dao.findByUsername(username);
      if (user_check != null) {
        throw new BlackboxEngineException(String.format(
                "username %s already exists",
                username));
      }

      Map input = new HashMap();
      input.put("username", username);
      input.put("password", password);
      input.put("email", email);
      input.put("type", type);
      input.put("dao", dao);
      command(engine_config.getAsString(CREATE_USER_SCRIPT), input);
    }
    catch(GroovyWrapperException ex) {
      throw new BlackboxEngineException(
              "unable to run groovy script to create user",
              ex);
    }
    catch(BasicDAOException ex) {
      throw new BlackboxEngineException(
              "unable to work with GalaxyUserDAO",
              ex);
    }
  }

  private void runScriptList(List scripts) throws BlackboxEngineException
  {
    Iterator it = scripts.iterator();
    while(it.hasNext()) {
      String script = it.next().toString();
      try {
        command(script, new HashMap());
      }
      catch(Exception ex) {
        throw new BlackboxEngineException(ex);
      }
    }
  }

  private boolean isMaster()
  {
    // there is always only one engine running
    return true;
  }
}
