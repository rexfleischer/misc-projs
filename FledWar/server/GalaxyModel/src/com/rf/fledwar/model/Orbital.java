/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class Orbital extends BasicVTO
{
    /**
     * the name of the planet
     */
    public static final String NAME = "name";
    
    /**
     * the mass of the planet in kg
     */
    public static final String MASS = "mass";
    
    /**
     * this represents the type of orbital is it... 
     * {@see com.rf.fledwar.gm.space.vto.OrbitalType}
     */
    public static final String ORBITAL_TYPE = "orbital_type";
    
    /**
     * the orbit status of this planet in relation to the object
     * being orbited:
     * {@see com.rf.fledwar.gm.util.OrbitStatus}
     */
    public static final String ORBIT_STATUS = "orbit_status";
    
    Orbital(Map data)
    {
        super(data);
    }
    
    Orbital(OrbitalType type)
    {
        super();
        this.setOrbitalType(type);
    }

    public final String getName()
    {
        return (String) this.get(NAME);
    }

    public final void setName(String name)
    {
        this.put(NAME, name);
    }

    public final OrbitalType getOrbitalType()
    {
        return OrbitalType.valueOf((String) this.get(ORBITAL_TYPE));
    }

    private void setOrbitalType(OrbitalType type)
    {
        this.put(ORBITAL_TYPE, type.toString());
    }

    public final double getMass()
    {
        return (double) this.get(MASS);
    }

    public final void setMass(double mass)
    {
        SpaceFunctionUtil.validateOrbitalMass(mass);
        this.put(MASS, mass);
    }

    public final OrbitStatus getOrbitStatus()
    {
        return new OrbitStatus(this.ensuredObjectGet(ORBIT_STATUS));
    }
}
