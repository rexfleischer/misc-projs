/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.input;

import com.rf.gotbot.input.util.ScreenShot;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author REx
 */
public abstract class ImageCheckAlgorithm 
{
    protected BufferedImage screen;
    
    protected Robot robot;
    
    public ImageCheckAlgorithm(Robot robot)
    {
        this.robot = robot;
        this.screen = null;
    }
    
    public void initNewScreen()
    {
        screen = ScreenShot.shoot(robot);
        initNewScreenCallback();
    }
    
    public void finished()
    {
        screen = null;
    }
    
    protected abstract void initNewScreenCallback();
    
    public abstract void setConfig(ImageCheckConfig config, Object value);
    
    public abstract void setImage(String key, BufferedImage image);
    
    public abstract void setImages(String key, ArrayList<BufferedImage> images);
    
    public abstract void setImageConfig(String imageKey, ImageCheckConfig config, Object value);
    
    public abstract Map<String, ArrayList<Rectangle>> getImages(String[] key);
}
