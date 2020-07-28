/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.view;

import com.rf.fled.desktop.app.focus.FocusState;
import com.rf.fled.desktop.app.focus.FocusState_MultiSystem;
import com.rf.fled.desktop.app.focus.FocusState_System;
import com.rf.fled.desktop.app.input.UserInput;
import com.rf.fled.desktop.game.GameException;
import com.rf.fled.desktop.game.GameFrame;
import com.rf.fled.desktop.game.GameTime;
import com.rf.fled.desktop.game.KeyBoardState;
import com.rf.fled.desktop.game.MouseState;
import com.rf.fled.desktop.game.Viewport;
import com.rf.fled.desktop.graphics.Text;
import com.rf.fled.desktop.util.BasicStatus;
import com.rf.fledwar.javaclient.connection.ClientConnection;
import com.rf.fledwar.model.GalaxySystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GameGraphicsWindow extends GameFrame
{
    private static final Logger logger = Logger.getLogger(GameGraphicsWindow.class);
    
    public final long MIN_FOCUS_CHANGE_WAIT = 1000;
    
    private BasicStatus gamestatus;
    
    private MouseState updatemouse;
    
    private ClientConnection client;
    
    private UserInput input;
    
    private Viewport viewport;
    
    private String focusname;
    
    private GameException exception;
    
    private Map config;
    
    private Map<ObjectId, GalaxySystem> systems;
    
    private GalaxySystemRender systemrender;
    
    private GalaxySystemUpdater systemupdater;
    
    private FocusState focus;

    public GameGraphicsWindow(Dimension frameDimension,
                              int xloc, int yloc, 
                              String title,
                              Color clearColor,
                              String focusName)
    {
        super(frameDimension, xloc, yloc, title, clearColor);
        this.focusname = focusName;
        this.input = new UserInput();
        this.viewport = new Viewport();
        this.viewport.setReal(new Rectangle2D.Double(
                0, 0, frameDimension.width, frameDimension.height));
        this.systems = new HashMap<>();
        this.systemrender = new GalaxySystemRender(viewport);
        this.systemupdater = new GalaxySystemUpdater(focusName);
    }
    
    public void setClientConnection(ClientConnection client)
    {
        this.client = client;
        gamestatus = new BasicStatus(client, Color.RED);
        gamestatus.attach(new Text() {
            @Override public void render(Graphics2D graphics)
            {
                setMessage(String.format("mouse [x:%s, y:%s, window:%s]", 
                                         updatemouse.getX(), 
                                         updatemouse.getY(),
                                         updatemouse.isOverWindow()));
                super.render(graphics);
            }
        });
        gamestatus.attach(new Text() {
            @Override public void render(Graphics2D graphics)
            {
                if (viewport.getView() != null)
                {
                    setMessage(String.format("viewport: x -> %s", viewport.getView().getX()));
                    super.render(graphics);
                }
            }
        });
        gamestatus.attach(new Text() {
            @Override public void render(Graphics2D graphics)
            {
                if (viewport.getView() != null)
                {
                    setMessage(String.format("viewport: y -> %s", viewport.getView().getY()));
                    super.render(graphics);
                }
            }
        });
        gamestatus.attach(new Text() {
            @Override public void render(Graphics2D graphics)
            {
                if (viewport.getView() != null)
                {
                    setMessage(String.format("viewport: w -> %s", viewport.getView().getWidth()));
                    super.render(graphics);
                }
            }
        });
        gamestatus.attach(new Text() {
            @Override public void render(Graphics2D graphics)
            {
                if (viewport.getView() != null)
                {
                    setMessage(String.format("viewport: h -> %s", viewport.getView().getHeight()));
                    super.render(graphics);
                }
            }
        });
        gamestatus.attach(new Text() {
            @Override public void render(Graphics2D graphics)
            {
                setMessage(String.format("system count: %s", systems.size()));
                super.render(graphics);
            }
        });
    }
    
    public void setGameConfiguration(Map config)
    {
        this.config = config;
        this.systemupdater.setTimeScale(Double.parseDouble(
                config.get("gm.timescale").toString()));
    }
    
    /**
     * this attempts to reset all of the focuses and makes the system
     * handed in be the only thing in focus for this view
     * @param systemname
     * @throws GameException 
     */
    public void focusOnSystem(String systemname)
            throws GameException
    {
        focus = new FocusState_System(client, focusname, systemname);
    }

    @Override
    public boolean update(GameTime gametime, 
                          KeyBoardState keyboard, 
                          MouseState mouse) 
            throws GameException
    {
        if (exception != null)
        {
            GameException throwing = exception;
            exception = null;
            throw throwing;
        }
        
        gamestatus.update(gametime, client);
        updatemouse = mouse;
        
        if (!input.update(keyboard, mouse, viewport))
        {
            return false;
        }
        
        systemupdater.updateSystems(client, systems);
        
        if (focus != null)
        {
            focus.update(viewport, systems);
            
            if (viewport.getView() != null)
            {
                double viewwidth = Math.max(viewport.getView().getWidth(), 
                                            viewport.getView().getHeight());
                if (viewwidth > WindowFocusType.SYSTEM.maxout)
                {
                    if (!(focus instanceof FocusState_MultiSystem))
                    {
                        // change to multi system focus
                        focus = new FocusState_MultiSystem(client, focusname, focus.getFocusId());
                    }
                }
                else if (viewwidth < WindowFocusType.MULTI_SYSTEM.maxin)
                {
                    if (!(focus instanceof FocusState_System))
                    {
                        // change to single system focus
                        focus = new FocusState_System(client, focusname, focus.getFocusId());
                    }
                }
            }
        }
        
        
        return true;
    }

    @Override
    protected void _render(GameTime gametime, Graphics2D graphics) 
            throws GameException
    {
        if (viewport.getView() == null || viewport.getReal() == null)
        {
            gamestatus.render(graphics);
            return;
        }
        
        systemrender.renderSystems(graphics, systems);
        
        gamestatus.render(graphics);
    }
    
}
