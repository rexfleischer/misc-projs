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
import com.rf.fledwar.socket.connection.ConversationInit;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class InitConversations extends StateHandler
{
    private static final Logger logger = Logger.getLogger(DoNothingOpen.class);
    
    public static final String NAME = "initer";

    public InitConversations()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) 
            throws StateHandlerException
    {
        if (logger.isTraceEnabled())
        {
            logger.trace(InitConversations.class);
        }
        
        while(!data.toInit.isEmpty())
        {
            ConversationInit convparams = data.toInit.remove(0);
            Conversation conversation;
            try
            {
                conversation = data.provider.provide(convparams.name);
            }
            catch(ConversationException ex)
            {
                throw new StateHandlerException(
                        "could not init conversation", 
                        ex);
            }

            if (conversation == null)
            {
                String errormsg = String.format("unknown conversation [name:%s]", 
                                                convparams.name);
                logger.error(errormsg);
                throw new StateHandlerException(errormsg);
            }

            if (logger.isDebugEnabled())
            {
                logger.debug(String.format("init conversation [name:%s]", 
                                           convparams.name));
            }

            conversation.setData(data);
            conversation.setThisStarted(true);
            conversation.setParams(convparams.options);

            Message firstmessage;
            try
            {
                firstmessage = conversation.getInitialMessage();
            }
            catch(ConversationException ex)
            {
                throw new StateHandlerException(
                        "could not get initial message", 
                        ex);
            }
            data.conversations.put(conversation.getName(), conversation);
            
            if (firstmessage != null)
            {
                try
                {
                    data.write(firstmessage);
                }
                catch(IOException ex)
                {
                    throw new StateHandlerException(
                            "could not write message",
                            ex);
                }
            }
        }
        
        return null;
    }
    
}
