/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class Query_SystemId implements Query
{
    public static final String SYSTEM_ID = "systemid";

    @Override
    public Object exec(ConnectionLiaison liaison, Message message) throws Exception
    {
        String rawsystemid = (String) message.getValue(SYSTEM_ID);
        if (rawsystemid == null)
        {
            return null;
        }
        
        ObjectId systemid = new ObjectId(rawsystemid);
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        GalaxySystem system = gsdao.findOneFromId(systemid);
        return system.getDBObject();
    }
    
}
