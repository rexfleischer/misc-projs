/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.AstroidBelt;
import com.rf.fledwar.model.AstroidBeltType;
import com.rf.fledwar.model.util.SpaceConstents;
import java.util.Random;

/**
 *
 * @author REx
 */
public class RandomAstroidBelt
{
    public static final double MIN_RANDOM_MASS = SpaceConstents.EARTH_MASS * 0.01;
    
    public static final double MAX_RANDOM_MASS = SpaceConstents.EARTH_MASS;
    
    public static double randomMass(Random random)
    {
        return RandomUtils.getRandom(random, MIN_RANDOM_MASS, MAX_RANDOM_MASS);
    }
    
    public static AstroidBelt create(Random random, String name)
    {
        AstroidBelt result = new AstroidBelt();
        result.setName(name);
        result.setAstroidBeltType(AstroidBeltType.roleType());
        result.setMass(randomMass(random));
        return result;
    }
}
