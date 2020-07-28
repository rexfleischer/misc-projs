/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.game;

import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author REx
 */
public abstract class Game //extends Frame
{
    private boolean stop = false;
    
    private KeyBoardInputAdapter keyboard;
    
    private MouseInputAdapter mouse;
    
    private AppWindowAdapter window;
    
    public abstract void initiate(GameTime gametime) throws GameException;
    
    public abstract void update(GameTime gametime) throws GameException;
    
    public abstract void render(GameTime gametime) throws GameException;
    
    public abstract void shutdown() throws GameException;
    
    public void signalStop()
    {
        stop = true;
    }
    
    public KeyBoardState getKeyBoardState()
    {
        if (keyboard == null)
        {
            throw new IllegalStateException("must be within the loop() method to call this");
        }
        return keyboard.createState();
    }
    
    public MouseState getMouseState()
    {
        if (mouse == null)
        {
            throw new IllegalStateException("must be within the loop() method to call this");
        }
        return mouse.createState();
    }
    
    public KeyAdapter getKeyAdapter()
    {
        if (keyboard == null)
        {
            keyboard = new KeyBoardInputAdapter();
        }
        return keyboard;
    }
    
    public MouseAdapter getMouseAdapter()
    {
        if (mouse == null)
        {
            mouse = new MouseInputAdapter();
        }
        return mouse;
    }
    
    public WindowAdapter getDefaultWindowAdapter()
    {
        if (window == null)
        {
            window = new AppWindowAdapter();
        }
        return window;
    }
    
    public void loop() throws GameException
    {
        // game time starts now
        GameTime gametime = new GameTime();
        
        // game init
        initiate(gametime);
        
        while(!stop)
        {
            if (gametime.update())
            {
                update(gametime);
                
                // run game logic
                render(gametime);
            }
            else
            {
                long current = System.currentTimeMillis();
                long waiting = Math.max(
                        (gametime.last + gametime.targetMS) - current, 
                        1);
                try
                {
                    Thread.sleep(waiting);
                }
                catch(InterruptedException ex) { }
            }
        }
        
        shutdown();
        
        keyboard = null;
        mouse = null;
    }
    
    class AppWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent we)
        {
            signalStop();
//            System.exit(0);
        }
    }
}
