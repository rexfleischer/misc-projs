/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.scope;

import com.fledwar.configuration.Configuration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public abstract class SimulationFeature implements Runnable
{
  protected abstract Object update() throws Exception;
  
  protected abstract Logger getLogger();
  
  private static final String DELAY = "delay";
  
  private static final String DELAY_INIT = "delay_init";
  
  private static final String FEATURE_TYPE = "feature_type";
  
  private static final String FEATURE_DROP_FORMAT = "feature_drop_format";
  
  private ScheduledFuture future;
  
  protected final SimulationScope parent;
  
  protected final Configuration feature_config;
  
  public SimulationFeature(SimulationScope parent,
                           Configuration feature_config) 
  {
    this.parent = Objects.requireNonNull(parent, 
                                         "parent");
    this.feature_config = Objects.requireNonNull(feature_config, 
                                                 "feature_config");
  }
  
  public String getFeatureType()
  {
    return feature_config.getAsString(FEATURE_TYPE);
  }
  
  public String getFeatureDropFromat()
  {
    return feature_config.getAsString(FEATURE_DROP_FORMAT);
  }
  
  public String getFeatureDropKey()
  {
    return String.format(getFeatureDropFromat(), parent.scope_id.toString());
  }
  
  public long getThreadPoolInitialDelay() 
  {
    return feature_config.getAsInteger(DELAY_INIT);
  }
  
  public long getThreadPoolDelay()
  {
    return feature_config.getAsInteger(DELAY);
  }
  
  public void setScheduledFuture(ScheduledFuture future)
  {
    if (this.future != null) {
      throw new IllegalStateException("future already set");
    }
    this.future = future;
  }
  
  public ScheduledFuture getScheduledFuture()
  {
    return future;
  }
  
  protected void handleMessage(Map message) throws Exception
  {
    getLogger().warn(String.format(
            "message was sent to a scope that is not set up to "
            + "process messages: %s", 
            message));
  }
  
  @Override
  public final void run() 
  {
    try {
      Object update = update();
      if (update != null) {
        parent.broadcast(update);
      }
    }
    catch(Exception ex) {
      String report = String.format(
              "error while running update [scope: %s]",
              getFeatureDropKey());
      getLogger().error(report, ex);
    }
  }
}
