/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.system;

import com.fledwar.vto.BaseVTO;
import com.fledwar.vto.BasicVTO;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public abstract class SystemPoint extends BaseVTO
{
  public static final String IS_ROOT = "is_root";
  
  public static final String SCOPE = "scope";
  
  public static final String TYPE = "type";
  
  public static final String MASS = "mass";
  
  public static final String RADIUS = "radius";
  
  public static final String LUMINOSITY = "luminosity";
  
  public static final String HEALTH = "health";
  
  public static final String MATERIAL = "material";
  
  public static final String PLANET_TYPE = "planettype";
  
  public static final String PLANET_RING = "planet_ring";
  
  public static final String DIST_OUTER = "dist_outer";
  
  public static final String DIST_INNER = "dist_inner";
  
  public static final String MOON_TYPE = "moontype";
  
  public static final String ORIENTATION = "orientation";
  
  public static final String CHILDREN = "children";

  public SystemPoint(SystemPointType type)
  {
    setType(type);
    setIsRoot(false);
    setMass(0.0);
    getObjectOrientation();
    getChildren();
  }

  public SystemPoint(Map data)
  {
    super(data);
  }
  
  public ObjectId getScope()
  {
    return getAsObjectId(SCOPE);
  }
  
  public void setScope(ObjectId scope)
  {
    this.put(SCOPE, scope);
  }

  public Double getMass()
  {
    return getAsDouble(MASS);
  }

  public void setMass(double mass)
  {
    this.put(MASS, mass);
  }

  public boolean getIsRoot()
  {
    return getAsBoolean(IS_ROOT);
  }

  public void setIsRoot(boolean is_root)
  {
    this.put(IS_ROOT, is_root);
  }

  public SystemPointType getType()
  {
    return SystemPointType.valueOf(getAsString(TYPE));
  }

  private void setType(SystemPointType type)
  {
    Objects.requireNonNull(type, "type");
    this.put(TYPE, type.toString());
  }

  public ObjectOrientation getObjectOrientation()
  {
    return new ObjectOrientation(ensuredMapGet(ORIENTATION));
  }

  public List<ObjectId> getChildren()
  {
    return ensuredListGet(CHILDREN);
  }
}
