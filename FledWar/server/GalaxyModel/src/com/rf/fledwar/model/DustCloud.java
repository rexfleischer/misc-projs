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
public class DustCloud extends Orbital
{
    /**
     * the average radius of the cloud in km
     */
    private static final String RADIUS = "radius";
    
    /**
     * the type of cloud:
     * {@see com.rf.fledwar.gm.space.vto.DustCloudType}
     */
    private static final String CLOUD_TYPE = "cloud_type";
    
    
    public DustCloud()
    {
        super(OrbitalType.DUST_CLOUD);
    }
    
    public DustCloud(Map data)
    {
        super(data);
    }

    public double getRadius()
    {
        return (double) this.get(RADIUS);
    }

    public void setRadius(double radius)
    {
        this.put(RADIUS, radius);
    }

    public DustCloudType getDustCloudType()
    {
        String type = (String) this.get(CLOUD_TYPE);
        return DustCloudType.valueOf(type);
    }

    public void setDustCloudType(DustCloudType dustCloudType)
    {
        this.put(CLOUD_TYPE, dustCloudType.name());
    }
}
