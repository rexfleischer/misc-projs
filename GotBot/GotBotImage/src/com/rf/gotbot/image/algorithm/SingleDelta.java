/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.algorithm;

import com.rf.gotbot.image.ImageCheckAlgorithm;
import com.rf.gotbot.image.ImageCheckConfig;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author REx
 */
public class SingleDelta extends ImageCheckAlgorithm
{
    public SingleDelta(Robot robot) 
    {
        super(robot);
    }

    @Override
    protected void initNewScreenCallback() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setConfig(ImageCheckConfig config, Object value) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setImageConfig(String imageKey, ImageCheckConfig config, Object value) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setImage(String key, BufferedImage image) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setImages(String key, ArrayList<BufferedImage> images) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Map<String, ArrayList<Rectangle>> getImages(String[] key) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
