/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model.util;

/**
 *
 * @author REx
 */
public class SpaceFunctionUtil
{
    
    /**
     * finds the distance from *m1* where the center of mass is
     * between two objects orbiting each other.
     * 
     * the equation is derived from the ratio m1 / m2 = a2 / a1....
     * and being that a1 + a2 = distance, we can derive a1 or a2 from
     * there.
     * @param m1 kg 
     * @param m2 kg
     * @param distance meters
     * @return meters
     */
    public static double centerOfMassBetweenTwoObjects(double m1, 
                                                       double m2, 
                                                       double distance)
    {
        // instead of distance / ((m1 / m2) + 1)
        return (distance * m2) / (m1 + m2);
    }
    
    /**
     * 
     * @param alpha1
     * @param radius1
     * @param alpha2
     * @param radius2
     * @return 
     */
    public static double distanceBetweenTwoObjects(double alpha1, 
                                                   double radius1, 
                                                   double alpha2, 
                                                   double radius2)
    {
        return Math.sqrt(Math.pow(radius1, 2) + 
                         Math.pow(radius2, 2) - 
                         2 * radius1 * radius2 * Math.cos(alpha1 - alpha2));
    }
    
    /**
     * 
     * @param alpha1
     * @param radius1
     * @param alpha2
     * @param radius2
     * @return 
     */
    public static double alphaBetweenTwoObjects(double alpha1, 
                                                double radius1, 
                                                double alpha2, 
                                                double radius2)
    {
        double dx = radius1 * Math.cos(alpha1) - radius2 * Math.cos(alpha2);
        double dy = radius1 * Math.sin(alpha1) - radius2 * Math.sin(alpha2);
        
        return Math.atan2(dy, dx);
    }
    
    /**
     * 
     * @param m1 kg
     * @param distance meters
     * @return joule
     */
    public static double gpOfObject(double m1, double distance)
    {
        return gpBetweenTwoObjects(m1, 1.0, distance);
    }
    
    /**
     * 
     * @param m1 kg
     * @param m2 kg
     * @param distance meters
     * @return joule
     */
    public static double gpBetweenTwoObjects(double m1, double m2, double distance)
    {
        return (SpaceConstents.GRAVITATIONAL_CONSTANT * m1 * m2) / (distance * distance);
    }
    
    /**
     * 
     * @param massOfCOM kg
     * @param hieghtFromCOM meters
     * @return m/s
     */
    public static double circularOrbitSpeedRequirement(
            double m1,
            double m2,
            double distance)
    {
        return Math.sqrt(SpaceConstents.GRAVITATIONAL_CONSTANT * (m1 + m2) / distance);
    }
    
    /**
     * 
     * @param m1 kg
     * @param m2 kg
     * @param distance meters
     * @return hours
     */
    public static double peroidOfOrbit(
            double m1,
            double m2,
            double distance)
    {
//        return 2 * Math.PI * distance / circularOrbitSpeedRequirement(m1, m2, distance);
        
        /**
         * 0.5555555555555... = (2 / 3.6)...
         * the 3.6 comes from 3600 in one hour
         */
        return 0.0005555555555555 * Math.PI * 
               Math.sqrt(
                    Math.pow(distance, 3) / 
                    (SpaceConstents.GRAVITATIONAL_CONSTANT * (m1 + m2))
               );
    }
    
    /**
     * 
     * @param mass kg
     * @return meters
     */
    public static double starMassToRadius(double mass)
    {
        return Math.pow(mass / SpaceConstents.SOLAR_MASS, 0.65) 
               * SpaceConstents.SOLAR_RADIUS;
    }
    
    /**
     * this is assuming a very loose ratio based on the 
     * sun and its temp... it should include some type of
     * random component to make more realistic.
     * @param mass kg
     * @return kelvin
     */
    public static double starMassToTemperature(double mass)
    {
        return SpaceConstents.SOLAR_TEMPERATURE * 
               Math.pow(mass / SpaceConstents.SOLAR_MASS, 0.625);
    }
    
    /**
     * 
     * @param mass
     * @return 
     */
    public static double starMassToLifeExpectancy(double mass)
    {
        return SpaceConstents.SOLAR_LIFE_EXPECTANCY / 
               Math.pow(mass / SpaceConstents.SOLAR_MASS, 3.0);
    }
    
    /**
     * 
     * @param mass kg
     */
    public static void validateOrbitalMass(double mass)
    {
        if (Double.isInfinite(mass) || Double.isNaN(mass) || mass < 1.0)
        {
            throw new IllegalArgumentException(
                    String.format("invalid value for orbital mass [%s]", mass));
        }
    }
    
    /**
     * 
     * @param distance meters
     */
    public static void validateOrbitalDistance(double distance)
    {
        if (Double.isInfinite(distance) || Double.isNaN(distance) || distance < 0)
        {
            throw new IllegalArgumentException(
                    String.format("invalid value for orbital distance [%s]", distance));
        }
    }
    
    /**
     * this will tell you how many game hours has passed 
     * from the amount of real time milli seconds has passed.
     * @param elapseMS system milli sec
     * @return hours
     */
    public static double systemMSToGameHours(long elapseMS)
    {
        return (elapseMS / SpaceConstents.GAME_HOUR);
    }
    
    /**
     * this is the current game year
     * @return 
     */
    public static double currentGameYear()
    {
        return (System.currentTimeMillis() / SpaceConstents.GAME_YEAR) + 
               SpaceConstents.GAME_YEAR_BEGINNING;
    }
    
    /**
     * transforms a radian to make sure it is 0 <= result <= (Math.PI * 2)
     * @param radian
     * @return 
     */
    public static double nomalizeRadianForDB(double radian)
    {
        radian %= (Math.PI * 2);
        if (radian < 0.0)
        {
            radian += (Math.PI * 2);
        }
        return radian;
    }
    
    public static void applyAlphaDistanceFromAddingStatus(OrbitStatus os1, 
                                                          OrbitStatus os2, 
                                                          OrbitStatus applying)
    {
        
    }
    
    /**
     * this helper function can be used to apply orbit updates.
     * 
     * a few assumptions that this makes is that original and
     * applying have the same delta_alpha and orbit path
     * @param center
     * @param orbiting
     * @param original
     * @param applying 
     */
    public static void applyOrientOrbit(OrbitStatus center, 
                                        OrbitStatus orbiting, 
                                        OrbitStatus original,
                                        OrbitStatus applying)
    {
        double center_orbiting_distance;
        double center_orbiting_alpha;
        
        // we know that a lot of the usage for this method
        // will be with the center as 0 distance and 0 alpha,
        // that means we dont actually need to compute it
        // {@see com.rf.fledwar.model.util.SpaceConstents.GALAXY_CENTER}
        if (center.getAlpha() == 0 && center.getDistance() == 0)
        {
            center_orbiting_distance = orbiting.getDistance();
            center_orbiting_alpha = orbiting.getAlpha();
        }
        else
        {
            center_orbiting_distance = distanceBetweenTwoObjects(
                    center.getAlpha(), 
                    center.getDistance(), 
                    orbiting.getAlpha(), 
                    orbiting.getDistance());
            center_orbiting_alpha = alphaBetweenTwoObjects(
                    center.getAlpha(), 
                    center.getDistance(), 
                    orbiting.getAlpha(), 
                    orbiting.getDistance());
        }
        
        double center_orbiting_x = (center_orbiting_distance * Math.cos(center_orbiting_alpha));
        double center_orbiting_y = (center_orbiting_distance * Math.sin(center_orbiting_alpha));
        double original_x = original.getDistance() * Math.cos(original.getAlpha());
        double original_y = original.getDistance() * Math.sin(original.getAlpha());
        
        double delta_x = (center_orbiting_x - original_x);
        double delta_y = (center_orbiting_y - original_y);
        
        double distance = Math.sqrt(Math.pow(delta_x, 2) + Math.pow(delta_y, 2));
        double alpha = Math.atan2(delta_y, delta_x);
        alpha %= (2 * Math.PI);
        if (alpha < 0.0)
        {
            alpha += (2 * Math.PI);
        }
        
        applying.setAlpha(alpha);
        applying.setDistance(distance);
    }
}
