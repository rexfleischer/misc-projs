/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.util;

/**
 *
 * @author REx
 */
public class SpaceConstents
{
    /**
     * sense we're doing everything in km, we need to put this constant
     * in terms of N(km/kg)^2 instead of the standard N(m/kg)^2
     */
    public static final double GRAVITATIONAL_CONSTANT = 6.68384E-11;
    
    /**
     * this is the angular speed of the center of solar systems
     * that are rotating around the center of the galaxy. in reality,
     * the more outer the star, the larger the angular velocity is. 
     * but this is good enough for now. 
     */
    public static final double GALAXY_ANGULAR_SPEED = 4.32E-12;
    
    /**
     * the amount of kg in the earth... this is mostly used for
     * analyzing other planets and such
     */
    public static final double EARTH_MASS = 5.97219E24;
    
    /**
     * radius of the earth in meters
     */
    public static final double EARTH_RADIUS = 6.8E6;
    
    /**
     * an earth year in hours
     */
    public static final double EARTH_YEAR = 365.25 * 24;
    
    /**
     * how many real milli sec are in one game hour.
     * the idea is to make a game day to be about a 
     * minute. so, thats 60000ms / 24hr... 
     */
    public static final double GAME_HOUR = 2500;
    
    /**
     * what is considered the beginning of time... basically,
     * how old was the game universe conceptually when
     * the game actually began.
     */
    public static final double GAME_YEAR_BEGINNING = 1000000;
    
    /**
     * how many milli sec are there in one game year
     */
    public static final double GAME_YEAR = (GAME_HOUR * 24 * 365.25);
    
    /**
     * the amount of kg in the sun
     */
    public static final double SOLAR_MASS = 1.989E30;
    
    /**
     * the radius of the sun in meters
     */
    public static final double SOLAR_RADIUS = 6.955E8;
    
    /**
     * the temperature of the sun in kelven
     */
    public static final double SOLAR_TEMPERATURE = 5800;
    
    /**
     * the life expectancy of the sun (10 billion years)
     */
    public static final double SOLAR_LIFE_EXPECTANCY = 1E10;
    
    /**
     * the min amount of mass for a body of mass to have fusion.
     * 
     * anything below this is considered a dwarf star.
     * 
     * @TODO: check this constant
     */
    public static final double MASS_OF_SOLAR_FUSION = (SOLAR_MASS * 0.3);
    
    /**
     * the min amount of mass for a BOM to be considered a dwarf star.
     * 
     * @TODO: check this constant
     */
    public static final double MASS_OF_MIN_DWARF = (SOLAR_MASS * 0.05);
    
    /**
     * @TODO: check this constant
     */
    public static final double MASS_OF_MIN_PLANET = (EARTH_MASS * 0.0001);
    
    /**
     * @TODO: check this constant
     */
    public static final double MASS_OF_MIN_MOON = (EARTH_MASS * 0.0000001);
    
    /**
     * 
     */
    public static final double MASS_OF_MAX_MOON = EARTH_MASS;
    
    /**
     * 
     */
    public static final double MASS_OF_MAX_PLANET = MASS_OF_MIN_DWARF * 0.99;
    
    /**
     * 
     */
    public static final double MASS_OF_BLACK_HOLE = (SOLAR_MASS * 10);
    
    /**
     * the max amount of mass a BOM can have for a star.
     * 
     * @TODO: check this constant
     */
    public static final double MASS_OF_MAX_STAR = (SOLAR_MASS * 10000);
    
    /**
     * the amount of m in one light year
     */
    public static final double LIGHTYEAR = 9.4605284E15;
    
    /**
     * light speed in m/hr
     * 9460528400000000 / 8766
     * 1079229796942.7332877024868811316 m/hr
     * 
     * 299786054 m/s
     */
    public static final double LIGHT_SPEED = (LIGHTYEAR / (24 * 365.25));
    
    /**
     * the amount of m in one AU
     */
    public static final double AU = 1.495978707E11;
    
    /**
     * the amount of light years in one km
     */
    public static final double KM_TO_LIGHTYEAR = (1 / LIGHTYEAR);
    
    /**
     * the amount of AUs in one km
     */
    public static final double KM_TO_AU = (1 / AU);
}
