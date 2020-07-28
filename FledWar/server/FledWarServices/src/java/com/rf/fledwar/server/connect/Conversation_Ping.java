/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationState;

/**
 * this is the simplest protocol because all it does is
 * send an empty message to the client and all the client
 * is supposed to do is send and empty message back.
 * @author REx
 */
public class Conversation_Ping extends Conversation
{
    public static final String NAME = "ping";
    
    public static final int TIMEOUT = 2000;

    public Conversation_Ping()
    {
        super(NAME);
        this.setTimeout(TIMEOUT);
    }
    
    

    @Override
    public Message getInitialMessage() throws ConversationException
    {
        return getEmptyMessage();
    }

    @Override
    public Message execute(Message message) throws ConversationException
    {
        Message result;
        if (this.thisStarted)
        {
            // if server started this protocol, then that means
            // that the client has responsed
            result = null;
        }
        else
        {
            // if the client started this protocol, then we need
            // to send a message back in order to say the server
            // got it
            result = getEmptyMessage();
        }
        
        setState(ConversationState.FINISHED);
        
        return result;
    }

    @Override
    public Message waitTimeoutMessage()
    {
        setState(ConversationState.CLOSE_CONNECTION);
        return null;
    }
    
}
