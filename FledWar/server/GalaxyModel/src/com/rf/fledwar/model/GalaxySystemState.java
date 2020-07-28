/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.model;

/**
 *
 * @author REx
 */
public enum GalaxySystemState
{
    /**
     * the galaxy system is not being updated
     * by another thread
     */
    NONE,
    
    /**
     * the system is being updated by a thread.
     * this makes other threads not touch a
     * system if it is marked as this.
     */
    UPDATING;
    
    public static GalaxySystemState ordinalOf(int ordinal)
    {
        for(GalaxySystemState state : values())
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
