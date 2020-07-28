/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.update;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class ActionRouter
{
    public static final String BASE_CLASS_PATH = "com.rf.fledwar.gm.action.%s";
    
    public static final String CONFIG_ACTION = "action";
    
    public static final Map<String, Constructor> constructorCache = new HashMap<>();
    
    public static Action route(Map<String, Object> request) 
            throws ActionException
    {
        String actionkey = (String) request.get(CONFIG_ACTION);
        if (actionkey == null)
        {
            throw new ActionException(
                    String.format("key %s must be specified", 
                                  CONFIG_ACTION));
        }
        
        try
        {
            Constructor constructor = constructorCache.get(actionkey);
            if (constructor == null)
            {
                String clazzstring = String.format(BASE_CLASS_PATH, actionkey);
                Class clazz = Class.forName(clazzstring);
                constructor = clazz.getConstructor();
                constructorCache.put(actionkey, constructor);
            }
            Action instance = (Action) constructor.newInstance();
            instance.init(request);
            return instance;
        }
        catch(ClassNotFoundException | 
              NoSuchMethodException |
              SecurityException |
              InstantiationException |
              IllegalAccessException |
              IllegalArgumentException | 
              InvocationTargetException ex)
        {
            throw new ActionException(ex);
        }
    }
}
