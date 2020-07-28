/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.scope;

import com.fledwar.blackbox.simulation.BlackboxSimulationException;
import com.fledwar.configuration.Configuration;
import com.fledwar.configuration.ConfigurationException;
import com.fledwar.dao.BasicDAOException;
import com.fledwar.dao.DAOFactoryRegistry;
import com.fledwar.util.Callback;
import com.fledwar.vto.galaxy.util.ScopeData;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class SimulationScope
{
  private static final Logger logger = Logger.getLogger(SimulationScope.class);
  
  final ObjectId scope_id;
  
  final Configuration feature_configs;
  
  final DAOFactoryRegistry dao_registry;
  
  final ScheduledThreadPoolExecutor thread_pool;
  
  final Map<String, SimulationFeature> features;
  
  final List<SimulationFeatureDrop> focuses;
  
  final ScopeData scope_data;
  
  final Map<String, Object> scope_cache;
  
  private boolean running = false;
  
  public SimulationScope(ObjectId scope_id, 
                         Configuration feature_configs,
                         DAOFactoryRegistry dao_registry,
                         ScheduledThreadPoolExecutor thread_pool)
          throws BlackboxSimulationException
  {
    this.scope_id = Objects.requireNonNull(scope_id, 
                                           "scope_id");
    this.feature_configs = Objects.requireNonNull(feature_configs, 
                                                  "feature_configs");
    this.dao_registry = Objects.requireNonNull(dao_registry, 
                                               "dao_registry");
    this.thread_pool = Objects.requireNonNull(thread_pool, 
                                              "thread_pool");
    
    this.focuses = new ArrayList<>();
    this.features = new HashMap<>();
    this.scope_cache = new HashMap<>();
    try {
      this.scope_data = ScopeData.get(dao_registry, scope_id);
    }
    catch(BasicDAOException ex) {
      throw new BlackboxSimulationException(
              String.format("unable to initialize scope data for %s", scope_id), 
              ex);
    }
    
    Iterator it = feature_configs.keySet().iterator();
    while(it.hasNext()) {
      String feature_key = it.next().toString();
      Configuration feature_config = feature_configs
              .getAsConfiguration(feature_key);
      
      if (logger.isInfoEnabled()) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("initializing feature ");
        buffer.append(feature_key);
        buffer.append(" for scope ");
        buffer.append(scope_id.toString());
        buffer.append("\n");
        
        Iterator key_it = feature_config.keySet().iterator();
        while(key_it.hasNext()) {
          Object key = key_it.next();
          Object value = feature_config.get(key);
          buffer.append("    -- feature config: ");
          buffer.append(key);
          buffer.append(" => ");
          buffer.append(value);
          buffer.append("\n");
        }
        
        logger.info(buffer.toString());
      }
      
      SimulationFeature feature;
      try {
        Class clazz = feature_config.getAsClass(
                Configuration.DEFAULT_CLASS_KEY);
        Constructor constructor = clazz.getConstructor(
                SimulationScope.class, Configuration.class);
        feature = (SimulationFeature) constructor.newInstance(
                this, feature_config);
      }
      catch(Exception ex) {
        throw new BlackboxSimulationException(
                String.format("unable to make instance of feature [%s]",
                              feature_key),
                ex);
      }
      
      if (!feature_key.equals(feature.getFeatureType())) {
        throw new ConfigurationException(String.format(
                "feature key [%s] does not match a feature type [%s]",
                feature.getFeatureType(),
                feature_key));
      }
      
      features.put(feature_key, feature);
    }
  }
  
  public ObjectId getScopeId()
  {
    return scope_id;
  }
  
  public ScopeData getScopeData()
  {
    return scope_data;
  }
  
  public void start()
          throws BlackboxSimulationException, BasicDAOException
  {
    if (running) {
      throw new BlackboxSimulationException("start has already been called");
    }
    
    for(SimulationFeature feature : features.values()) {
      ScheduledFuture future = thread_pool.scheduleAtFixedRate(
              feature, 
              feature.getThreadPoolInitialDelay(), 
              feature.getThreadPoolDelay(), 
              TimeUnit.MILLISECONDS);
      feature.setScheduledFuture(future);
    }
    
    running = true;
  }
  
  public Object status()
  {
    return null;
  }
  
  public void stop()
  {
    synchronized(focuses) {
      for(SimulationFeature feature : features.values()) {
        try {
          if (feature.getScheduledFuture() != null) {
            feature.getScheduledFuture().cancel(true);
          }
        }
        catch(Exception ex) {
          logger.error(String.format("unable to cancel work for feature %s", 
                                     feature.getFeatureType()), 
                       ex);
        }
      }
      
      focuses.clear();
      
      running = false;
    }
  }
  
  public SimulationFeatureDrop focus(Callback callback)
          throws BlackboxSimulationException
  {
    SimulationFeatureDrop drop;
    synchronized(focuses) {
      if (!running) {
        throw new BlackboxSimulationException("scope has not been started");
      }
      
      drop = new SimulationFeatureDrop(this, callback);
      focuses.add(drop);
    }
    return drop;
  }
  
  public void broadcast(Object update) 
  {
    SimulationFeatureDrop[] pushing;
    synchronized(focuses) {
      pushing = focuses.toArray(new SimulationFeatureDrop[focuses.size()]);
    }
    for(SimulationFeatureDrop drop : pushing) {
      Runnable drop_task = drop.createRunnableForUpdate(update);
      thread_pool.execute(drop_task);
    }
  }
  
  public void receiveMessage(Map message) 
          throws BlackboxSimulationException
  {
    if (!running) {
      throw new BlackboxSimulationException("scope has not been started");
    }
  }

  @Override
  public int hashCode() 
  {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.scope_id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) 
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SimulationScope other = (SimulationScope) obj;
    if (!Objects.equals(this.scope_id, other.scope_id)) {
      return false;
    }
    return true;
  }
}
