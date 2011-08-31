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
public class DeltaImage implements GotBotImage<short[][]>
{
    private int oWidth;
    
    private int oHeight;

    private short[] horizontal;
    
    private short[] vertical;
    
    public short getUp(int x, int y)
    {
        if (y == 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        return vertical[x + (y - 1) * oWidth];
    }
    
    public short getDown(int x, int y)
    {
        if (y == (oHeight - 1))
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        return vertical[x + y * oWidth];
    }
    
    public short getLeft(int x, int y)
    {
        if (x == 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        return horizontal[(x - 1) + y * (oWidth - 1)];
    }
    
    public short getRight(int x, int y)
    {
        if (x == (oWidth - 1))
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        return horizontal[x + y * (oWidth - 1)];
    }
    
    @Override
    public int getWidth()
    {
        return oWidth;
    }
    
    @Override
    public int getHeight()
    {
        return oHeight;
    }
    
    @Override
    public short[][] getRaw()
    {
        return new short[][]{horizontal, vertical};
    }

    @Override
    public void init(int width, int height, short[][] init) 
    {
        if (init.length != 2)
        {
            throw new IllegalArgumentException(
                    "init must be two arrays");
        }
        if (init[0].length != height * (width - 1))
        {
            throw new IllegalArgumentException(
                    "init[0] does not have the required amount of data");
        }
        if (init[1].length != (height - 1) * width)
        {
            throw new IllegalArgumentException(
                    "init[1] does not have the required amount of data");
        }
        oWidth      = width;
        oHeight     = height;
        horizontal  = init[0];
        vertical    = init[1];
    }
    
    @Override
    public int getPixel(int x, int y)
    {
        int rgb = 0;
        int total = 0;

        if (y != 0)
        {
            rgb += vertical[x + (y - 1) * oWidth];
            total++;
        }
        if (x != 0)
        {
            rgb += horizontal[(x - 1) + y * (oWidth - 1)];
            total++;
        }
        if (y != (oHeight - 1))
        {
            rgb += vertical[x + y * oWidth];
            total++;
        }
        if (x != (oWidth - 1))
        {
            rgb += horizontal[x + y * (oWidth - 1)];
            total++;
        }
        rgb = rgb / total;

        return (rgb << 16) + (rgb << 8) + rgb;
    }
    
    @Override
    public BufferedImage toBufferedImage()
    {
        BufferedImage result = new BufferedImage(
                oWidth, oHeight, BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < oHeight; y++)
        {
            for(int x = 0; x < oWidth; x++)
            {
                result.setRGB(x, y, getPixel(x, y));
            }
        }
        
        return result;
    }
    
}
