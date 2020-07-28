/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import javax.swing.JOptionPane;

/**
 * 
 * @author REx
 */
public abstract class GameFrame extends Frame
{
    public abstract boolean update(GameTime gametime, 
                                   KeyBoardState keyboard, 
                                   MouseState mouse) 
            throws GameException;
    
    protected abstract void _render(GameTime gametime, 
                                    Graphics2D graphics)
            throws GameException;
    
    private Color clearColor;
    
    private BufferStrategy strategy;
    
    private Dimension frameDimension;
    
    public GameFrame(Dimension frameDimension,
                     int xloc, int yloc, 
                     String title,
                     Color clearColor)
    {
        setSize(frameDimension);
        setTitle(title);
        setLocation(xloc, yloc);
        
        // dont want to handle this yet
        setResizable(false);
        
        // show this frame
        setVisible(true);
        
        // make sure we control when something gets rendered
        setIgnoreRepaint(true);
        
        // back buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        
        this.clearColor = clearColor;
        this.frameDimension = frameDimension;
    }
    
    public void setKeyListener(KeyAdapter adapter)
    {
        addKeyListener(adapter);
    }
    
    public void setMouseListener(MouseAdapter adapter)
    {
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);
    }
    
    public void setWindowListener(WindowAdapter adapter)
    {
        addWindowListener(adapter);
    }
    
    public void showMessage(Object message, String title, int messageType)
    {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public void render(GameTime gametime) throws GameException
    {
        Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
        graphics.setColor(clearColor);
        graphics.fillRect(0, 0, 
                          (int) frameDimension.getWidth(),
                          (int) frameDimension.getHeight());

        // run game render logic
        _render(gametime, graphics);

        // swap buffers
        graphics.dispose();
        strategy.show();
    }
    
}
