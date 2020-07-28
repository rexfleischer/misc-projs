/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection;

import com.rf.fledwar.javaclient.connection.conversation.Conversation_Focus;
import com.rf.fledwar.javaclient.connection.conversation.Conversation_Query;
import com.rf.fledwar.javaclient.connection.handlers.AsyncRequest;
import com.rf.fledwar.javaclient.connection.handlers.AsyncRequestHandler;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.Connection;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.ConversationProvider;
import com.rf.fledwar.socket.connection.StateHandlerException;
import com.rf.fledwar.socket.connection.StateHandlerPipeline;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class ClientConnection extends Connection
{
    public ClientConnection(Socket socket, 
                            ConversationProvider provider,
                            Map<ConnectionState, StateHandlerPipeline> handlers) 
            throws IOException, StateHandlerException
    {
        super(socket, provider, handlers);
        data.commonData.put(AsyncRequestHandler.NAME, new ArrayList());
        data.commonData.put(Conversation_Query.NAME, new ArrayList());
        data.commonData.put(Conversation_Focus.NAME, new HashMap());
        initConversation(Conversation_Query.NAME, null, false);
        initConversation(Conversation_Focus.NAME, null, false);
    }
    
    public void close()
    {
        this.initConversation("close", null, false);
        try
        {
            this.join();
        }
        catch(InterruptedException ex) {}
    }
    
    public void initQuery(String queryname, Map<String, Object> options)
    {
        if (options == null)
        {
            options = new HashMap<>();
        }
        options.put(Conversation_Query.QUERY_NAME, queryname);
        synchronized(data.commonData)
        {
            ((List) data.commonData.get(Conversation_Query.NAME)).add(options);
        }
    }
    
    public Object blockingQuery(String queryname,
                                Map<String, Object> options,
                                int maxwait) 
    {
        initQuery(queryname, options);
        Object result = null;
        long stopat = System.currentTimeMillis() + maxwait;
        do
        {
            try
            {
                Thread.sleep(50);
            }
            catch(InterruptedException ex) { }
            
            synchronized(data.commonData)
            {
                result = this.data.commonData.remove(queryname);
            }
        }
        while(stopat > System.currentTimeMillis() && result == null);
        
        return result;
    }
    
    public void asyncQuery(String queryname, 
                           Map<String, Object> options, 
                           RequestCallback callback)
    {
        synchronized(data.commonData)
        {
            initQuery(queryname, options);
            AsyncRequest request = new AsyncRequest();
            request.name = queryname;
            request.callback = callback;
            ((List) data.commonData.get(AsyncRequestHandler.NAME)).add(request);
        }
    }
    
    public void setFocus(FocusQueryBuilder builder) 
            throws IOException
    {
        Message message = builder.getMessage();
        message.setProtocol(Conversation_Focus.NAME);
        
        synchronized(data)
        {
            data.write(message);
        }
    }
    
    public void endFocus(String focusname) throws IOException
    {
        Message message = new Message();
        message.setProtocol(Conversation_Focus.NAME);
        message.putValue(Conversation_Focus.FOCUS_NAME, focusname);
        message.putValue(Conversation_Focus.FOCUS_END, true);
        synchronized(data)
        {
            data.write(message);
        }
    }
    
    public Object getFocus(String focuskey)
    {
        synchronized(data.commonData)
        {
            return ((Map) data.commonData.get(Conversation_Focus.NAME))
                    .remove(focuskey);
        }
    }
    
    public String getConnectionAddress()
    {
        return String.format("%s:%s", data.socket.getSocket().getInetAddress(), 
                                      data.socket.getSocket().getPort());
    }
}
