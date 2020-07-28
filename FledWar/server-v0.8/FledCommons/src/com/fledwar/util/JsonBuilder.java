/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class JsonBuilder
{
    public static JsonBuilder ok()
    {
        return new JsonBuilder(true);
    }
    
    public static JsonBuilder not_ok()
    {
        return new JsonBuilder(false);
    }
    
    private Map root;

    public JsonBuilder(boolean ok)
    {
        root = new LinkedHashMap();
        root.put("ok", ok);
    }
    
    public JsonBuilder()
    {
        root = new LinkedHashMap();
    }

    public Map getRoot()
    {
        return root;
    }
    
    public String build(boolean pretty)
    {
        return pretty ? 
                JsonHelper.toPrettyJson(root) : 
                JsonHelper.toJson(pretty);
    }
    
    public String build()
    {
        return build(true);
    }
    
    public JsonBuilder put(Object key, Object value)
    {
        root.put(key, value);
        return this;
    }

    public JsonBuilder push(Object key, Object value)
    {
        ensureGetList(key).add(value);
        return this;
    }

    public JsonBuilder push(Object key, List value)
    {
        ensureGetList(key).addAll(value);
        return this;
    }

    private List ensureGetList(Object key)
    {
        List array = (List) root.get(key);
        if (array == null)
        {
            array = new ArrayList();
            root.put(key, array);
        }
        return array;
    }
}
