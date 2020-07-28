/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.system;

import java.util.Map;

/**
 *
 * @author REx
 */
public class PointStar extends SystemPoint
{

    public PointStar()
    {
        super(SystemPointType.STAR);
    }

    public PointStar(Map data)
    {
        super(data);
    }
    
    public Double getRadius()
    {
        return getAsDouble(RADIUS);
    }
    
    public void setRadius(double radius)
    {
        this.put(RADIUS, radius);
    }
    
    public Double getLuminosity()
    {
        return getAsDouble(LUMINOSITY);
    }
    
    public void setLuminosity(double luminosity)
    {
        this.put(LUMINOSITY, luminosity);
    }
    
}
