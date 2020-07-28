/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import com.rf.fledwar.socket.JsonSocketHandler;
import com.rf.fledwar.socket.Message;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class ConnectionLiaison
{
    /**
     * the last message received from
     * the 'other side'
     */
    public long lastmessage;
    
    /**
     * the conversations that are active.
     */
    public final Map<String, Conversation> conversations;
    
    /**
     * 
     */
    public final ConversationProvider provider;
    
    /**
     * conversations to initialize during the
     * next working order of the conversations
     */
    public final List<ConversationInit> toInit;
    
    /**
     * data that can be used to store application logic not
     * specific to the connection handler. 
     */
    public final Map<String, Object> commonData;
    
    /**
     * 
     */
    public final JsonSocketHandler socket;
    
    /**
     * 
     */
    public List<MessageCurator> readers;
    
    /**
     * 
     */
    public List<MessageCurator> writers;

    /**
     * 
     * @param handler
     * @param finder 
     */
    public ConnectionLiaison(Socket socket, 
                             ConversationProvider provider, 
                             Logger logger)
            throws IOException
    {
        this.provider       = Objects.requireNonNull(provider, "provider");
        this.socket         = new JsonSocketHandler(socket);
        this.conversations  = new HashMap<>();
        this.toInit         = new ArrayList<>();
        this.commonData     = new HashMap<>();
    }
    
    public ConnectionLiaison()
    {
        this.provider       = null;
        this.socket         = null;
        this.conversations  = new HashMap<>();
        this.toInit         = new ArrayList<>();
        this.commonData     = new HashMap<>();
    }
    
    /**
     * this is supposed to be called every time a complete
     * message is received.
     */
    public void triggerMessageRecieved()
    {
        lastmessage = System.currentTimeMillis();
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    public Message read() throws IOException
    {
        Message result = socket.read();
        if (result == null)
        {
            return null;
        }
        triggerMessageRecieved();
        if (readers != null)
        {
            for(int i = 0, n = readers.size(); i < n; i++)
            {
                result = readers.get(i).curate(this, result);
            }
        }
        return result;
    }
    
    /**
     * 
     * @param message
     * @throws IOException 
     */
    public void write(Message message) throws IOException
    {
        if (writers != null)
        {
            for(int i = 0, n = writers.size(); i < n; i++)
            {
                message = writers.get(i).curate(this, message);
            }
        }
        socket.write(message);
    }
}
