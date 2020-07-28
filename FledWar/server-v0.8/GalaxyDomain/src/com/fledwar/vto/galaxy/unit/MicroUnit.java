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
public class MicroUnit extends BaseVTO
{

  public static final String UNIT_IN_ID = "unit_in_id";
  
  public static final String UNIT_IN_TYPE = "unit_in_type";

  public MicroUnit()
  {
    setUnitIn(MicroUnitInType.UNKNOWN, null);
  }

  public MicroUnit(Map data)
  {
    super(data);
  }

  public ObjectId getUnitInId()
  {
    return getAsObjectId(UNIT_IN_ID);
  }

  public MicroUnitInType getUnitInType()
  {
    return MicroUnitInType.valueOf(getAsString(UNIT_IN_TYPE));
  }

  public void setUnitIn(MicroUnitInType type, ObjectId id)
  {
    put(UNIT_IN_TYPE, type.name());
    put(UNIT_IN_ID, id);
  }
}
