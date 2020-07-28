/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.graphics;

import com.rf.fled.desktop.game.Viewport;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author REx
 */
public class Text implements Renderable
{
    private int x;
    
    private int y;
    
    private Color color;
    
    private String message;
    
    public Text()
    {
        
    }

    public Text(int x, int y, Color color, String message)
    {
        this.x = x;
        this.y = y;
        this.color = color;
        this.message = message;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }
    
    @Override
    public void render(Graphics2D graphics)
    {
        graphics.setColor(color);
        graphics.drawString(message, x, y);
    }
}
