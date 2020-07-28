/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection.handlers;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.Conversation;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class SendErrorAndClose extends StateHandler
{
    private static final Logger logger = Logger.getLogger(DoNothingOpen.class);
    
    public static final String NAME = "error";

    public SendErrorAndClose()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) 
            throws StateHandlerException
    {
        if (logger.isTraceEnabled())
        {
            logger.trace(SendErrorAndClose.class);
        }
        
        try
        {
            Conversation conversation = data.provider.provide(NAME);
            Message message = conversation.getInitialMessage();
            data.write(message);
        }
        catch(Throwable ex)
        {
            logger.error("could not write close message", ex);
        }
        
        return ConnectionState.CLOSE;
    }
    
}
