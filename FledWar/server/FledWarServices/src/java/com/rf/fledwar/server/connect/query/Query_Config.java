/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;

/**
 *
 * @author REx
 */
public class Query_Config implements Query
{

    @Override
    public Object exec(ConnectionLiaison liaison, Message message) throws Exception
    {
        return GalaxyModelEngine.getGMProperties();
    }
    
}
