/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.server.user;

import com.fledwar.blackbox.BlackboxEngine;
import com.fledwar.blackbox.connection.BlackboxConnection;
import com.fledwar.blackbox.scope.SimulationFeatureDrop;
import com.fledwar.server.FledWarServer;
import com.fledwar.util.JsonBuilder;
import com.fledwar.util.JsonHelper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class WSConnectionHandler extends MessageInbound
{
  private static final Logger logger = Logger
          .getLogger(WSConnectionHandler.class);
  
  WsOutbound writer;
  
  BlackboxConnection connection;
  
  private boolean closed;
  
  private final String remote_user;
  
  private final String remote_address;
  
  private final String remote_host;
  
  private final int remote_port;
  
  final UserWebSocketServlet parent;

  WSConnectionHandler(HttpServletRequest hsr, UserWebSocketServlet parent)
  {
    remote_user = hsr.getRemoteUser();
    remote_address = hsr.getRemoteAddr();
    remote_host = hsr.getRemoteHost();
    remote_port = hsr.getRemotePort();
    closed = false;
    this.parent = parent;
  }

  public void writeMessage(String message)
          throws IOException
  {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format("raw message sending %s", message));
    }

    CharBuffer buffer = CharBuffer.wrap(message.toCharArray());
    writer.writeTextMessage(buffer);
    writer.flush();
  }

  //<editor-fold defaultstate="collapsed" desc="ws event">
  @Override
  protected void onOpen(WsOutbound outbound)
  {
    super.onOpen(outbound);

    if (logger.isInfoEnabled()) {
      String report = String.format(
              "ws openning connection info: \n"
              + "        -- remote user: %s\n"
              + "        -- remote address: %s\n"
              + "        -- remote host: %s\n"
              + "        -- remote port: %s",
              remote_user,
              remote_address,
              remote_host,
              remote_port);
      logger.info(report);
    }

    writer = outbound;
  }

  @Override
  protected void onClose(int status)
  {
    super.onClose(status);

    if (logger.isInfoEnabled()) {
      String report = String.format(
              "ws closing connection info: \n"
              + "        -- remote user: %s\n"
              + "        -- remote address: %s\n"
              + "        -- remote host: %s\n"
              + "        -- remote port: %s\n"
              + "        -- closing status: %s",
              remote_user,
              remote_address,
              remote_host,
              remote_port,
              status);
      logger.info(report);
    }

    closed = true;
    writer = null;

    if (connection != null) {
      connection.endConnection();
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="inbound messaging">
  @Override
  protected void onBinaryMessage(ByteBuffer bb) throws IOException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void onTextMessage(CharBuffer cb) throws IOException
  {
    if (connection != null && !connection.isLoggedIn()) {
      closed = true;
    }
    if (closed) {
      logger.warn(String.format(
              "attempting to send message on closed connection %s",
              cb));
      String close_message = JsonBuilder.ok()
              .put("close", true)
              .build();
      writeMessage(close_message);
      return;
    }

    if (logger.isDebugEnabled()) {
      logger.debug(String.format("raw message received %s", cb));
    }

    Map message;
    try {
      message = (Map) JsonHelper.toJavaMap(cb.toString());
    }
    catch(Exception ex) {
      logger.error(String.format("unable to parse message %s", cb), ex);
      return;
    }

    if (connection == null) {
      message = initConnection(message);
      if (message == null) {
        return;
      }
    }

    String response = handleMessage(message);

    if (response != null) {
      writeMessage(response);
    }
  }

  private String handleMessage(Map message)
  {
    String response;
    try {
      Object result;
      if (message.containsKey(BlackboxConnection.ACTION_TYPE)) {
        result = connection.action(new HashMap(message));
      }
      else if (message.containsKey(BlackboxConnection.FOCUS_TYPE)) {
        Map input = new HashMap(message);
        FeatureDropCallback callback = new FeatureDropCallback();
        input.put("callback", callback);
        SimulationFeatureDrop drop = connection.focus(input);
        callback.parent = this;
        callback.drop_key = drop.getDropKey();
        result = drop.getDropKey();
      }
      else if (message.containsKey(BlackboxConnection.QUERY_TYPE)) {
        result = connection.query(new HashMap(message));
      }
      else {
        return null;
      }

      response = JsonBuilder.ok()
              .put("original", message)
              .put("response", result)
              .build();
    }
    catch(Exception ex) {
      logger.error(String.format("unable to process command %s",
                                 message),
                   ex);
      response = JsonBuilder.not_ok()
              .put("original", message)
              .put("response", ex.getMessage())
              .build();
    }

    return response;
  }

  private Map initConnection(Map message)
  {
    synchronized(this) {
      if (connection == null) {
        if (closed) {
          return null;
        }

        BlackboxEngine engine = FledWarServer.getEngine();
        try {
          connection = (BlackboxConnection) engine.command(
                  parent.conn_init,
                  new HashMap(message));
        }
        catch(Exception ex) {
          logger.error(String.format(
                  "unable to get BlackboxConnection %s",
                  message),
                       ex);
          closed = true;
        }

        if (connection == null) {
          closed = true;
          logger.info(String.format("did not accept connection %s",
                                    message));
        }
        else {
          logger.info(String.format("accepted connection %s",
                                    message));
        }

        return null;
      }
      else {
        return message;
      }
    }
  }
  //</editor-fold>
}
