/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image.util;

import java.awt.image.BufferedImage;

/**
 *
 * @author REx
 */
public class DeltaImage
{
    private int oWidth;
    
    private int oHeight;

    private short[] horizontal;
    
    private short[] vertical;
    
    public DeltaImage(BufferedImage image)
    {
        oWidth = image.getWidth();
        oHeight = image.getHeight();
        horizontal  = new short[oHeight * (oWidth - 1)];
        vertical    = new short[(oHeight - 1) * oWidth];
        
        // do this manually...
        short[] temp = new short[oWidth * oHeight];
        for(int y = 0; y < oHeight; y++)
        {
            for(int x = 0; x < oWidth; x++)
            {
                temp[x + y * oWidth] = (short)(image.getRGB(x, y) & 0x000000ff);
            }
        }
        
        // first, do horizontal
        for(int y = 0; y < oHeight; y++)
        {
            for(int x = 0; x < oWidth - 1; x++)
            {
                horizontal[x + y * (oWidth - 1)] = (short)
                        Math.abs(temp[x + y * oWidth] - temp[(x + 1) + y * oWidth]);
            }
        }
        
        // now do vertical
        for(int y = 0; y < oHeight - 1; y++)
        {
            for(int x = 0; x < oWidth; x++)
            {
                vertical[x + y * oWidth] = (short)
                        Math.abs(temp[x + y * oWidth] - temp[x + (y + 1) * oWidth]);
            }
        }
    }
    
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
    
    public BufferedImage toDeltaBufferedImage()
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
