/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.create;

import com.rf.fledwar.model.Star;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.Random;

/**
 *
 * @author REx
 */
public class RandomStar
{
    /**
     * creates a random star assuming a main sequence computations.
     * @param random
     * @param minMass
     * @param maxMass
     * @param currentyear
     * @return 
     */
    public static Star create(Random random,
                              String name,
                              double minMass, 
                              double maxMass,
                              double currentyear)
    {
        Star result = new Star();
        
        /**
         * holy crap... dont forget to set the name of every
         * object else mongodb gives you a very useless 
         * exception and you will be tracking it down for 
         * a very very long time.
         */
        result.setName(name);
        
        /**
         * we first need to figure out the mass of the star
         * because we bass most of our computations on
         * that... 
         */
        double mass = (random.nextDouble() * (maxMass - minMass)) + minMass;
        result.setMass(mass);
        
        /**
         * now we get the life expectancy of the star from the 
         * mass. it is common knowledge that the larger the 
         * star the faster it uses its possible energy. so,
         * we place as being born some time back from the 
         * current year from some random amount of its life
         * expectancy
         */
        long birth = (long)(currentyear - 
                            (SpaceFunctionUtil.starMassToLifeExpectancy(mass) 
                             * random.nextDouble()));
        result.setBirthDate(birth);
        
        /**
         * radius from mass
         */
        result.setRadius(SpaceFunctionUtil.starMassToRadius(mass));
        
        /**
         * temp from mass. generally, the larger the star the more it
         * outputs energy. but i've found with more research that
         * its fairly random... the starMassToTemperature() method
         * generally returns the max temp of stars greater than
         * on solar mass and the min of stars less than one solar mass.
         * 
         * so, for now, we just change the temperature randomly..
         */
        result.setTemp((random.nextDouble() * 0.5 + 0.5) * 
                       SpaceFunctionUtil.starMassToTemperature(mass));
        
        /**
         * @TODO: figure this out...
         */
        result.setRotation(1);
        
        return result;
    }
    
    public static Star create(Random random,
                              String name,
                              double currentyear)
    {
        return create(random, 
                      name,
                      SpaceConstents.MASS_OF_SOLAR_FUSION,
                      SpaceConstents.MASS_OF_MAX_STAR, 
                      currentyear);
    }
}
