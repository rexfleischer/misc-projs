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
public class Star extends Orbital
{
    /**
     * the average radius of the cloud in km
     */
    private static final String RADIUS = "radius";
    
    /**
     * the temp of the planet in Celsius
     */
    private static final String TEMP = "temp";
    
    /**
     * the rotation of the planet in rad/hr
     */
    private static final String ROTATION = "rotation";
    
    /**
     * the birth of the star since "the beginning"
     */
    private static final String BIRTH_DATE = "birth_date";
    
    
    public Star()
    {
        super(OrbitalType.STAR);
    }
    
    public Star(Map data)
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

    public double getBirthDate()
    {
        return (double) this.get(BIRTH_DATE);
    }

    public void setBirthDate(double birthdate)
    {
        this.put(BIRTH_DATE, birthdate);
    }
}
