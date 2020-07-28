/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;

/**
 *
 * @author REx
 */
public class Query_System implements Query
{
    public static final String QUERY_SYSTEM = "system";
    
    @Override
    public Object exec(ConnectionLiaison liaison, Message message) 
            throws Exception
    {
        String systemname = (String) message.getValue(QUERY_SYSTEM);
        if (systemname == null)
        {
            return null;
        }
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem system = gsdao.findByName(systemname);
        return system.getDBObject();
    }
}
