/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.handlers;

import com.rf.fledwar.javaclient.connection.conversation.ConversationKeys;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author REx
 */
public class OpenHandler extends StateHandler
{
    public static final String NAME = "open";

    public OpenHandler()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data)
            throws StateHandlerException
    {
        if (!data.commonData.containsKey(ConversationKeys.SESSION_KEY))
        {
            Message message;
            try
            {
                message = data.read();
            }
            catch(IOException ex)
            {
                throw new StateHandlerException(
                        "could not read client message during initSession()", 
                        ex);
            }
            if (message != null)
            {
                // get the message and check it
                UUID check = message.getSessionKey();

                if (check == null)
                {
                    throw new StateHandlerException("session key mismatch");
                }

                data.commonData.put(ConversationKeys.SESSION_KEY, check);
                data.triggerMessageRecieved();
                try
                {
                    data.write(message);
                }
                catch(IOException ex)
                {
                    throw new StateHandlerException(
                            "could not write client message during initSession()", 
                            ex);
                }
            }
        }
        return data.commonData.containsKey(ConversationKeys.SESSION_KEY) 
                ? ConnectionState.WORK 
                : null;
    }
    
}
