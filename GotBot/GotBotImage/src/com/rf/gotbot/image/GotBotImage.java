/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.gotbot.image;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author REx
 */
public interface GotBotImage extends Serializable
{
    public void init(int width, int height, Object init);
    
    public Object getRaw();
    
    public int getWidth();
    
    public int getHeight();
    
    public int getPixel(int x, int y);
    
    public BufferedImage toBufferedImage();
}
