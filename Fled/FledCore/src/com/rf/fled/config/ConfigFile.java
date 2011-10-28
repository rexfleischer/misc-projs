
package com.rf.fled.config;

import com.rf.fled.interfaces.Configurable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author REx
 */
public class ConfigFile implements Configurable<String>, Serializable
{
    private String context;
    
    private Map<String, String> strings;
    
    private Map<String, Integer> integers;
    
    private Map<String, Boolean> booleans;
    
    protected ConfigFile(String context)
    {
        this.context    = context;
        strings     = new HashMap<String, String>();
        integers    = new HashMap<String, Integer>();
        booleans    = new HashMap<String, Boolean>();
    }
    
    @Override
    public synchronized String configContext()
    {
        return context;
    }

    @Override
    public synchronized void configSetBoolean(String key, Boolean value)
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        if (value == null)
        {
            throw new NullPointerException("value");
        }
        booleans.put(key, value);
    }

    @Override
    public synchronized void configSetInteger(String key, Integer value) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        if (value == null)
        {
            throw new NullPointerException("value");
        }
        integers.put(key, value);
    }

    @Override
    public synchronized void configSetString(String key, String value) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        if (value == null)
        {
            throw new NullPointerException("value");
        }
        strings.put(key, value);
    }

    @Override
    public synchronized boolean configBooleanExists(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        return booleans.containsKey(key);
    }

    @Override
    public synchronized boolean configIntegerExists(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        return integers.containsKey(key);
    }

    @Override
    public synchronized boolean configStringExists(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        return strings.containsKey(key);
    }

    @Override
    public synchronized Boolean configGetBoolean(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        return booleans.get(key);
    }

    @Override
    public synchronized Integer configGetInteger(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        return integers.get(key);
    }

    @Override
    public synchronized String configGetString(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        return strings.get(key);
    }

    @Override
    public synchronized void configRemoveBoolean(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        booleans.remove(key);
    }

    @Override
    public synchronized void configRemoveInteger(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        integers.remove(key);
    }

    @Override
    public synchronized void configRemoveString(String key) 
    {
        if (key == null)
        {
            throw new NullPointerException("key");
        }
        strings.remove(key);
    }
    
}