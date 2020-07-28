/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.handlers;

import com.rf.fledwar.server.connect.ConversationKeys;
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
            if (!data.commonData.containsKey("keymaybe"))
            {
                try
                {
                    Message message = new Message();
                    message.setProtocol(NAME);
                    UUID keymaybe = UUID.randomUUID();
                    
                    message.setSessionKey(keymaybe);
                    data.commonData.put("keymaybe", keymaybe);
                    
                    data.socket.write(message);
                }
                catch(IOException ex)
                {
                    throw new StateHandlerException(
                            "could not init starting conversation", 
                            ex);
                }
            }
            else
            {
                Message message;
                try
                {
                    // we need to bypass the listeners here
                    message = data.socket.read();
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
                    UUID sentkey = (UUID) data.commonData.get("keymaybe");
                    
                    if (!sentkey.equals(message.getSessionKey()))
                    {
                        throw new StateHandlerException("session key mismatch");
                    }

                    data.commonData.put(ConversationKeys.SESSION_KEY, check);
                    data.triggerMessageRecieved();
                    
                    // cleanup
                    data.commonData.remove("keymaybe");
                }
            }
        }
        return data.commonData.containsKey(ConversationKeys.SESSION_KEY) 
                ? ConnectionState.WORK 
                : null;
    }
    
}
