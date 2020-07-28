/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.blackbox.balancer;

import com.fledwar.blackbox.simulation.BlackboxSimulation;
import com.fledwar.dao.GalaxyScopeDAO;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class DefaultGalaxyScopeBalancer implements GalaxyScopeBalancer
{
  private static final Logger logger = Logger.getLogger(
            DefaultGalaxyScopeBalancer.class);
  
  protected BlackboxSimulation parent;
  
  protected ScheduledFuture future;
  
  @Override
  public void init(BlackboxSimulation parent) 
  {
    this.parent = parent;
  }
  
  @Override
  public void start()
  {
    future = parent.getThreadPool().scheduleAtFixedRate(
            new CheckerTask(),
            1000, 
            2000, 
            TimeUnit.MILLISECONDS);
  }
  
  @Override
  public void shutdown()
  {
    future.cancel(true);
  }

  @Override
  public Map status()
  {
    Map result = new LinkedHashMap();
    result.put("clazz", this.getClass().toString());
    return result;
  }

  class CheckerTask implements Runnable
  {

    @Override
    public void run()
    {
      if (logger.isDebugEnabled()) {
        logger.debug("running galaxy scope check");
      }

      try {
        List<ObjectId> galaxy_ids;
        try(GalaxyScopeDAO dao = parent.getDAOFactoryRegistry()
                        .getDAOFactory(GalaxyScopeDAO.class)) {
          galaxy_ids = dao.getAllIds();
        }
        
        List<ObjectId> galaxy_scope = parent.getScopeIds();
        
        
        while(!galaxy_scope.isEmpty()) {
          ObjectId checking = galaxy_scope.remove(0);

          boolean match = galaxy_ids.remove(checking);
          if (!match) {
            logger.info(String.format(
                    "removing system to scope: %s",
                    checking));
            parent.removeScope(checking);
          }
        }
        
        while(!galaxy_ids.isEmpty()) {
          ObjectId checking = galaxy_ids.remove(0);
          logger.info(String.format(
                  "adding system to scope: %s",
                  checking));
          parent.addScope(checking);

        }
      }
      catch(Exception ex) {
        logger.error("error while trying to balance thread", ex);
      }
    }
  }
}
