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
 *
 * @author REx
 */
public class Conversation_Close extends Conversation
{
    public static final String NAME = "close";
    
    public static final int TIMEOUT = 500;

    public Conversation_Close()
    {
        super(NAME);
        this.setTimeout(TIMEOUT);
    }
    
    @Override
    public Message getInitialMessage() throws ConversationException
    {
        setState(ConversationState.CLOSE_CONNECTION);
        return getEmptyMessage();
    }

    @Override
    public Message execute(Message message) throws ConversationException
    {
        setState(ConversationState.CLOSE_CONNECTION);
        return null;
    }

    @Override
    public Message waitTimeoutMessage()
    {
        setState(ConversationState.CLOSE_CONNECTION);
        return null;
    }
    
}
