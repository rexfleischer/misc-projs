/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.conversation;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationState;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author REx
 */
public class Conversation_Focus extends Conversation
{
    public static final String NAME = "focus";
    
    public static final String FOCUS_END = "focusend";
    
    public static final String FOCUS_NAME = "focusname";
    
    public static final String FOCUS_UPDATE = "update";

    public Conversation_Focus()
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
        String key = (String) message.getValue(FOCUS_NAME);
        Object update = message.getValue(FOCUS_UPDATE);
        ((Map) data.commonData.get(Conversation_Focus.NAME)).put(key, update);
        
        return null;
    }

    @Override
    public Message waitTimeoutMessage() throws ConversationException
    {
        return null;
    }
}
