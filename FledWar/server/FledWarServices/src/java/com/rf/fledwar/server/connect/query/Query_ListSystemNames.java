/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.mongodb.DBCursor;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
class Query_ListSystemNames implements Query
{
    private static final Logger logger = Logger.getLogger(Query_ListSystemNames.class);
    
    @Override
    public Object exec(ConnectionLiaison liaison, Message message) throws Exception
    {
        GalaxySystemDAO dao = new GalaxySystemDAO();

        DBCursor cursor = dao.getCollection().find();
        ArrayList<String> list = new ArrayList<>(cursor.count());
        if (logger.isDebugEnabled())
        {
            logger.debug(String.format("found %s names", cursor.size()));
        }
        while(cursor.hasNext())
        {
            GalaxySystem system = new GalaxySystem((Map) cursor.next());
            list.add(system.getName());
        }

        return list;
    }
}
