/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect;

import com.rf.fledwar.server.connect.query.QueryException;
import com.rf.fledwar.server.connect.query.QueryRouter;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.ConversationException;
import com.rf.fledwar.socket.connection.ConversationState;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class Conversation_Query extends Conversation
{
    private static final Logger logger = Logger.getLogger(Conversation_Query.class);
    
    public static final String NAME = "query";
    
    public static final String QUERY_NAME = "queryname";
    
    public static final String RESULT = "result";
    
    public Conversation_Query()
    {
        super(NAME);
    }

    @Override
    public Message getInitialMessage() throws ConversationException
    {
        throw new UnsupportedOperationException(
                "server cannot start this message");
    }

    @Override
    public Message execute(Message message) throws ConversationException
    {
        setState(ConversationState.WAITING);
        if (logger.isDebugEnabled())
        {
            logger.debug(String.format("executing query %s", message));
        }
        
        message.getData().remove(Message.PROTOCOL_KEY);
        
        String queryname = (String) message.getValue(QUERY_NAME);
        if (queryname == null || queryname.isEmpty())
        {
            message.putValue("error", "the key 'queryname' must "
                                    + "be specified with a valid query");
            return message;
        }
        
        try
        {
            Object result = QueryRouter.route(data, queryname, message);
            if (result != null)
            {
                message.putValue(RESULT, result);
            }
        }
        catch(QueryException ex)
        {
            logger.warn("correctly handled query exception", ex);
            message.putValue("error", ex.getMessage());
        }
        catch(Throwable ex)
        {
            logger.error("error while performing query", ex);
            message.putValue("error", "there was an unexpected server-side exception");
        }
        
        return message;
    }

    @Override
    public Message waitTimeoutMessage() throws ConversationException
    {
        // do nothing
        return null;
    }
    
}
