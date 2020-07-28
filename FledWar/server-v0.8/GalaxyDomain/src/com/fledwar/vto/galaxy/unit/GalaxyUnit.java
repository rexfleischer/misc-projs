/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.unit;

import com.fledwar.vto.BaseVTO;
import com.fledwar.vto.BasicVTO;
import com.fledwar.vto.ListWrapper;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;

/**
 * 
 * @author REx
 */
public class GalaxyUnit extends BaseVTO
{
  public static final String OWNER_ID = "owner_id";
  
  public static final Pattern VALID_NAME = Pattern.compile(
          "^[a-zA-Z][-a-zA-Z0-9_@#$%^&*()]{3,30}$");
  
  public static final String TYPE = "type";
  
  public static final String SCOPE = "scope";
  
  public static final String ABILITIES = "abilities";
  
  public static final String ATTRIBUTES = "attributes";
  
  public static final String ORIENTATION = "orientation";
  
  public GalaxyUnit()
  {
    base();
  }
  
  public GalaxyUnit(Map data)
  {
    super(data);
    if (data.isEmpty()) {
      base();
    }
  }
  
  private void base() 
  {
    getOrientation();
    getAbilities();
    getAttributes();
  }
  
  @Override
  public void setName(String name)
  {
    Matcher pass_check = VALID_NAME.matcher(name);
    if (!pass_check.matches()) {
      throw new IllegalArgumentException(
              String.format("unit name %s is invalid", name));
    }
    put(NAME, name);
  }

  public String getType()
  {
    return getAsString(TYPE);
  }

  public void setType(String type)
  {
    put(TYPE, type);
  }
  
  public ObjectId getScope()
  {
    return getAsObjectId(SCOPE);
  }
  
  public void setScope(ObjectId scope) 
  {
    put(SCOPE, scope);
  }

  public ObjectId getOwnerId()
  {
    return getAsObjectId(OWNER_ID);
  }

  public void setOwnerId(ObjectId owner_id)
  {
    put(OWNER_ID, owner_id);
  }

  public ObjectOrientation getOrientation()
  {
    return new ObjectOrientation(ensuredMapGet(ORIENTATION));
  }

  public BasicVTO getAbilities()
  {
    return new BasicVTO(ensuredMapGet(ABILITIES));
  }
  
  public BasicVTO getAttributes()
  {
    return new BasicVTO(ensuredMapGet(ATTRIBUTES));
  }
}
