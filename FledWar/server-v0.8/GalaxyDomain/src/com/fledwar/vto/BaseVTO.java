/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto;

import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public abstract class BaseVTO extends BasicVTO
{
  public static final String ID = "_id";
  
  public static final String NAME = "name";

  public BaseVTO()
  {
  }

  public BaseVTO(Map data)
  {
    super(data);
  }
  
  public ObjectId getId()
  {
    return getAsObjectId(ID);
  }
  
  public void generateId()
  {
    if (this.get(ID) != null) {
      throw new IllegalStateException(
              "unable to generate id when id is already set");
    }
    put(ID, ObjectId.get());
  }
  
  public String getName()
  {
    return getAsString(NAME);
  }
  
  public void setName(String name) 
  {
    put(NAME, name);
  }
  
}
