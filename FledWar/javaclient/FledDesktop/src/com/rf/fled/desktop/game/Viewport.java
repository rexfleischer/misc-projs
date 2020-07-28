/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

import java.awt.geom.Rectangle2D;

/**
 * this represents the logic for what is focused on
 * @author REx
 */
public class Viewport
{
    
    /**
     * represents the view in the game
     */
    private Rectangle2D view;
    
    /**
     * represents the resolution and view
     * to the consumer
     */
    private Rectangle2D real;
    
    public Viewport()
    {
        view = null;
        real = null;
    }

    public Rectangle2D getView()
    {
        return view;
    }

    public void setView(Rectangle2D view)
    {
        this.view = view;
    }

    public Rectangle2D getReal()
    {
        return real;
    }

    public void setReal(Rectangle2D real)
    {
        this.real = real;
    }
    
    public void ready()
    {
        /**
         * nothing to do yet. when the viewport does more
         * there will be computations that we only want
         * to do once, and those will go here.
         */
    }
    
    public boolean place(double x, double y)
    {
        return view.contains(x, y);
    }
    
    public int transformX(double x)
    {
        // first, we want to get the percentage
        // from the left
        double transform = ((x - view.getX()) / view.getWidth());
        return (int) (transform * real.getWidth());
    }
    
    public int transformY(double y)
    {
        // same as transformY(), but will go from top
        double transform = ((y - view.getY()) / view.getHeight());
        return (int) (transform * real.getHeight());
    }
    
//    private double rotation;
//    public double triangleArea(double x1,
//                               double y1,
//                               double x2,
//                               double y2, 
//                               double x3, 
//                               double y3)
//    {
//        return (x3*y2 - x2*y3) - (x3*y1 - x1*y3) + (x2*y1 - x1*y2);
//    }
//    
//    public boolean place(double x, double y)
//    {
//        double ab = triangleArea(ax, ay, bx, by, x, y);
//        double bc = triangleArea(bx, by, cx, cy, x, y);
//        
//    }
}
