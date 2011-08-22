/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.screenshot;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author REx
 */
public class ScreenShot 
{
    private BufferedImage screen;
    
    public ScreenShot(Robot robot)
    {
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        screen = robot.createScreenCapture(
                new Rectangle(0, 0, 
                        (int) screenDim.getWidth(),
                        (int) screenDim.getHeight()));
    }
    
    public Map<String, ArrayList<Rectangle>> checkForAllImages(
                        Map<String, ShotImage> images, 
                        int errorFactor)
    {
        Map<String, ArrayList<Rectangle>> result = new HashMap<String, ArrayList<Rectangle>>();
        Set<Map.Entry<String, ShotImage>> entries = images.entrySet();
        
        return result;
    }
    
    public ArrayList<Rectangle> checkForImage(
            ShotImage image, int errorFactor)
    {
        ArrayList<Rectangle> results = new ArrayList<Rectangle>();
        
        int searchWidth = screen.getWidth() - image.getImage().getWidth();
        int searchHeight = screen.getHeight() - image.getImage().getHeight();
        
        for(int x = 0; x < searchWidth; x++)
        {
            for(int y = 0; y < searchHeight; y++)
            {
                if (unsafeCheckImage(x, y, image, errorFactor))
                {
                    results.add(new Rectangle(x, y, 
                            image.getImage().getWidth(), 
                            image.getImage().getHeight()));
                }
            }
        }
        
        return results;
    }
    
    private boolean unsafeCheckImage(
            int xStart, int yStart, ShotImage image, int error)
    {
        int width = image.getImage().getWidth();
        int height = image.getImage().getHeight();
        int errorAmount = 0;
        
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if (!image.isIgnored(x, y))
                {
                    int imageColor = image.getImage().getRGB(x, y);
                    
                    int screenColor = screen.getRGB(x + xStart, y + yStart);
                    
                    if (imageColor != screenColor)
                    {
                        int redDif = Math.abs(
                                ((imageColor & 0x00FF0000) >> 16) -
                                ((screenColor & 0x00FF0000) >> 16));
                        int greenDif = Math.abs(
                                ((imageColor & 0x0000FF00) >> 8) -
                                ((screenColor & 0x0000FF00) >> 8));
                        int blueDif = Math.abs(
                                (imageColor & 0x000000FF) -
                                (screenColor & 0x000000FF));
                        errorAmount += (redDif + greenDif + blueDif);
                        if (errorAmount > error)
                        {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    @Override
    public String toString()
    {
        return screen.toString();
    }
}
