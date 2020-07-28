/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.scope;

import com.fledwar.vto.BaseVTO;
import com.fledwar.vto.galaxy.util.GalaxyOrientation;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import java.util.Map;

/**
 *
 * @author REx
 */
public class GalaxyScope extends BaseVTO
{
  public static final String SCOPE_TYPE = "scope_type";
  
  public static final String LAST_SYSTEM_UPDATE = "last_system_update";
  
  public static final String LAST_UNIT_UPDATE = "last_unit_update";
    
  public static final String ORBITING = "orbiting";

  public static final String ORIENTATION = "orientation";

  public static final String ORIENTATION_POS_X =
          String.format("%s.%s", GalaxyScope.ORIENTATION,
                                 GalaxyOrientation.POS_X);
  
  public static final String ORIENTATION_POS_Y =
          String.format("%s.%s", GalaxyScope.ORIENTATION,
                                 GalaxyOrientation.POS_Y);

  public static final String ORIENTATION_ALPHA =
          String.format("%s.%s", GalaxyScope.ORIENTATION,
                                 GalaxyOrientation.ALPHA);
  
  public static final String ORIENTATION_DISTANCE =
          String.format("%s.%s", GalaxyScope.ORIENTATION,
                                 GalaxyOrientation.DISTANCE);
  
  public static final String ORBITING_ALPHA = 
          String.format("%s.%s", GalaxyScope.ORBITING,
                                 ObjectOrientation.ALPHA);
  
  public static final String ORBITING_DISTANCE = 
          String.format("%s.%s", GalaxyScope.ORBITING,
                                 ObjectOrientation.DISTANCE);
  
  
  public GalaxyScope()
  {
    super();
    triggerLastSystemUpdate();
    triggerLastUnitUpdate();
    setScopeType(GalaxyScopeType.NONE);
    getObjectOrientation();
    getGalaxyOrientation();
  }
  
  public GalaxyScope(Map data)
  {
    super(data);
  }
  
  public long getLastSystemUpdate()
  {
    return getAsLong(LAST_SYSTEM_UPDATE);
  }
  
  public void triggerLastSystemUpdate()
  {
    put(LAST_SYSTEM_UPDATE, System.currentTimeMillis());
  }
  
  public long getLastUnitUpdate()
  {
    return getAsLong(LAST_UNIT_UPDATE);
  }
  
  public void triggerLastUnitUpdate()
  {
    put(LAST_UNIT_UPDATE, System.currentTimeMillis());
  }
  
  public GalaxyScopeType getScopeType()
  {
    return GalaxyScopeType.valueOf(getAsString(SCOPE_TYPE));
  }
  
  public void setScopeType(GalaxyScopeType type)
  {
    put(SCOPE_TYPE, type.name());
  }
  
  public ObjectOrientation getObjectOrientation()
  {
    return new ObjectOrientation(ensuredMapGet(ORBITING));
  }
  
  public GalaxyOrientation getGalaxyOrientation()
  {
    return new GalaxyOrientation(ensuredMapGet(ORIENTATION));
  }
}
