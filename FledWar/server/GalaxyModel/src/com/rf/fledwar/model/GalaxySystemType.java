/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

/**
 *
 * @author REx
 */
public enum GalaxySystemType
{
    /**
     * these are galaxy systems that can be play grounds
     * for players and hold stars and such.
     */
    PLAYABLE,
    
    /**
     * this is a system that is used as a center marker
     * for many other systems. this will be used for making
     * clusters of stars (like 30-50 systems) rotate around
     * a common center.
     */
    GRAVITY_CENTER;
    
    public static GalaxySystemType ordinalOf(int ordinal)
    {
        for(GalaxySystemType state : values())
        {
            if (state.ordinal() == ordinal)
            {
                return state;
            }
        }
        
        throw new IllegalArgumentException(
                String.format("unknown ordinal [%s]", ordinal));
    }
}
