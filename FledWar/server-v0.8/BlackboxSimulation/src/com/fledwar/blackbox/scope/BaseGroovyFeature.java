/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.scope;

import com.fledwar.configuration.Configuration;
import com.fledwar.configuration.ConfigurationException;
import com.fledwar.groovy.GroovyWrapper;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class BaseGroovyFeature extends SimulationFeature
{
  
  private static final Logger logger = Logger.getLogger(
          BaseGroovyFeature.class);
  
  @Override
  protected Logger getLogger() {
    return logger;
  }
  
  private static final String UPDATE_SCRIPT = "update_script";
  
  private static final String MESSAGE_SCRIPT = "message_script";
  
  private static final String SCRIPT_SCOPE_ID = "scope_id";
  
  private static final String SCRIPT_SCOPE_DATA = "scope_data";
  
  private static final String SCRIPT_SCOPE_CACHE = "scope_cache";
  
  private static final String SCRIPT_FEATURE_CACHE = "feature_cache";
  
  private static final String SCRIPT_FEATURE_CONFIG = "feature_config";
  
  private static final String SCRIPT_FEATURE_DROP_KEY = "feature_drop_key";
  
  private static final String SCRIPT_DAO_REGISTRY = "dao_registry";
  
  private static final String SCRIPT_MESSAGE = "message";
  
  public BaseGroovyFeature(SimulationScope parent,
                           Configuration feature_config) 
  {
    super(parent, feature_config);
    
    this.update_script = feature_config.getAsString(UPDATE_SCRIPT);
    if (this.update_script == null) {
      throw new ConfigurationException(String.format(
              "%s undefined for feature config: %s",
              UPDATE_SCRIPT, feature_config));
    }
    
    this.message_script = feature_config.getAsString(MESSAGE_SCRIPT);
    if (this.message_script == null) {
      logger.warn(String.format(
              "%s undefined for feature config: %s", 
              MESSAGE_SCRIPT, feature_config));
    }
    
    this.script_input = new HashMap<>();
    this.script_input.put(SCRIPT_FEATURE_CACHE, new HashMap<String, Object>());
    this.script_input.put(SCRIPT_FEATURE_CONFIG, super.feature_config);
    this.script_input.put(SCRIPT_FEATURE_DROP_KEY, getFeatureDropKey());
    this.script_input.put(SCRIPT_DAO_REGISTRY, super.parent.dao_registry);
    this.script_input.put(SCRIPT_SCOPE_ID, super.parent.scope_id);
    this.script_input.put(SCRIPT_SCOPE_DATA, super.parent.scope_data);
    this.script_input.put(SCRIPT_SCOPE_CACHE, super.parent.scope_cache);
  }
  
  private String update_script;
  
  private String message_script;
  
  private Map<String, Object> script_input;
  
  @Override
  protected Object update() 
          throws Exception 
  {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format(
              "performing update for scope %s with script %s", 
              getFeatureDropKey(), update_script));
    }
    
    return GroovyWrapper.runScript(update_script, script_input);
  }
  
  @Override
  protected void handleMessage(Map message)
          throws Exception
  {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format(
              "received message for scope %s, handling with "
              + "script %s... message: %s", 
              getFeatureDropKey(), message_script, message));
    }
    
    if (message_script == null) {
      logger.warn(String.format(
              "feature is not configured to receive a message: %s",
              feature_config));
    }
    else {
      Map input = new HashMap(script_input);
      input.put(SCRIPT_MESSAGE, message);
      GroovyWrapper.runScript(message_script, input);
    }
  }
}
