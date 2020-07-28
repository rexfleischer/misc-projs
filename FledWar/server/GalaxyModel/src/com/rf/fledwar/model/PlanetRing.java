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
public class PlanetRing extends BasicVTO
{
    /**
     * this is the min radius of the ring... basically the distance
     * away from the planets center where the ring starts.
     */
    private static final String START_RADIUS = "start_radius";
    
    /**
     * this is the max radius of the ring... as before, the distance
     * away from the planets center.
     */
    private static final String END_RADIUS = "end_radius";
    
    /**
     * the type of ring it is... 
     */
    private static final String RING_TYPE = "type";
    
    /**
     * the rotation of the ring in rad/s
     */
    private static final String ROTATION = "rotation";

    public PlanetRing(Map data)
    {
        super(data);
    }
    
    public double getRotation()
    {
        return (double) this.get(ROTATION);
    }

    public void setRotation(double rotation)
    {
        this.put(ROTATION, rotation);
    }
    
    public double getStartRadius()
    {
        return (double) this.get(START_RADIUS);
    }

    public void setStartRadius(double rotation)
    {
        this.put(START_RADIUS, rotation);
    }
    
    public double getEndRadius()
    {
        return (double) this.get(END_RADIUS);
    }

    public void setEndRadius(double rotation)
    {
        this.put(END_RADIUS, rotation);
    }
    
    public PlanetRingType getPlanetRingType()
    {
        String type = (String) this.get(RING_TYPE);
        return PlanetRingType.valueOf(type);
    }

    public void setPlanetRingType(PlanetRingType planetRingType)
    {
        this.put(RING_TYPE, planetRingType.name());
    }
    
}
