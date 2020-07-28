/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

/**
 *
 * @author REx
 */
public final class GameTime
{
    /**
     * the time in ms this object was created
     */
    long start;
    
    /**
     * the total time in ms this object has been in memory
     */
    long total;
    
    /**
     * whether or not the game time is running at target
     */
    boolean slow;
    
    /**
     * the amount of ms sense the last time update() returned true
     */
    long elapsed;
    
    /**
     * the time in ms of the last time update() returned true
     */
    long last;
    
    /**
     * the target ms for update() to return true
     */
    int targetMS;
    
    /**
     * the target amount of times update() is to return true in one second
     */
    int targetFPS;
    
    /**
     * the estimated actual fps
     */
    int actualFPS;
    
    /**
     * this helps count the number of fps
     */
    long fpsMSStart;
    
    /**
     * this gets reset every time fpsMSStart gets updated, and then the current
     * counted value gets set to actualFPS
     */
    int fpsCount;
    
    GameTime()
    {
        start = System.currentTimeMillis();
        fpsMSStart = start;
        setTargetFPS(30);
    }
    
    boolean update()
    {
        long current = System.currentTimeMillis();
        
        if ((last + targetMS) > current)
        {
            return false;
        }
        
        if ((fpsMSStart + 1000) < current)
        {
            fpsMSStart = current;
            actualFPS = fpsCount;
            fpsCount = 0;
        }
        fpsCount++;
        
        slow = ((current - last) > (2 * targetMS));
        
        total = (current - start);
        
        elapsed = (current - last);
        
        last = current;
        
        return true;
    }
    
    public void setTargetFPS(int fps)
    {
        targetFPS = fps;
        targetMS = (1000 / fps);
    }
    
    public int getTargetFPS()
    {
        return targetFPS;
    }
    
    public int getActualFPS()
    {
        return actualFPS;
    }
    
    public long getElapsedTime()
    {
        return elapsed;
    }
    
    public long getTotalGameTime()
    {
        return total;
    }
    
    public boolean isRunningSlowly()
    {
        return slow;
    }
}
