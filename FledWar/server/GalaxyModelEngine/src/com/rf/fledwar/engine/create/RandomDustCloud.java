/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.DustCloud;
import com.rf.fledwar.model.DustCloudType;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.Random;

/**
 *
 * @author REx
 */
public class RandomDustCloud
{
    public static final double MIN_RANDOM_MASS = SpaceConstents.EARTH_MASS * 0.1;
    
    public static final double MAX_RANDOM_MASS = SpaceConstents.EARTH_MASS;
    
    public static double randomMass(Random random)
    {
        return RandomUtils.getRandom(random, MIN_RANDOM_MASS, MAX_RANDOM_MASS);
    }
    
    public static double radiusFromMass(Random random, double mass)
    {
        double medium = (SpaceConstents.EARTH_RADIUS / SpaceConstents.EARTH_MASS) * mass;
        return RandomUtils.getRandom(random, medium * 0.001, mass * 1000);
    }
    
    public static DustCloud create(Random random, String name)
    {
        DustCloud result = new DustCloud();
        result.setName(name);
        result.setDustCloudType(DustCloudType.roleType());
        double mass = randomMass(random);
        result.setMass(mass);
        result.setRadius(radiusFromMass(random, mass));
        return result;
    }
}
