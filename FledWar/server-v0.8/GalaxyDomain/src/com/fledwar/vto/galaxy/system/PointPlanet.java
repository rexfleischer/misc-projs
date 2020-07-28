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
public class PointPlanet extends SystemPoint
{

    public PointPlanet()
    {
        super(SystemPointType.PLANET);
    }

    public PointPlanet(Map data)
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
    
    public PlanetType getPlanetType()
    {
        return PlanetType.valueOf(getAsString(PLANET_TYPE));
    }
    
    public void setPlanetType(PlanetType type)
    {
        Objects.requireNonNull(type, "type");
        this.put(PLANET_TYPE, type.toString());
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
    
    public PlanetRing getPlanetRing()
    {
        return new PlanetRing(ensuredMapGet(PLANET_RING));
    }
    
    public MaterialMap getMaterialMap()
    {
        return new MaterialMap(ensuredMapGet(MATERIAL));
    }
}
