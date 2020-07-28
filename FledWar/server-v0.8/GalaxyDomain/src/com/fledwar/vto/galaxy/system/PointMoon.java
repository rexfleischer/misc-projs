/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.system;

import java.util.Map;
import java.util.Objects;

/**
 *
 * @author REx
 */
public class PointMoon extends SystemPoint
{

    public PointMoon()
    {
        super(SystemPointType.MOON);
    }

    public PointMoon(Map data)
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
    
    public PlanetMoonType getPlanetMoonType()
    {
        return PlanetMoonType.valueOf(getAsString(MOON_TYPE));
    }
    
    public void setPlanetMoonType(PlanetMoonType type)
    {
        Objects.requireNonNull(type, "type");
        this.put(MOON_TYPE, type.toString());
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
