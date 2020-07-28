/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messaround;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

/**
 *
 * @author REx
 */
@WebServlet(name = "SocketStuff", urlPatterns =
{
    "/socket"
})
public class UserWebSocket extends WebSocketServlet
{

    @Override
    protected StreamInbound createWebSocketInbound(String string, 
                                                   HttpServletRequest hsr)
    {
        return new SocketStuff();
    }
    
}
