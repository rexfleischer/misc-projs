/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.util;

import com.fledwar.vto.BasicVTO;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class ObjectOrientation extends BasicVTO
{
  public static final String OBJECT_ID = "object_id";
  
  public static final String OBJECT_TYPE = "object_type";
  
  public static final String DALPHA = "dalpha";
  
  public static final String ALPHA = "alpha";
  
  public static final String DISTANCE = "distance";

  public ObjectOrientation()
  {
    base();
  }

  public ObjectOrientation(Map data)
  {
    super(data);
    if (data.isEmpty()) {
      // if there is nothing in the data map, then we
      // assume its a new ObjectOrientation instance
      base();
    }
  }
  
  private void base() 
  {
    setAlphaDistance(0.0, 0.0);
    setDeltaAlpha(0.0);
    setOrientationIdType(null, null);
  }

  public void setAlphaDistance(Double alpha, Double distance)
  {
    this.put(DISTANCE, distance);
    this.put(ALPHA, alpha);
  }
  
  public void setDeltaAlpha(Double dalpha)
  {
    this.put(DALPHA, dalpha);
  }
  
  public void setXY(double x, double y) 
  {
    setAlphaDistance(Math.atan2(y, x), Math.sqrt(x*x + y*y));
  }
  
  public void setOrientationIdType(ObjectId id, String type)
  {
    this.put(OBJECT_ID, id);
    this.put(OBJECT_TYPE, type);
  }
  
  public void setObjectOrientation(ObjectOrientation orientation) 
  {
    this.put(DISTANCE, orientation.getDistance());
    this.put(ALPHA, orientation.getAlpha());
    this.put(DALPHA, orientation.getDeltaAlpha());
    this.put(OBJECT_ID, orientation.getOrientationId());
    this.put(OBJECT_TYPE, orientation.getOrientationType());
  }
  
  public void setObjectOrientation(double distance,
                                   double alpha,
                                   double dalpha,
                                   ObjectId id,
                                   String type) 
  {
    this.put(DISTANCE, distance);
    this.put(ALPHA, alpha);
    this.put(DALPHA, dalpha);
    this.put(OBJECT_ID, id);
    this.put(OBJECT_TYPE, type);
  }
  
  public ObjectId getOrientationId()
  {
    return getAsObjectId(OBJECT_ID);
  }
  
  public String getOrientationType()
  {
    return getAsString(OBJECT_TYPE);
  }
  
  public Double getDeltaAlpha()
  {
    return getAsDouble(DALPHA);
  }

  public Double getAlpha()
  {
    return getAsDouble(ALPHA);
  }

  public Double getDistance()
  {
    return getAsDouble(DISTANCE);
  }

  public Double getPosX()
  {
    return getDistance() * Math.cos(getAlpha());
  }

  public Double getPosY()
  {
    return getDistance() * Math.sin(getAlpha());
  }

  public void updateElapseTime(double gamehours)
  {
    double alpha = getAlpha();
    alpha += (getDeltaAlpha() * gamehours);
    alpha %= (2 * Math.PI);

    if (alpha < 0.0) {
      alpha += (2 * Math.PI);
    }

    setAlphaDistance(alpha, getDistance());
  }
}
