/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;

/**
 *
 * @author REx
 */
public enum QueryRouter
{
    LISTSYSTEMNAMES(new Query_ListSystemNames()),
    CONFIG(new Query_Config()),
    SYSTEM(new Query_System()),
    SYSTEMID(new Query_SystemId()),
    RANGE(new Query_Range()),
    ;
    
    private Query query;
    
    private QueryRouter(Query query)
    {
        this.query = query;
    }
    
    public static Object route(ConnectionLiaison liaison, 
                               String queryname, 
                               Message message) throws Exception
    {
        try
        {
            QueryRouter routerImpl = valueOf(queryname.toUpperCase());
            return routerImpl.query.exec(liaison, message);
        }
        catch(IllegalArgumentException ex)
        {
            message.putValue("error", String.format("unknown query type [%s]", 
                                                    queryname));
        }
        return null;
    }
}
