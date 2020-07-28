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
public class Conversation_Query extends Conversation
{
    public static final String NAME = "query";
    
    public static final String QUERY_NAME = "queryname";
    
    public static final String RESULT = "result";

    public Conversation_Query()
    {
        super(NAME);
        setState(ConversationState.WAITING);
    }

    @Override
    public Message getInitialMessage() throws ConversationException
    {
        return null;
    }

    @Override
    public Message execute(Message message) throws ConversationException
    {
        String queryname = (String) message.getValue(QUERY_NAME);
        Object result = message.getValue(RESULT);
        
        data.commonData.put(queryname, result);
        
        return null;
    }

    @Override
    public Message waitTimeoutMessage() throws ConversationException
    {
        return null;
    }
    
}
