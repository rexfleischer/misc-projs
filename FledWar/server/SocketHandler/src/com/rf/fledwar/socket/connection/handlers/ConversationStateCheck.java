/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection.handlers;

import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class ConversationStateCheck extends StateHandler
{
    private static final Logger logger = Logger.getLogger(ConversationStateCheck.class);
    
    public static final String NAME = "statecheck";

    public ConversationStateCheck()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) throws StateHandlerException
    {
        if (logger.isTraceEnabled())
        {
            logger.trace(ConversationStateCheck.class);
        }
        
        Iterator<String> it = data.conversations.keySet().iterator();
        while(it.hasNext())
        {
            String key = it.next();
            Conversation conversation = data.conversations.get(key);
            switch(conversation.getState())
            {
                // special return case. we just stop what
                // we are doing and return the close message
                case CLOSE_CONNECTION:
                {
                    logger.debug(String.format("conversation sending close signal [name:%s]", 
                                               conversation.getName()));
                    return ConnectionState.CLOSE;
                }
                    
                // this means we need to remove this 
                // protocol from the collection of protocols  
                case FINISHED:
                {
                    logger.debug(String.format("conversation sending finished [name:%s]", 
                                               conversation.getName()));
                    it.remove();
                    break;
                }
                  
                // this is the only way the state can 
                // change here
                case ERROR:
                {
                    logger.debug(String.format("conversation sending error [name:%s]", 
                                               conversation.getName()));
                    return ConnectionState.ERROR;
                }
                        
                /**
                 * do nothing
                 */
                case WAITING:
                {
                    break;
                }

                /**
                 * NEW state is an illegal state here 
                 * because the protocol should have been 
                 * initialized
                 */
                case NEW:
                    throw new IllegalStateException(
                            "NEW state is not allowed here");
                default:
                    throw new IllegalStateException(
                            "unknown state");
            }
        }
        
        return null;
    }
    
}
