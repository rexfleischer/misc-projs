/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect;

import com.rf.fledwar.server.curator.MessageVerbose;
import com.rf.fledwar.server.curator.SessionKeyCheck;
import com.rf.fledwar.socket.connection.Connection;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.ConversationProvider;
import com.rf.fledwar.socket.connection.StateHandlerException;
import com.rf.fledwar.socket.connection.StateHandlerPipeline;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author REx
 */
public class ConnectionProvider extends Connection
{

    public ConnectionProvider(Socket socket, 
                              ConversationProvider provider, 
                              Map<ConnectionState, StateHandlerPipeline> handlers) 
            throws IOException, StateHandlerException
    {
        super(socket, provider, handlers);
        registerReadCurator(new SessionKeyCheck());
        registerReadCurator(new MessageVerbose("receiving %s"));
        registerWriteCurator(new MessageVerbose("sending %s"));
    }
    
}
