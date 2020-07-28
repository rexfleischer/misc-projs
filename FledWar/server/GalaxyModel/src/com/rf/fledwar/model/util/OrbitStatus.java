/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.util;

import com.rf.fledwar.model.BasicVTO;
import java.util.Map;

/**
 *
 * @author REx
 */
public final class OrbitStatus extends BasicVTO
{
    /**
     * the distance from the center of the object being orbited
     */
    public static final String DISTANCE = "distance";
    
    /**
     * the radius the orbiting object is at to the perspective of the
     * object being orbited
     */
    public static final String ALPHA = "alpha";
    
    /**
     * the delta of alpha in rad/hr
     */
    public static final String DELTA_ALPHA = "delta_alpha";
    
    /**
     * the path to the object this status is centered of
     */
    public static final String ORBIT_PATH = "orbit_path";

    public OrbitStatus(double distance, 
                       double alpha, 
                       double delta_alpha,
                       OrbitPath path)
    {
        super();
        this.setDistance(distance);
        this.setAlpha(alpha);
        this.setDeltaAlpha(delta_alpha);
        this.setOrbitPath(path);
    }
    
    public OrbitStatus(Map data)
    {
        super(data);
    }
    
    public double getDistance()
    {
        return (double) this.get(DISTANCE);
    }
    
    public double getAlpha()
    {
        return (double) this.get(ALPHA);
    }
    
    public double getDeltaAlpha()
    {
        return (double) this.get(DELTA_ALPHA);
    }
    
    public OrbitPath getOrbitPath()
    {
        String rawpath = (String) this.get(ORBIT_PATH);
        return (rawpath != null) ? new OrbitPath(rawpath) : null;
    }
    
    public double getCartesianX()
    {
        return getDistance() * Math.cos(getAlpha());
    }
    
    public double getCartesianY()
    {
        return getDistance() * Math.sin(getAlpha());
    }
    
    public void setDistance(double value)
    {
        SpaceFunctionUtil.validateOrbitalDistance(value);
        this.put(DISTANCE, value);
    }
    
    public void setAlpha(double value)
    {
        this.put(ALPHA, value);
    }
    
    public void setDeltaAlpha(double value)
    {
        this.put(DELTA_ALPHA, value);
    }
    
    public void setOrbitPath(OrbitPath path)
    {
        this.put(ORBIT_PATH, path.toFullPath());
    }
    
    public void updateElapseTime(double gameHours)
    {
        double alpha = getAlpha();
        alpha += (getDeltaAlpha() * gameHours);
        alpha %= (2 * Math.PI);
        
        setAlpha(alpha);
    }

    @Override
    public String toString()
    {
        String distance = (get(DISTANCE) != null) ? get(DISTANCE).toString() : "?";
        String alpha = (get(ALPHA) != null) ? get(ALPHA).toString() : "?";
        String deltaalpha = (get(DELTA_ALPHA) != null) ? get(DELTA_ALPHA).toString() : "?";
        String orbitpath = (get(ORBIT_PATH) != null) ? get(ORBIT_PATH).toString() : "?";
        
        return String.format("OrbitStatus{distance:%s, alpha:%s, deltaalpha:%s, orbitpath:%s}",
                             distance, alpha, deltaalpha, orbitpath);
    }
    
    
}
