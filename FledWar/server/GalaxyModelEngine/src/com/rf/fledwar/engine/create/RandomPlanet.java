/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.Planet;
import com.rf.fledwar.model.PlanetMoon;
import com.rf.fledwar.model.PlanetType;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.Random;

/**
 *
 * @author REx
 */
public class RandomPlanet
{
    
    
    public static Planet create(Random random, String name)
    {
        Planet result = new Planet();
        result.setName(name);
        result.setPlanetType(PlanetType.roleType());
        
        /**
         * TODO: figure this out
         */
        result.setRotation(1.0);
        result.setTemp(500.0);
        result.setMass(SpaceConstents.EARTH_MASS);
        
        return result;
    }
    
    public static PlanetMoon createMoon(Random random, String name)
    {
        return null;
    }
}
