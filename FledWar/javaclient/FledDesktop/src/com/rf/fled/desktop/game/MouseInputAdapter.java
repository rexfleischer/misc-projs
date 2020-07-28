/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author REx
 */
class MouseInputAdapter extends MouseAdapter
{
    static final int MAX_INPUTS = 5;
    
    MouseState[] states;
    
    int state;
    
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

    public MouseInputAdapter()
    {
        state = 0;
        states = new MouseState[MAX_INPUTS];
        for(int i = 0; i < MAX_INPUTS; i++)
        {
            states[i] = new MouseState();
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        overWindow = true;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {	
        overWindow = false;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) 
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                button1Click = true;
                break;
            case MouseEvent.BUTTON2:
                button2Click = true;
                break;
            case MouseEvent.BUTTON3:
                button3Click = true;
                break;
        }
    }

    @Override
    public synchronized void mousePressed(MouseEvent e)
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                button1Down = true;
                break;
            case MouseEvent.BUTTON2:
                button2Down = true;
                break;
            case MouseEvent.BUTTON3:
                button3Down = true;
                break;
        }
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e)
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                button1Down = false;
                break;
            case MouseEvent.BUTTON2:
                button2Down = false;
                break;
            case MouseEvent.BUTTON3:
                button3Down = false;
                break;
        }
        dragging = false;
    }

    @Override
    public synchronized void mouseWheelMoved(MouseWheelEvent e)
    {
        wheelRotation = e.getWheelRotation();
    }

    @Override
    public synchronized void mouseDragged(MouseEvent e)
    {
        updatePos(e);
        dragging = true;
    }

    @Override
    public synchronized void mouseMoved(MouseEvent e)
    {
        updatePos(e);
    }
    
    private void updatePos(MouseEvent e)
    {
        x = e.getX();
        y = e.getY();
    }

    synchronized MouseState createState()
    {
        MouseState result = getNextState();
        
        result.update(x, y, 
                      button1Click,
                      button2Click,
                      button3Click, 
                      button1Down, 
                      button2Down, 
                      button3Down,
                      overWindow,
                      dragging, 
                      wheelRotation);
        
        button1Click = false;
        button2Click = false;
        button3Click = false;
        wheelRotation = 0;
        
        return result;
    }
    
    MouseState getNextState()
    {
        MouseState result = states[state];
        state++;
        if (state == states.length)
        {
            state = 0;
        }
        return result;
    }
}
