/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.curators;

import com.rf.fledwar.javaclient.connection.conversation.ConversationKeys;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.MessageCurator;
import java.util.UUID;

/**
 *
 * @author REx
 */
public class AttachSessionKey implements MessageCurator
{

    @Override
    public Message curate(ConnectionLiaison liaison, Message message)
    {
        UUID putting = (UUID) liaison.commonData.get(ConversationKeys.SESSION_KEY);
        if (putting == null)
        {
            throw new IllegalStateException("uuid must be specified");
        }
        message.setSessionKey(putting);
        return message;
    }
    
}
