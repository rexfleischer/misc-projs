/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.handlers;

import com.rf.fledwar.javaclient.connection.conversation.Conversation_Query;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class SendQueryHandler extends StateHandler
{
    public static final String NAME = "query";

    public SendQueryHandler()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) throws StateHandlerException
    {
        List list = (List) data.commonData.get(NAME);
        if (list.isEmpty())
        {
            return null;
        }
        
        Iterator it = list.iterator();
        while(it.hasNext())
        {
            Map query = (Map) it.next();
            Message message = new Message();
            message.setProtocol(Conversation_Query.NAME);
            
            for(Object key : query.keySet())
            {
                Object value = query.get(key);
                message.putValue(key, value);
            }
            
            try
            {
                data.write(message);
            }
            catch(IOException ex)
            {
                throw new StateHandlerException(ex);
            }
        }
        
        list.clear();
        
        return null;
    }
    
}
