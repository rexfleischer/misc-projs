/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.update.Action;
import com.rf.fledwar.model.update.ActionException;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.GalaxySystemType;
import com.rf.fledwar.model.Star;
import com.rf.fledwar.model.util.ChanceRole;
import com.rf.fledwar.model.util.OrbitPath;
import com.rf.fledwar.model.util.OrbitStatus;
import java.util.Random;

/**
 *
 * @author REx
 */
public class CreateRandomSystem extends Action
{
    // make a system with no central orbiting mass...
    // this can be large dust clouds or runaway stars
    // or planets...
    public static final int SYSTEM_TYPE_NONE = 0;
    
    public static final int SYSTEM_TYPE_NONE_CHANCE = 10;
    
    // make a system with a central orbiting mass that
    // is too small for fushion.
    public static final int SYSTEM_TYPE_DWARF = 1;
    
    public static final int SYSTEM_TYPE_DWARF_CHANCE = 10;
    
    // a system with a single random orbiting mass
    public static final int SYSTEM_TYPE_SINGLE = 2;
    
    public static final int SYSTEM_TYPE_SINGLE_CHANCE = 40;
    
    // a system with two main oribiting masses.
    // there are two main type that can happen:
    // 1 -  if the two stars are very different
    //      in amount of mass, then the large one will 
    //      be considered primary, and they will be in
    //      locked orbit with each other.
    // 2 -  if they are close to the same amount of mass, 
    //      then there can be two different type of orbits:
    //  a - locked orbit.
    //  b - they will both orbit in two large circles around
    //      around a common orbiting center
    public static final int SYSTEM_TYPE_BINARY = 3;
    
    public static final int SYSTEM_TYPE_BINARY_CHANCE = 60;
    
    
    public static final String SYSTEM = "system";
    
    public static final String CURRENT_YEAR = "currentyear";
    
    public static final String NAME = "name";
    
    public void setCurrentYear(double year)
    {
        putValue(CURRENT_YEAR, year);
    }
    
    public void setSystemName(String name)
    {
        putValue(NAME, name);
    }
    
    public GalaxySystem getSystem()
    {
        return (GalaxySystem) getValue(SYSTEM);
    }

    @Override
    public void exec() throws ActionException
    {
        Random random = new Random();
        double currentyear = (double) getValue(CURRENT_YEAR);
        String name = (String) getValue(NAME);
        if (name == null)
        {
            throw new NullPointerException("name");
        }
        
        GalaxySystem system = new GalaxySystem();
        system.setName(name);
        int type = getRandomSystemType(random);
        switch(type)
        {
            default:
//            case SYSTEM_TYPE_NONE:
//            {
//                buildNullOrbitingSystem(system);
//                break;
//            }
//            case SYSTEM_TYPE_DWARF:
//            {
//                buildDwarfSystem(system);
//                break;
//            }
            case SYSTEM_TYPE_SINGLE:
            {
                buildSingleSystem(random, system, currentyear);
                break;
            }
//            case SYSTEM_TYPE_BINARY:
//            {
//                buildBinarySystem(system);
//                break;
//            }
        }
        
        system.setSystemType(GalaxySystemType.PLAYABLE);
        
        putValue(SYSTEM, system);
    }
    
    private int getRandomSystemType(Random random)
    {
        ChanceRole typerole = new ChanceRole(random);
        typerole.set(SYSTEM_TYPE_NONE_CHANCE, SYSTEM_TYPE_NONE);
        typerole.set(SYSTEM_TYPE_DWARF_CHANCE, SYSTEM_TYPE_DWARF);
        typerole.set(SYSTEM_TYPE_SINGLE_CHANCE, SYSTEM_TYPE_SINGLE);
        typerole.set(SYSTEM_TYPE_BINARY_CHANCE, SYSTEM_TYPE_BINARY);
        return (int) typerole.role();
    }
    
    private void buildNullOrbitingSystem(Random random, GalaxySystem system, double currentyear)
            throws ActionException
    {
        
    }
    
    private void buildDwarfSystem(Random random, GalaxySystem system, double currentyear)
            throws ActionException
    {
        
    }
    
    private void buildSingleSystem(Random random, GalaxySystem system, double currentyear)
            throws ActionException
    {
        // first we need to build the orbit path because its 
        // going to be the same for each orbital
        OrbitPath path = new OrbitPath(1);
        path.setSystem(null); 
        path.setPathPart(0, OrbitPath.PATH_CENTER);
        
        // now we make the only star and set the orbit status
        String starname = String.format("%s-star-1", system.getName());
        Star star = RandomStar.create(random, starname, currentyear);
        OrbitStatus starorbit = star.getOrbitStatus();
        starorbit.setAlpha(0);
        starorbit.setDeltaAlpha(0);
        starorbit.setDistance(0);
        starorbit.setOrbitPath(path);
        system.addOrbital(star);
        
        system.put("orbitals", system.get("orbitals"));
        
        RandomOrbitals.createRandomOrbitals(random, 
                                            system,
                                            path.toFullPath(),
                                            star.getMass(), 
                                            RandomUtils.MIN_ORBITAL_DISTANCE,
                                            RandomUtils.MAX_ORBITAL_DISTANCE);
    }
    
    private void buildBinarySystem(Random random, GalaxySystem system, double currentyear)
            throws ActionException
    {
        
    }
}
