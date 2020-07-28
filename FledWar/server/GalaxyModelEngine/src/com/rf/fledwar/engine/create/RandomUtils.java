/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.Random;

/**
 *
 * @author REx
 */
public class RandomUtils
{
    public static double MIN_ORBITAL_DISTANCE = SpaceConstents.AU * 0.01;
    
    public static double MAX_ORBITAL_DISTANCE = SpaceConstents.AU * 50.0;
    
    public static void setOrbitStatus(OrbitStatus status, 
                                      Random random,
                                      double orbitingmass,
                                      double orbitermass,
                                      double distance,
                                      String orbitpath)
    {
        // start in a random place
        status.setAlpha(random.nextDouble() * (2 * Math.PI));
        
        // put together how fast the orbiter is moving
        double period = SpaceFunctionUtil.peroidOfOrbit(orbitingmass,
                                                        orbitermass, 
                                                        distance);
        // being that delta_alpha is the period of the orbit, we need
        // to know how far it moves in one hour, therefore we do
        // one over delta_alpha
        status.setDeltaAlpha(1 / period);
        
        // distance
        status.setDistance(distance);
        
        // and finally the path
        status.setOrbitPath(new OrbitPath(orbitpath));
    }
    
    public static double getOrbitalDistance(Random random, 
                                            double mindist, 
                                            double maxdist,
                                            int orbitalcount,
                                            int iter)
    {
        double range = (maxdist - mindist) / orbitalcount;
        double root = range * (1 + iter) + mindist;
        return root + (random.nextDouble() - 0.5) * (range * 0.5);
    }
    
    public static double getRandom(Random random, double min, double max)
    {
        return (random.nextDouble() * (max - min)) + min;
    }
}
