/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect;

import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationProvider;

/**
 *
 * @author REx
 */
public class ConversationProviderServer extends ConversationProvider
{

    public ConversationProviderServer()
            throws ConversationException
    {
        super();
        
        registerConversation(Conversation_Ping.NAME,  Conversation_Ping.class);
        registerConversation(Conversation_Close.NAME, Conversation_Close.class);
        
        registerConversation(Conversation_Query.NAME, 
                             Conversation_Query.class);
        registerConversation(Conversation_Focus.CONVERSATION_NAME, 
                             Conversation_Focus.class);
    }
    
}
