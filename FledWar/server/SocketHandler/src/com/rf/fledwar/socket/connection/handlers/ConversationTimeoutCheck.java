/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection.handlers;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class ConversationTimeoutCheck extends StateHandler
{
    private static final Logger logger = Logger.getLogger(ConversationTimeoutCheck.class);
    
    public static final String NAME = "timoutcheck";

    public ConversationTimeoutCheck()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) 
            throws StateHandlerException
    {
        if (logger.isTraceEnabled())
        {
            logger.trace(ConversationTimeoutCheck.class);
        }
        
        // now see if there are any messages that 
        // should be sent from other conversations
        // that may be timed out
        long currenttime = System.currentTimeMillis();
        for(Conversation conversation : data.conversations.values())
        {
            if (conversation.getTimeout() == -1)
            {
                continue;
            }
            if (conversation.getLastResponse() + 
                conversation.getTimeout() < currenttime)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug(String.format("conversation timeout [name:%s]", 
                                               conversation.getName()));
                }
                
                try
                {
                    Message sending = conversation.waitTimeoutMessage();
                    if (sending != null)
                    {
                        data.write(sending);
                    }
                }
                catch(ConversationException | IOException ex)
                {
                    throw new StateHandlerException(
                            "could not send timeout message", 
                            ex);
                }
            }
        }
        
        return null;
    }
    
}
