/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.types;

import com.rf.gotbot.image.GotBotImage;
import java.awt.image.BufferedImage;

/**
 *
 * @author REx
 */
public class GotBotDeviation implements GotBotImage
{
    private short[] average;
    
    private short[] deviation;
    
    private int width;
    
    private int height;

    @Override
    public void init(int width, int height, Object init)
    {
        short[][] actual = (short[][]) init;
        if (actual.length != 2)
        {
            throw new IllegalArgumentException(
                    "init.length != 2");
        }
        if (actual[0].length != width * height)
        {
            throw new IllegalArgumentException(
                    "init[0].length != width * height");
        }
        if (actual[1].length != width * height)
        {
            throw new IllegalArgumentException(
                    "init[1].length != width * height");
        }
        average     = actual[0];
        deviation   = actual[1];
        this.width  = width;
        this.height = height;
    }
    
    @Override
    public int getHeight() 
    {
        return height;
    }

    @Override
    public int getWidth() 
    {
        return width;
    }
    
    public boolean isAcceptablePixel(int x, int y, int pixel)
    {
        short comp = (short)(pixel & 0x000000FF);
        int index = x + y * width;
        return (average[index] - (deviation[index] * 1.2)) 
                    <= comp && comp <= 
               (average[index] + (deviation[index] * 1.2));
    }
    
    public int getPixelMax(int x, int y)
    {
        int index = x + y * width;
        return (average[index] + deviation[index]);
    }
    
    public int getPixelMin(int x, int y)
    {
        int index = x + y * width;
        return (average[index] - deviation[index]);
    }

    @Override
    public int getPixel(int x, int y) 
    {
        int index = x + y * width;
        return (int) ((average[index] << 16) + (average[index] << 8) + average[index]);
    }

    @Override
    public short[][] getRaw() 
    {
        return new short[][]{ average, deviation };
    }

    @Override
    public BufferedImage toBufferedImage() 
    {
        BufferedImage result = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                result.setRGB(x, y, getPixel(x, y));
            }
        }
        
        return result;
    }
    
}
