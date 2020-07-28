/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.conversation;

import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationProvider;

/**
 *
 * @author REx
 */
public class ConversationProviderClient extends ConversationProvider
{

    public ConversationProviderClient() throws ConversationException
    {
        
        registerConversation(Conversation_Query.NAME, 
                             Conversation_Query.class);
        registerConversation(Conversation_Focus.NAME, 
                             Conversation_Focus.class);
        registerConversation(Conversation_Ping.NAME,
                             Conversation_Ping.class);
        registerConversation(Conversation_Close.NAME, 
                             Conversation_Close.class);
    }
    
}
