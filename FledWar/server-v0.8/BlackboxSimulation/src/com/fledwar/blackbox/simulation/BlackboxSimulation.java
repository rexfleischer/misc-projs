/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.simulation;

import com.fledwar.blackbox.balancer.GalaxyScopeBalancer;
import com.fledwar.blackbox.scope.SimulationScope;
import com.fledwar.configuration.Configuration;
import com.fledwar.configuration.ConfigurationException;
import com.fledwar.dao.DAOFactoryRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class BlackboxSimulation
{

  private static final Logger logger = Logger
          .getLogger(BlackboxSimulation.class);
    
  public static final String BALANCER = "balancer";
  
  public static final String POOL_SIZE = "pool_size";
  
  public static final String FEATURES = "features";
  
  
  
  private final ScheduledThreadPoolExecutor thread_pool;
  
  private final DAOFactoryRegistry dao_registry;
  
  private final Configuration simulation_config;
  
  private final Configuration engine_config;
  
  private final Map<ObjectId, SimulationScope> scopes;
  
  private final GalaxyScopeBalancer balancer;

  public BlackboxSimulation(Configuration engine_config,
                            Configuration simulation_config,
                            DAOFactoryRegistry dao_registry)
          throws BlackboxSimulationException
  {
    this.engine_config = engine_config;
    this.simulation_config = simulation_config;
    this.dao_registry = dao_registry;
    this.scopes = new HashMap<>();

    logger.info("starting new runner");

    try {
      int pool_size = simulation_config.getAsInteger(POOL_SIZE);
      logger.info(String.format("pool_size -> %s", pool_size));
      thread_pool = new ScheduledThreadPoolExecutor(pool_size);
    }
    catch(Exception ex) {
      throw new ConfigurationException(
              "not all required runner config values are present",
              ex);
    }

    try {
      balancer = simulation_config.getAsInstance(GalaxyScopeBalancer.class, 
                                                 BALANCER);
      balancer.init(this);
      balancer.start();
    }
    catch(Exception ex) {
      shutdown();
      throw new BlackboxSimulationException(
              "could not create balancer",
              ex);
    }
  }
  
  public ScheduledThreadPoolExecutor getThreadPool()
  {
    return thread_pool;
  }
  
  public DAOFactoryRegistry getDAOFactoryRegistry()
  {
    return dao_registry;
  }

  public List<ObjectId> getScopeIds()
  {
    return new ArrayList<>(this.scopes.keySet());
  }
  
  public GalaxyScopeBalancer getGalaxyScopeBalancer()
  {
    return balancer;
  }

  public void shutdown()
          throws BlackboxSimulationException
  {
    synchronized(this.scopes) {
      
      for(SimulationScope scope : this.scopes.values()) {
        scope.stop();
      }
      this.scopes.clear();
      
      balancer.shutdown();
      
      thread_pool.shutdown();
    }
  }

  public void addScope(ObjectId scope_id)
          throws BlackboxSimulationException
  {
    synchronized(this.scopes) {
      if (this.scopes.containsKey(scope_id)) {
        throw new BlackboxSimulationException(
                "scope already exists in this simulation");
      }
      
      try {
        SimulationScope new_scope = new SimulationScope(
                scope_id, 
                simulation_config.getAsConfiguration(FEATURES), 
                dao_registry, 
                thread_pool);
        new_scope.start();
        this.scopes.put(scope_id, new_scope);
      }
      catch(Exception ex) {
        throw new BlackboxSimulationException(
                String.format("unable to add scope for %s", 
                              scope_id.toString()), 
                ex);
      }
    }
  }

  public void removeScope(ObjectId scope_id)
          throws BlackboxSimulationException
  {
    synchronized(this.scopes) {
      if (!this.scopes.containsKey(scope_id)) {
        throw new BlackboxSimulationException(String.format(
                "scope doesnt exists in simulation [%s]",
                scope_id));
      }
      
      SimulationScope scope = this.scopes.remove(scope_id);
      scope.stop();
    }
  }
  
  public SimulationScope getScope(ObjectId scope_id) 
          throws BlackboxSimulationException
  {
    synchronized(this.scopes) {
      return this.scopes.get(scope_id);
    }
  }
}
