/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.screenshot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author REx
 */
public class ShotImage 
{
    private int[] ignored;
    
    private BufferedImage image;
    
    public ShotImage(String image) throws IOException
    {
        this.image = ImageIO.read(new File(image));
        ignored = null;
    }
    
    public ShotImage(String image, int[] ignored) throws IOException
    {
        this.image = ImageIO.read(new File(image));
        this.ignored = new int[ignored.length];
        System.arraycopy(ignored, 0, this.ignored, 0, ignored.length);
    }
    
    public boolean isIgnored(int x, int y)
    {
        if (ignored == null)
        {
            return false;
        }
        int search = x + image.getWidth() * y;
        for(int ignore : ignored)
        {
            if (ignore == search)
            {
                return true;
            }
        }
        return false;
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
}
