/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection.handlers;

import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.ConversationInit;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;

/**
 *
 * @author REx
 */
public class ConnectionTimeoutCheck extends StateHandler
{
    public static final String NAME = "conntimoutcheck";
    
    public static final int DEFAULT_TIMEOUT = 2000;

    public ConnectionTimeoutCheck()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data)
            throws StateHandlerException
    {
        long current = System.currentTimeMillis();
        
        if (data.lastmessage + DEFAULT_TIMEOUT <= current)
        {
            // connection timeout....
            if (!data.conversations.containsKey("ping"))
            {
                // send a ping. this will for the connection
                // to try to communicate with the other side
                // and therefore check if the connection is
                // actually still valid
                boolean found = false;
                for(int i = 0; i < data.toInit.size() && !found; i++)
                {
                    ConversationInit initing = data.toInit.get(i);
                    if (initing.name.equals("ping"))
                    {
                        found = true;
                    }
                }
                if (!found)
                {
                    data.toInit.add(new ConversationInit("ping", null));
                }
            }
        }
        
        return null;
    }
    
}
