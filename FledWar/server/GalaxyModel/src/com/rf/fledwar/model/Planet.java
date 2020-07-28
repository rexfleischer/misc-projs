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
public class Planet extends Orbital
{
    /**
     * the temp of the planet in Celsius
     */
    private static final String TEMP = "temp";
    
    /**
     * the rotation of the planet in rad/hr
     */
    private static final String ROTATION = "rotation";
    
    /**
     * the planet type by string enum: 
     * {@see com.rf.fledwar.gm.space.vto.PlanetType}
     */
    private static final String PLANET_TYPE = "planet_type";
    
    /**
     * the ring around this planet:
     * {@see com.rf.fledwar.gm.space.vto.PlanetRing}
     */
    private static final String PLANET_RING = "planet_ring";
    
    /**
     * the list of moons orbiting this planet
     */
    private static final String PLANET_MOONS = "planet_moons";
    
    public Planet()
    {
        super(OrbitalType.PLANET);
    }
    
    public Planet(Map data)
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

    public PlanetRing getPlanetRing()
    {
        return new PlanetRing(this.ensuredObjectGet(PLANET_RING));
    }
    
    public BasicVTOList<PlanetMoon> getPlanetMoon()
    {
        return new BasicVTOList<>(
                ensuredListGet(PLANET_MOONS), 
                PlanetMoon.class);
    }
    
}
