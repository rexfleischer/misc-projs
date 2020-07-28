/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.user;

import com.fledwar.util.Callback;
import com.fledwar.util.JsonBuilder;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class FeatureDropCallback implements Callback
{

  private static final Logger logger = Logger.getLogger(
          FeatureDropCallback.class);
  
  WSConnectionHandler parent;
  
  String drop_key;

  @Override
  public Object call(Object... params) throws Exception
  {
    logger.debug("registering runable for callback update");

    parent.parent.thread_pool.execute(new FeatureDropCallback.PushToUserTask(params[0]));

    return null;
  }

  @Override
  public String toString()
  {
    return String.format("SimulationFeatureDrop{%s}", drop_key);
  }

  class PushToUserTask implements Runnable
  {

    private Object update;

    public PushToUserTask(Object update)
    {
      this.update = update;
    }

    @Override
    public void run()
    {
      logger.debug("sending update to user");

      String raw_update = JsonBuilder.ok()
              .put("focus", drop_key)
              .put("update", update)
              .build();

      try {
        parent.writeMessage(raw_update);
      }
      catch(IOException ex) {
        logger.error(String.format(
                "unable to write update [drop:%s]",
                drop_key),
                     ex);
      }
    }
  }
}
