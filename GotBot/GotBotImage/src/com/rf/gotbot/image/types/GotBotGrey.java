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
public class GotBotGrey implements GotBotImage
{
    private int height;
    
    private int width;
    
    private byte[] data;

    @Override
    public void init(int width, int height, Object init) 
    {
        byte[] actual = (byte[]) init;
        if (width * height != actual.length)
        {
            throw new IllegalArgumentException("init != width * heigth");
        }
        this.width  = width;
        this.height = height;
        data = new byte[width * height];
        System.arraycopy(actual, 0, data, 0, width * height);
    }

    @Override
    public byte[] getRaw() 
    {
        return data;
    }
    
    @Override
    public int getWidth()
    {
        return width;
    }
    
    @Override
    public int getHeight()
    {
        return height;
    }
    
    public byte getByte(int x, int y)
    {
        return data[x + y * width];
    }

    @Override
    public int getPixel(int x, int y) 
    {
        int index = x + y * width;
        return (data[index] << 16) + (data[index] << 8) + data[index];
    }

    @Override
    public BufferedImage toBufferedImage() 
    {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        
        int counter = 0;
        int[] rgbArray = new int[width * height];
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                rgbArray[counter] = 
                        (data[counter] << 16) + 
                        (data[counter] << 8) + 
                         data[counter];
                counter++;
            }
        }
        result.setRGB(0, 0, width, height, rgbArray, 0, width);
        
        return result;
    }
    
}
