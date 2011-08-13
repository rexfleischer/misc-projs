/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rf.dcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author REx
 */
public class ImmutableData
{
    private final Map<String, Object> data;

    public ImmutableData(Map<String, Object> data)
    {
        this.data = new HashMap<String, Object>(data);
    }

    public Set<String> getKeySet()
    {
        return data.keySet();
    }

    public Object getData(String key)
    {
        return data.get(key);
    }
}
