/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.socket.connection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this handles running a list of StateHandlers
 * in a consistent way. 
 * @author REx
 */
public final class StateHandlerPipeline
{
    private ArrayList<StateHandler> handlers;
    
    public StateHandlerPipeline()
    {
        this.handlers = new ArrayList<>();
    }
    
    /**
     * 
     * @param rawnames
     * @throws Exception 
     */
    public StateHandlerPipeline(String rawnames)
            throws Exception
    {
        this.handlers = new ArrayList<>();
        String[] splitnames = rawnames.split("\\,");
        for(String name : splitnames)
        {
            Class<? extends StateHandler> handlerclazz = 
                    (Class<? extends StateHandler>) Class.forName(name.trim());
            registerHandler(handlerclazz);
        }
    }
    
    /**
     * 
     * @param rawnames 
     */
    public StateHandlerPipeline(String[] rawnames)
            throws Exception
    {
        this.handlers = new ArrayList<>();
        for(String name : rawnames)
        {
            Class<? extends StateHandler> handlerclazz = 
                    (Class<? extends StateHandler>) Class.forName(name);
            registerHandler(handlerclazz);
        }
    }
    
    /**
     * 
     * @param handlers
     * @throws Exception 
     */
    public StateHandlerPipeline(List<Class<? extends StateHandler>> handlers)
            throws Exception
    {
        this.handlers = new ArrayList<>();
        for(Class<? extends StateHandler> handler : handlers)
        {
            registerHandler(handler);
        }
    }
    
    /**
     * 
     * @param handler 
     */
    public void registerHandler(StateHandler handler)
    {
        Objects.requireNonNull(handler, "handler");
        
        boolean found = false;
        for(int i = 0; i < handlers.size() && !found; i++)
        {
            if (handlers.get(i).name.equals(handler.name))
            {
                handlers.set(i, handler);
                found = true;
            }
        }
        if (!found)
        {
            handlers.add(handler);
        }
    }
    
    /**
     * 
     * @param handler
     * @throws Exception 
     */
    public void registerHandler(Class<? extends StateHandler> handler) 
            throws Exception
    {
        Constructor<? extends StateHandler> constructor = handler.getConstructor();
        registerHandler(constructor.newInstance());
    }
    
    /**
     * 
     * @return 
     */
    public ArrayList<StateHandler> getHandlers()
    {
        return handlers;
    }
    
    /**
     * 
     * @param data
     * @return
     * @throws StateHandlerException 
     */
    public ConnectionState runPipeline(ConnectionLiaison data) 
            throws StateHandlerException
    {
        ConnectionState response = null;
        
        synchronized(data)
        {
            for(int i = 0; i < handlers.size() && response == null; i++)
            {
                response = handlers.get(i).handleState(data);
            }
        }
        
        return response;
    }
}
