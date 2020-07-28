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
public class ReceiveMessages extends StateHandler
{
    private static final Logger logger = Logger.getLogger(DoNothingOpen.class);
    
    public static final String NAME = "receivemessages";

    public ReceiveMessages()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data)
            throws StateHandlerException
    {
        if (logger.isTraceEnabled())
        {
            logger.trace(ReceiveMessages.class);
        }
        
        try
        {
            Message message;
            while((message = data.read()) != null)
            {
                Conversation conversation = data.conversations.get(message.getName());
                if (conversation == null)
                {
                    // we need to start a new conversation... getting
                    // here means the other side started the conversation
                    conversation = data.provider.provide(message.getName());
                    if (conversation == null)
                    {
                        throw new StateHandlerException(
                                String.format("unknown conversation type [message:%s]", 
                                              message));
                    }
                    
                    if (logger.isDebugEnabled())
                    {
                        logger.debug(String.format("starting conversation [name:%s]",
                                                   message.getName()));
                    }

                    conversation.setData(data);
                    conversation.setThisStarted(false);
                    data.conversations.put(conversation.getName(), conversation);
                }

                // execute the conversation
                Message response = conversation.execute(message);
                conversation.triggerLastResponse();
                data.triggerMessageRecieved();
                if (response != null)
                {
                    data.write(response);
                }
            }
        }
        catch(ConversationException | IOException ex)
        {
            throw new StateHandlerException(
                    "error while receiving messages", 
                    ex);
        }
        
        return null;
    }
}
