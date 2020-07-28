/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.update.ActionException;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.Orbital;
import com.rf.fledwar.model.util.ChanceRole;
import com.rf.fledwar.model.util.OrbitStatus;
import java.util.Random;

/**
 *
 * @author REx
 */
public class RandomOrbitals
{
    public static final int ORBITALS_MAX_RANDOM = 50;
    
    public static final int ORBITALS_MIN_RANDOM = 5;
    
    public static final int ORBITALS_ASTROID_BELT = 0;
    
    public static final int ORBITALS_ASTROID_BELT_CHANCE = 30;
    
    public static final String ORBITALS_ASTROID_BELT_NAME = "astroid";
    
    public static final int ORBITALS_DUST_CLOUD = 1;
    
    public static final int ORBITALS_DUST_CLOUD_CHANCE = 25;
    
    public static final String ORBITALS_DUST_CLOUD_NAME = "dustcloud";
    
    public static final int ORBITALS_PLANET = 2;
    
    public static final int ORBITALS_PLANET_CHANCE = 70;
    
    public static final String ORBITALS_PLANET_NAME = "planet";
    
    public static final String NAME_FORMAT = "%s-%%s-%%s";
    
    private static ChanceRole getRandomOrbitalType(Random random)
    {
        ChanceRole typerole = new ChanceRole(random);
        typerole.set(ORBITALS_ASTROID_BELT_CHANCE, ORBITALS_ASTROID_BELT);
        typerole.set(ORBITALS_DUST_CLOUD_CHANCE, ORBITALS_DUST_CLOUD);
        typerole.set(ORBITALS_PLANET_CHANCE, ORBITALS_PLANET);
        return typerole;
    }
    
    public static void createRandomOrbitals(Random random,
                                            GalaxySystem system,
                                            String orbitingpath,
                                            double orbitingmass,
                                            double mindist,
                                            double maxdist)
            throws ActionException
    {
        ChanceRole orbitaltyperoler = getRandomOrbitalType(random);
        int orbitalcount = random.nextInt(ORBITALS_MAX_RANDOM - 
                                          ORBITALS_MIN_RANDOM) + 
                               ORBITALS_MIN_RANDOM;
        
        String formatname = String.format(NAME_FORMAT, system.getName());
        
        int astroidBeltCount = 1;
        int dustCloudCount = 1;
        int planetCount = 1;
        
        for(int i = 0; i < orbitalcount; i++)
        {
            int orbialtype = (int) orbitaltyperoler.role();
            double distance = RandomUtils.getOrbitalDistance(random,
                                                             mindist,
                                                             maxdist,
                                                             orbitalcount,
                                                             i);
            
            Orbital orbital = null;
            switch(orbialtype)
            {
                case ORBITALS_ASTROID_BELT:
                {
                    String name = String.format(formatname, 
                                                ORBITALS_ASTROID_BELT_NAME,
                                                astroidBeltCount++);
                    orbital = RandomAstroidBelt.create(random, name);
                    break;
                }
                case ORBITALS_DUST_CLOUD:
                {
                    String name = String.format(formatname, 
                                                ORBITALS_DUST_CLOUD_NAME,
                                                dustCloudCount++);
                    orbital = RandomDustCloud.create(random, name);
                    break;
                }
                case ORBITALS_PLANET:
                {
                    String name = String.format(formatname, 
                                                ORBITALS_PLANET_NAME,
                                                planetCount++);
                    orbital = RandomPlanet.create(random, name);
                    break;
                }
                default:
                    throw new ActionException(
                            String.format("screwup on the random orbital "
                            + "roler [returned: %s]", orbialtype));
            }
            
            OrbitStatus thisstatus = orbital.getOrbitStatus();
            RandomUtils.setOrbitStatus(thisstatus,
                                       random,
                                       orbitingmass,
                                       orbital.getMass(), 
                                       distance, 
                                       orbitingpath);
            system.addOrbital(orbital);
        }
    }
}
