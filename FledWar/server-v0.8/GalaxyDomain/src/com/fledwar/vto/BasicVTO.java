/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto;

import com.fledwar.mongo.ObjectIdHelper;
import com.fledwar.util.JsonHelper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class BasicVTO
{
  private final Map data;

  public BasicVTO()
  {
    data = new BasicDBObject();
  }

  public BasicVTO(Map data)
  {
    this.data = Objects.requireNonNull(data, "data");
  }

  public final BasicDBObject getDataAsDBObject()
  {
    return (BasicDBObject) data;
  }

  public final Map getDataAsMap()
  {
    return data;
  }

  public final Object get(String key)
  {
    return data.get(key);
  }

  public final void put(String key, Object value)
  {
    data.put(key, value);
  }
  
  public final BasicVTO append(String key, Object value) 
  {
    data.put(key, value);
    return this;
  }

  public final ObjectId getAsObjectId(String key)
  {
    return ObjectIdHelper.parseObject(data.get(key));
  }

  public final String getAsString(String key)
  {
    Object check = data.get(key);
    return (check != null) ? check.toString() : null;
  }

  public final Integer getAsInteger(String key)
  {
    Object check = data.get(key);
    return (check != null) ? ((Number) check).intValue() : null;
  }

  public final Long getAsLong(String key)
  {
    Object check = data.get(key);
    return (check != null) ? ((Number) check).longValue() : null;
  }

  public final Double getAsDouble(String key)
  {
    Object check = data.get(key);
    return (check != null) ? ((Number) check).doubleValue() : null;
  }

  public final Boolean getAsBoolean(String key)
  {
    return (Boolean) data.get(key);
  }

  public final byte[] getAsByteArray(String key)
  {
    return (byte[]) data.get(key);
  }

  public final Map ensuredMapGet(String key)
  {
    Map object = (Map) data.get(key);
    if (object == null) {
      object = new BasicDBObject();
      data.put(key, object);
    }
    return object;
  }

  public final List ensuredListGet(String key)
  {
    List object = (List) data.get(key);
    if (object == null) {
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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final BasicVTO other = (BasicVTO) obj;
    if (!Objects.equals(this.data, other.data)) {
      return false;
    }
    return true;
  }
  
  public BasicVTO copy()
  {
    return new BasicVTO(new BasicDBObject(data));
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
