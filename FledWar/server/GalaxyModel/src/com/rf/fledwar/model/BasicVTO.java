/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.rf.fledwar.model.util.JsonHelper;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author REx
 */
public abstract class BasicVTO
{
    private Map data;
    
    protected BasicVTO()
    {
        data = new BasicDBObject();
    }
    
    protected BasicVTO(Map data)
    {
        this.data = Objects.requireNonNull(data, "data");
    }
    
    public final BasicDBObject getDBObject()
    {
        return (BasicDBObject) data;
    }
    
    public final Map getDataAsMap()
    {
        return data;
    }
    
    public Object get(String key)
    {
        return data.get(key);
    }
    
    public void put(String key, Object value)
    {
        data.put(key, value);
    }
    
    protected Map ensuredObjectGet(String key)
    {
        Map object = (Map) get(key);
        if (object == null)
        {
            object = new BasicDBObject();
            data.put(key, object);
        }
        return object;
    }
    
    protected List ensuredListGet(String key)
    {
        List object = (List) get(key);
        if (object == null)
        {
            object = new BasicDBList();
            data.put(key, object);
        }
        return object;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final BasicVTO other = (BasicVTO) obj;
        if (!Objects.equals(this.data, other.data))
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return String.format("VTO%s", data);
    }
    
    public String toPrettyString()
    {
        return JsonHelper.toPrettyJson(data);
    }
}
