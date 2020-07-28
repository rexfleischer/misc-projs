/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection.handlers;

import com.rf.fledwar.socket.connection.ConnectionLiaison;
import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.StateHandler;
import com.rf.fledwar.socket.connection.StateHandlerException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author REx
 */
public class AsyncRequestHandler extends StateHandler
{
    public static final String NAME = "asyncreq";

    public AsyncRequestHandler()
    {
        super(NAME);
    }

    @Override
    public ConnectionState handleState(ConnectionLiaison data) 
            throws StateHandlerException
    {
        List list = (List) data.commonData.get(NAME);
        if (list.isEmpty())
        {
            return null;
        }
        
        Iterator it = list.iterator();
        while(it.hasNext())
        {
            AsyncRequest request = (AsyncRequest) it.next();
            Object result = data.commonData.remove(request.name);
            if (result != null)
            {
                request.callback.callback(result);
                it.remove();
            }
        }
        
        return null;
    }
    
}
