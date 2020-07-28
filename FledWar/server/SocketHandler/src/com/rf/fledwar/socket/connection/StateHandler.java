/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import java.util.Objects;

/**
 * 
 * @author REx
 */
public abstract class StateHandler
{
    public final String name;

    public StateHandler(String name)
    {
        this.name = Objects.requireNonNull(name, "name");
    }
    
    /**
     * 
     * @param data 
     * @return returns a request to change the connection state. for
     * the most part, this request will be honored. but state changes
     * mean that the pipeline will stop and not continue with the
     * rest of the states.
     * @throws StateHandlerException 
     */
    public abstract ConnectionState handleState(ConnectionLiaison data)
            throws StateHandlerException;
}
