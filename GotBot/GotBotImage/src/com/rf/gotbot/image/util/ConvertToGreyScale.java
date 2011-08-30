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
public class ConvertToGreyScale 
{
    public static BufferedImage convert(final BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage result = new BufferedImage(
                width, height, 
                BufferedImage.TYPE_BYTE_GRAY);
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                int rgb = image.getRGB(x, y);
                int grey = (int)(
                            ((rgb & 0x00FF0000) >> 16) * 0.3 +  // red
                            ((rgb & 0x0000FF00) >> 8) * 0.59 +  // green
                             (rgb & 0x000000FF) * 0.11);        // blue
                result.setRGB(x, y, (grey << 16) + (grey << 8) + grey);
            }
        }
        
        return result;
    }
}
