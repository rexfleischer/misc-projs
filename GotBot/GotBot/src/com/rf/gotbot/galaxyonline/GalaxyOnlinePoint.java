/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.galaxyonline;

import java.awt.Point;

/**
 *
 * @author REx
 */
public enum GalaxyOnlinePoint 
{
    EMPTY_RESET_POINT()
    {
        @Override public Point getPoint() { return new Point(25, 70); }
    };
    
    public abstract Point getPoint();
}
