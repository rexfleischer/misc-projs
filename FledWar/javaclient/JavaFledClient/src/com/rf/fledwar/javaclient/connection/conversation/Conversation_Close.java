/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.conversation;

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

    public Conversation_Close()
    {
        super(NAME);
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
    public Message waitTimeoutMessage() throws ConversationException
    {
        throw new ConversationException("this conversation cannot timeout");
    }
    
}
