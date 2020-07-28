/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.input;

import com.rf.fled.desktop.game.KeyBoardState;
import com.rf.fled.desktop.game.MouseState;
import com.rf.fled.desktop.game.Viewport;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

/**
 * this checks user input and handles the adjustments
 * of the objects
 * @author REx
 */
public class UserInput
{
    private static final double SCROLL_SPEED = 0.01;
    
    private static final double ZOOM_IN = 0.9;
    
    private static final double ZOOM_OUT = 1.1;
    
    KeyBoardState prevKey;
    
    MouseState prevMouse;
    
    public UserInput(KeyBoardState newKey, MouseState newMouse)
    {
        prevKey = newKey;
        prevMouse = newMouse;
    }
    
    public UserInput()
    {
        this(KeyBoardState.EMPTY_STATE, MouseState.EMPTY_STATE);
    }
    
    public boolean update(KeyBoardState newKey, 
                          MouseState newMouse,
                          Viewport viewport)
    {
        if (newKey.isKeyDown(KeyEvent.VK_ESCAPE))
        {
            return false;
        }
        
        if (viewport == null)
        {
            return true;
        }
        
        /**
         * keyboard input
         */
        if (newKey.isKeyDown(KeyEvent.VK_CONTROL))
        {
            if (newKey.isKeyDown(KeyEvent.VK_UP))
            {
                zoomIn(viewport);
            }
            if (newKey.isKeyDown(KeyEvent.VK_DOWN))
            {
                zoomOut(viewport);
            }
        }
        else
        {
            if (newKey.isKeyDown(KeyEvent.VK_UP))
            {
                Rectangle2D view = viewport.getView();
                view.setRect(view.getX(), 
                             view.getY() - (view.getHeight() * SCROLL_SPEED), 
                             view.getWidth(), 
                             view.getHeight());
            }
            if (newKey.isKeyDown(KeyEvent.VK_DOWN))
            {
                Rectangle2D view = viewport.getView();
                view.setRect(view.getX(), 
                             view.getY() + (view.getHeight() * SCROLL_SPEED), 
                             view.getWidth(), 
                             view.getHeight());
            }
            if (newKey.isKeyDown(KeyEvent.VK_RIGHT))
            {
                Rectangle2D view = viewport.getView();
                view.setRect(view.getX() + (view.getHeight() * SCROLL_SPEED), 
                             view.getY(), 
                             view.getWidth(), 
                             view.getHeight());
            }
            if (newKey.isKeyDown(KeyEvent.VK_LEFT))
            {
                Rectangle2D view = viewport.getView();
                view.setRect(view.getX() - (view.getHeight() * SCROLL_SPEED), 
                             view.getY(), 
                             view.getWidth(), 
                             view.getHeight());
            }
        }
        
        /**
         * mouse input
         */
        int wheel = newMouse.getWheelRotation();
        if (wheel > 0)
        {
//            if (newMouse.isOverWindow())
//            {
//                Rectangle2D view = viewport.getView();
//                double transformX = (newMouse.getX() / viewport.getReal().getX()) * 0.5;
//                double transformY = (newMouse.getY() / viewport.getReal().getY()) * 0.5;
//                double newwidth = view.getWidth() * ZOOM_OUT;
//                double newheight = view.getHeight() * ZOOM_OUT;
//                view.setRect(view.getX() + ((view.getWidth() - newwidth) / 2),
//                             view.getY() + ((view.getHeight() - newheight) / 2),
//                             newwidth, 
//                             newheight);
//            }
//            else
//            {
                zoomOut(viewport);
//            }
        }
        else if (wheel < 0)
        {
//            if (newMouse.isOverWindow())
//            {
//                Rectangle2D view = viewport.getView();
//                
//                // get widths so we can shorten the size of the code
//                double c_width = viewport.getReal().getWidth();
//                double c_height = viewport.getReal().getHeight();
//                
//                // get the percentage of the distance from the
//                // center to the mouse... then divide by two
//                double transformX = ((newMouse.getX() - c_width * 0.5) / c_width) * 0.5;
//                double transformY = ((newMouse.getY() - c_height * 0.5) / c_height) * 0.5;
//                
//                // find widths like normal
//                double newwidth = view.getWidth() * ZOOM_IN;
//                double newheight = view.getHeight() * ZOOM_IN;
//                
//                // apply the transform to x and y, then add the 
//                // second transform of 
//                double newx = view.getX() - (view.getX() * transformX * 0.5); 
//                newx += ((view.getWidth() - newwidth) * 0.5);
//                double newy = view.getY() - (view.getY() * transformY * 0.5);
//                newy += ((view.getHeight() - newheight) * 0.5);
//                
//                view.setRect(newx, newy, newwidth, newheight);
//            }
//            else
            {
                zoomIn(viewport);
            }
        }
        if (newMouse.isDragging())
        {
            int dx = (newMouse.getX() - prevMouse.getX());
            int dy = (newMouse.getY() - prevMouse.getY());
            
            Rectangle2D real = viewport.getReal();
            Rectangle2D view = viewport.getView();
            
            // transform x and shift
            double transformX = (dx / real.getWidth()) * view.getWidth();
            double transformY = (dy / real.getHeight()) * view.getHeight();
            view.setRect(view.getX() - transformX, 
                         view.getY() - transformY,
                         view.getWidth(), 
                         view.getHeight());
        }
        
        
        prevKey = newKey;
        prevMouse = newMouse;
        return true;
    }
    
    private void zoomOut(Viewport viewport)
    {
        Rectangle2D view = viewport.getView();
        double newwidth = view.getWidth() * ZOOM_OUT;
        double newheight = view.getHeight() * ZOOM_OUT;
        view.setRect(view.getX() + ((view.getWidth() - newwidth) / 2),
                     view.getY() + ((view.getHeight() - newheight) / 2),
                     newwidth, 
                     newheight);
    }
    
    private void zoomIn(Viewport viewport)
    {
        Rectangle2D view = viewport.getView();
        double newwidth = view.getWidth() * ZOOM_IN;
        double newheight = view.getHeight() * ZOOM_IN;
        view.setRect(view.getX() + ((view.getWidth() - newwidth) / 2),
                     view.getY() + ((view.getHeight() - newheight) / 2),
                     newwidth, 
                     newheight);
    }
}
