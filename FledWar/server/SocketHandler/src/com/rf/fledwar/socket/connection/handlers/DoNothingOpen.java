/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection.handlers;

import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class DoNothingOpen extends StateHandler
{
    private static final Logger logger = Logger.getLogger(DoNothingOpen.class);
    
    public static final String NAME = "open";

    public DoNothingOpen()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) 
            throws StateHandlerException
    {
        if (logger.isTraceEnabled())
        {
            logger.trace(DoNothingOpen.class);
        }
        
        return ConnectionState.WORK;
    }
    
}
