/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.unit;

import com.fledwar.vto.BaseVTO;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 * 
 * @author REx
 */
public class UnitAction extends BaseVTO
{
  public static final long NULL_LONG = -1l;
  
  public static final String UNIT_ID = "unit_id";
  
  public static final String SCOPE = "scope";
  
  public static final String TYPE = "type";
  
  public static final String CANCELED_TIME = "canceled_time";
  
  public static final String START_TIME = "start_time";
  
  private boolean finished = false;
  
  public UnitAction()
  {
    base();
  }
  
  public UnitAction(Map data)
  {
    super(data);
    if (data.isEmpty()) {
      base();
    }
  }
  
  private void base() {
    setActionType(UnitActionType.NONE);
    put(CANCELED_TIME, NULL_LONG);
    put(START_TIME, NULL_LONG);
  }
  
  public UnitActionType getActionType()
  {
    return UnitActionType.valueOf(getAsString(TYPE));
  }
  
  public void setActionType(UnitActionType type)
  {
    put(TYPE, type.name());
  }
  
  public boolean hasFinished()
  {
    return finished;
  }
  
  public void markFinished()
  {
    finished = true;
  }
  
  public boolean hasStarted()
  {
    return (getAsLong(START_TIME) != NULL_LONG);
  }
  
  public void setStartTime(long start_time)
  {
    if (hasStarted()) {
      throw new IllegalStateException("action already started");
    }
    put(START_TIME, start_time);
  }
  
  public long getStartTime()
  {
    return getAsLong(START_TIME);
  }
  
  public boolean hasCanceled()
  {
    return (getAsLong(CANCELED_TIME) != NULL_LONG);
  }
  
  public void setCanceledTime(long canceled_time)
  {
    if (hasCanceled()) {
      throw new IllegalStateException("action already canceled");
    }
    this.put(CANCELED_TIME, canceled_time);
  }
  
  public long getCanceledTime()
  {
    return getAsLong(CANCELED_TIME);
  }
  
  public ObjectId getUnitId()
  {
    return getAsObjectId(UNIT_ID);
  }
  
  public void setUnitId(ObjectId unit_id)
  {
    put(UNIT_ID, unit_id);
  }
  
  public ObjectId getScope()
  {
    return getAsObjectId(SCOPE);
  }
  
  public void setScope(ObjectId unit_id)
  {
    put(SCOPE, unit_id);
  }
  
}
