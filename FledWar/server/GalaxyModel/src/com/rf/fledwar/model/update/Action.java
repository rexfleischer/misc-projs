/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.update;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class Action
{
    private Map<String, Object> values;
    
    public Action()
    {
    }
    
    protected final Map<String, Object> getValues()
    {
        if (values == null)
        {
            values = new HashMap<>();
        }
        return values;
    }
    
    public final void init(Map<String, Object> values)
    {
        if (this.values != null)
        {
            throw new IllegalStateException("values already init");
        }
        this.values = values;
    }
    
    public final Action putValue(String key, Object value)
    {
        getValues().put(key, value);
        return this;
    }
    
    public final Object getValue(String key)
    {
        return getValues().get(key);
    }
    
    public abstract void exec() throws ActionException;
}
