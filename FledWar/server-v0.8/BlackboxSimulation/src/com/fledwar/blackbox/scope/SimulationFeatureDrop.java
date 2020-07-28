/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.scope;

import com.fledwar.util.Callback;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class SimulationFeatureDrop
{
  private static final Logger logger = Logger.getLogger(
          SimulationFeatureDrop.class);
  
  private final SimulationScope parent;
  
  private final Callback callback;
  
  SimulationFeatureDrop(SimulationScope parent, 
                        Callback callback)
  {
    this.parent = parent;
    this.callback = callback;
  }
  
  public void cancel()
  {
    synchronized(parent.focuses) {
      parent.focuses.remove(this);
    }
  }
  
  public String getDropKey()
  {
    return String.format("scope-%s", parent.scope_id.toString());
  }
  
  Runnable createRunnableForUpdate(Object update) 
  {
    return new RunnableForUpdate(update);
  }
  
  @Override
  public String toString() 
  {
    return String.format("SimulationFeatureDrop{%s}", getDropKey());
  }
  
  class RunnableForUpdate implements Runnable
  {
    private Object update;
    
    public RunnableForUpdate(Object update)
    {
      this.update = update;
    }

    @Override
    public void run()
    {
      try {
        callback.call(update);
      }
      catch(Exception ex) {
        String error = String.format(
                "unable to complete callback for update: \n%s", 
                update);
        logger.error(error, ex);
      }
    }
  }
  
}
