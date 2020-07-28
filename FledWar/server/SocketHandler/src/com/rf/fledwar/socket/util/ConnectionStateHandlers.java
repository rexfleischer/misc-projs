/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.util;

import com.rf.fledwar.socket.connection.ConnectionState;
import com.rf.fledwar.socket.connection.StateHandlerPipeline;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author REx
 */
public class ConnectionStateHandlers
{
    public static final String HANDLER_START = "connection.handler.";
    
    public static Map<ConnectionState, StateHandlerPipeline> getHandlers(Properties properties) 
            throws Exception
    {
        Map<ConnectionState, StateHandlerPipeline> result = new HashMap<>();
        
        Iterator it = properties.keySet().iterator();
        while(it.hasNext())
        {
            String key = it.next().toString();
            if (key.startsWith(HANDLER_START))
            {
                String pipelinename = key.substring(HANDLER_START.length());
                ConnectionState state = ConnectionState.valueOf(
                        pipelinename.toUpperCase());
                StateHandlerPipeline pipeline = new StateHandlerPipeline(
                        properties.getProperty(key));
                
                result.put(state, pipeline);
            }
        }
        
        return result;
    }
}
