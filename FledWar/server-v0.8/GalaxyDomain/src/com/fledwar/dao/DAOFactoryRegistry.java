/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.configuration.Configuration;
import com.fledwar.util.PoolManagerException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class DAOFactoryRegistry
{

  public static final String CONFIG_CLAZZ = "clazz";
  
  public static final String DAO_REGISTRY = "dao_registry";
  
  private Map<Class, DAOFactory> factories = new HashMap<>();

  public DAOFactoryRegistry(Configuration config)
          throws BasicDAOException
  {
    Configuration dao_config = config.getAsConfiguration(DAO_REGISTRY);
    Iterator it = dao_config.keySet().iterator();
    while(it.hasNext()) {
      Object next_key = it.next();
      Configuration registering = dao_config.getAsConfiguration(next_key);
      registerSingle(registering);
    }
  }

  private void registerSingle(Configuration config)
          throws BasicDAOException
  {
    try {
      Class clazz = config.getAsClass(CONFIG_CLAZZ);
      DAOFactory new_factory = new DAOFactory(config, clazz);
      factories.put(clazz, new_factory);
    }
    catch(Exception ex) {
      throw new BasicDAOException(ex);
    }
  }

  public Map<Class, DAOFactory> getFactories()
  {
    return factories;
  }

  public void shutdown()
  {
    for(DAOFactory factory : factories.values()) {
      try {
        factory.shutdown();
      }
      catch(Exception ex) {
        Logger.getLogger(DAOFactoryRegistry.class)
                .error("unable to shutdown dao pool", ex);
      }
    }
    factories.clear();
  }

  public <T> T getDAOFactory(Class<T> type)
          throws BasicDAOException
  {
    DAOFactory factory = factories.get(type);
    if (factory == null) {
      throw new BasicDAOException(
              String.format("unknown factory type [%s]",
                            type));
    }
    try {
      return (T) factory.getUnused(true);
    }
    catch(PoolManagerException ex) {
      throw new BasicDAOException(ex);
    }
  }
  
  public <T> T get(Class<T> type) 
          throws BasicDAOException
  {
    return getDAOFactory(type);
  }
}
