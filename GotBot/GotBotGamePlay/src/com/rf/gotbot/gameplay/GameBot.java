/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.gameplay;

import com.rf.gotbot.output.SystemOutput;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class GameBot 
{
    
    protected SystemOutput output;
    
    protected Robot robot;
    
    public GameBot(int pauseMilli) throws AWTException
    {
        robot = new Robot();
        output = new SystemOutput(robot, pauseMilli);
    }
    
    public abstract Map<String, ArrayList<Rectangle>> getState(String[] imageCheck);
    
    public abstract boolean performAction(String action);
    
    public abstract void pause(int milli);
    
    protected void doImageString(
            String[] theseImages, 
            boolean restateEachIter,
            boolean doClick)
    {
        Map<String, ArrayList<Rectangle>> state = null;
        
        if (!restateEachIter)
        {
            state = getState(theseImages);
        }
        
        for(String image : theseImages)
        {
            if (restateEachIter)
            {
                state = getState(new String[]{image});
            }
            
            if (!state.containsKey(image))
            {
                throw new IllegalArgumentException(
                        "image does not exist: " + image);
            }
            
            if (doClick)
            {
                output.leftClick(state.get(image).get(0));
            }
            else
            {
                output.targetOnMouse(state.get(image).get(0));
            }
        }
    }
    
    protected void doFirstImage(String[] theseImages)
    {
        Map<String, ArrayList<Rectangle>> state = getState(theseImages);
        for(String image : theseImages)
        {
            if (state.containsKey(image))
            {
                output.leftClick(state.get(image).get(0));
                return;
            }
        }
        throw new IllegalArgumentException(
                "none of the asked images found: " 
                + Arrays.toString(theseImages));
    }
}
