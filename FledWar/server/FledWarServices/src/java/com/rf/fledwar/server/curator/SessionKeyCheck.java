/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.curator;

import com.rf.fledwar.server.connect.ConversationKeys;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.MessageCurator;
import java.util.UUID;

/**
 *
 * @author REx
 */
public class SessionKeyCheck implements MessageCurator
{

    @Override
    public Message curate(ConnectionLiaison liaison, Message message)
    {
        UUID sessionkey = (UUID) liaison.commonData.get(ConversationKeys.SESSION_KEY);
        if(sessionkey == null)
        {
            throw new SessionKeyMismatchException();
        }
        
        UUID checkkey = message.getSessionKey();
        if (checkkey == null)
        {
            throw new SessionKeyMismatchException();
        }
        
        if (!checkkey.equals(sessionkey))
        {
            throw new SessionKeyMismatchException();
        }
        
        return message;
    }
    
}
