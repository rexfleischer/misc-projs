/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.balancer;

import com.fledwar.blackbox.simulation.BlackboxSimulation;

/**
 *
 * @author REx
 */
public interface GalaxyScopeBalancer
{
  public void init(BlackboxSimulation parent);
  
  public abstract void start();

  public abstract void shutdown();

  public abstract Object status();
}
