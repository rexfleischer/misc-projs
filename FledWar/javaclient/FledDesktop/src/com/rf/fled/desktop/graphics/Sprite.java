/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.graphics;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author REx
 */
public class Sprite implements Renderable
{
    int posX;
    
    int posY;
    
    Image image;
    
    public Sprite(Graphics2D graphics, URL url) throws IOException
    {
        Image sourceImage = ImageIO.read(url);
        GraphicsConfiguration gc = graphics.getDeviceConfiguration();
        image = gc.createCompatibleImage(
                sourceImage.getWidth(null),
                sourceImage.getHeight(null),
                Transparency.BITMASK);
        image.getGraphics().drawImage(sourceImage, 0, 0, null);
    }

    public int getPosX()
    {
        return posX;
    }

    public void setPosX(int posX)
    {
        this.posX = posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public void setPosY(int poxY)
    {
        this.posY = poxY;
    }

    @Override
    public void render(Graphics2D graphics)
    {
        graphics.drawImage(image, posX, posY, null);
    }
}
