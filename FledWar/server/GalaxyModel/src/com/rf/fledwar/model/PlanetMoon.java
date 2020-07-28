/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import java.util.Map;

/**
 *
 * @author REx
 */
public class PlanetMoon extends Orbital
{
    
    private static final String TEMP = "temp";
    
    private static final String ROTATION = "rotation";
    
    private static final String PLANET_TYPE = "planet_type";
    
    public PlanetMoon()
    {
        super(OrbitalType.PLANET_MOON);
    }
    
    public PlanetMoon(Map data)
    {
        super(data);
    }

    public double getTemp()
    {
        return (double) this.get(TEMP);
    }

    public void setTemp(double temp)
    {
        this.put(TEMP, temp);
    }

    public double getRotation()
    {
        return (double) this.get(ROTATION);
    }

    public void setRotation(double rotation)
    {
        this.put(ROTATION, rotation);
    }

    public PlanetType getPlanetType()
    {
        String type = (String) this.get(PLANET_TYPE);
        return PlanetType.valueOf(type);
    }

    public void setPlanetType(PlanetType planetType)
    {
        this.put(PLANET_TYPE, planetType.name());
    }
    
}
