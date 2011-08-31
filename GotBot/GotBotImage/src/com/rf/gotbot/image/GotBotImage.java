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
public interface GotBotImage<_Ty> extends Serializable
{
    public void init(int width, int height, _Ty init);
    
    public _Ty getRaw();
    
    public int getWidth();
    
    public int getHeight();
    
    public int getPixel(int x, int y);
    
    public BufferedImage toBufferedImage();
}
