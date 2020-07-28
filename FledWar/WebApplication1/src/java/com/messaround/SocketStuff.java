/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messaround;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import javax.servlet.annotation.WebServlet;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

/**
 *
 * @author REx
 */
public class SocketStuff extends MessageInbound
{
    WsOutbound writer;
    
    SocketStuff()
    {
        writer = getWsOutbound();
    }

    @Override
    protected void onBinaryMessage(ByteBuffer bb) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onTextMessage(CharBuffer cb) throws IOException
    {
        System.out.println(cb);
    }
    
}
