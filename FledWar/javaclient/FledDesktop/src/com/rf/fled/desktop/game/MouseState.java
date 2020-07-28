/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

/**
 *
 * @author REx
 */
public class MouseState
{
    public static final MouseState EMPTY_STATE = new MouseState();
    
    int x;
    
    int y;
    
    boolean button1Click;
    
    boolean button2Click;
    
    boolean button3Click;
    
    boolean button1Down;
    
    boolean button2Down;
    
    boolean button3Down;
    
    boolean overWindow;
    
    boolean dragging;
    
    int wheelRotation;

    void update(int x, 
                int y,
                boolean button1Click, 
                boolean button2Click, 
                boolean button3Click,
                boolean button1Down,
                boolean button2Down, 
                boolean button3Down, 
                boolean overWindow,
                boolean dragging, 
                int wheelRotation)
    {
        this.x = x;
        this.y = y;
        this.button1Click   = button1Click;
        this.button2Click   = button2Click;
        this.button3Click   = button3Click;
        this.button1Down    = button1Down;
        this.button2Down    = button2Down;
        this.button3Down    = button3Down;
        this.overWindow     = overWindow;
        this.dragging       = dragging;
        this.wheelRotation  = wheelRotation;
    }

    public boolean isButton1Click()
    {
        return button1Click;
    }

    public boolean isButton1Down()
    {
        return button1Down;
    }

    public boolean isButton2Click()
    {
        return button2Click;
    }

    public boolean isButton2Down()
    {
        return button2Down;
    }

    public boolean isButton3Click()
    {
        return button3Click;
    }

    public boolean isButton3Down()
    {
        return button3Down;
    }

    public boolean isDragging()
    {
        return dragging;
    }
    
    public boolean isOverWindow()
    {
        return overWindow;
    }

    public int getWheelRotation()
    {
        return wheelRotation;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
    
    
}
