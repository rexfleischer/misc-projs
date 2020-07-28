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
public class PointCloud extends SystemPoint
{
    
    public PointCloud()
    {
        super(SystemPointType.CLOUD);
    }

    public PointCloud(Map data)
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
    
    public byte getHealth()
    {
        Byte result = (Byte) get(HEALTH);
        if (result == null)
        {
            result = 0x0;
            put(HEALTH, result);
        }
        return result;
    }
    
    public void setHealth(byte health)
    {
        put(HEALTH, health);
    }
    
    public MaterialMap getMaterialMap()
    {
        return new MaterialMap(ensuredMapGet(MATERIAL));
    }
}
