/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.configuration.Configuration;
import com.fledwar.dao.BasicDAOException;
import com.fledwar.dao.DAOFactoryRegistry;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.logging.Log4JHelper;
import com.fledwar.mongo.MongoConnect;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class FledWarServer
{
  public static final String LOG4J = "config/log4j.properties";
  
  public static final String FLEDWAR = "config/fledwar.groovy";
  
  public static final String SCRIPTROOT = "scriptroot";
  
  public static final String FLEDWAR_HOME = "FLEDWAR_HOME";
  
  private static Configuration configuration;
  
  private static BlackboxEngine engine;
  
  private static DAOFactoryRegistry dao_registry;

  public static BlackboxEngine getEngine()
  {
    return engine;
  }

  public static DAOFactoryRegistry getDAOFactoryRegistry()
          throws BasicDAOException, IOException
  {
    if (dao_registry == null) {
      dao_registry = new DAOFactoryRegistry(getConfiguration());
    }
    return dao_registry;
  }

  public static Configuration getConfiguration() throws IOException
  {
    if (configuration == null) {
      String fledwar_config_path = (getFledWarHome() + FLEDWAR);
      configuration = new Configuration();
      configuration.loadFile(fledwar_config_path);
    }
    return configuration;
  }

  public static String getFledWarHome()
  {
    String fledwar_home = System.getenv(FLEDWAR_HOME);
    if (fledwar_home == null) {
      throw new NullPointerException(
              "FLEDWAR_HOME variable must be specified");
    }
    return fledwar_home;
  }

  public static void start() throws Exception
  {
    String fledwar_home = getFledWarHome();

    String log4j_path = (fledwar_home + LOG4J);
    String scriptroot = (fledwar_home + SCRIPTROOT);

    Properties log4j = new Properties();
    log4j.load(new FileInputStream(log4j_path));
    Log4JHelper.startLogger(log4j);

    GroovyWrapper.init(scriptroot);

    MongoConnect.initWithOptions(
            getConfiguration().getAsConfiguration("mongo"));

    // we are going to assume a single node for now
    engine = new BlackboxEngine(
            getConfiguration().getAsConfiguration("engine"),
            getDAOFactoryRegistry());

    // sleep to let the balancer run at least once
    Thread.sleep(2000);
  }

  public static void shutdown() throws Exception
  {
    if (engine != null) {
      engine.shutdown();
      engine = null;
      
      // sleep to let all of the threads
      // finish what they are doing
      Thread.sleep(1000);
    }

    if (dao_registry != null) {
      dao_registry.shutdown();
      dao_registry = null;
    }

    configuration = null;
    GroovyWrapper.shutdown();
    MongoConnect.closeMongo();
  }
}
