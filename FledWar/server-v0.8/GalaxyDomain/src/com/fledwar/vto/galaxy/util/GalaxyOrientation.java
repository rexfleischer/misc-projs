/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.util;

import com.fledwar.vto.BasicVTO;
import java.util.Map;

/**
 *
 * @author REx
 */
public class GalaxyOrientation extends BasicVTO
{
  public static final String ALPHA = "alpha";
  
  public static final String DISTANCE = "distance";
  
  public static final String POS_X = "posx";
  
  public static final String POS_Y = "posy";

  public GalaxyOrientation()
  {
    setAlphaDistance(0.0, 0.0);
  }
  
  public GalaxyOrientation(double alpha, double distance)
  {
    setAlphaDistance(alpha, distance);
  }

  public GalaxyOrientation(Map data)
  {
    super(data);
    if (data.isEmpty()) {
      // if there is nothing in the data map, then we
      // assume its a new GalaxyOrientation instance
      setAlphaDistance(0.0, 0.0);
    }
  }

  public Double getAlpha()
  {
    return this.getAsDouble(ALPHA);
  }

  public Double getDistance()
  {
    return this.getAsDouble(DISTANCE);
  }

  public Double getPosX()
  {
    return this.getAsDouble(POS_X);
  }

  public Double getPosY()
  {
    return this.getAsDouble(POS_Y);
  }

  public void setAlphaDistance(Double alpha, Double distance)
  {
    double pos_x = (distance * Math.cos(alpha));
    double pos_y = (distance * Math.sin(alpha));
    
    this.put(DISTANCE, distance);
    this.put(ALPHA, alpha);
    this.put(POS_X, pos_x);
    this.put(POS_Y, pos_y);
  }
  
  public void setAlphaDistance(GalaxyOrientation orientation) 
  {
    this.put(DISTANCE, orientation.getDistance());
    this.put(ALPHA, orientation.getAlpha());
    this.put(POS_X, orientation.getPosX());
    this.put(POS_Y, orientation.getPosY());
  }
  
  public void setAlphaDistance(GalaxyOrientation orbiting, 
                               ObjectOrientation relation)
  {
    double new_x = (orbiting.getPosX() + relation.getPosX());
    double new_y = (orbiting.getPosY() + relation.getPosY());

    double new_dist = Math.sqrt(new_x * new_x + new_y * new_y);
    double new_alpha = SpaceFunctionUtil.nomalizeRadianForDB(
            Math.atan2(new_y, new_x));
    
    setAlphaDistance(new_alpha, new_dist);
  }
  
  public GalaxyOrientation addObjectOrientation(ObjectOrientation object)
  {
    double new_x = (this.getPosX() + object.getPosX());
    double new_y = (this.getPosY() + object.getPosY());

    double new_dist = Math.sqrt(new_x * new_x + new_y * new_y);
    double new_alpha = SpaceFunctionUtil.nomalizeRadianForDB(
            Math.atan2(new_y, new_x));
    
    GalaxyOrientation result = new GalaxyOrientation();
    result.setAlphaDistance(new_alpha, new_dist);
    return result;
  }
  
  public double distanceFrom(GalaxyOrientation other)
  {
    double dx = (this.getPosX() - other.getPosX());
    double dy = (this.getPosY() - other.getPosY());
    
    return Math.sqrt(dx*dx + dy*dy);
  }

  @Override
  public BasicVTO copy()
  {
    return new GalaxyOrientation(getDataAsMap());
  }
}
