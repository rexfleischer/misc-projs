/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.focus;

import com.rf.fled.desktop.game.GameException;
import com.rf.fled.desktop.game.Viewport;
import com.rf.fledwar.javaclient.connection.ClientConnection;
import com.rf.fledwar.javaclient.connection.FocusQueryBuilder;
import com.rf.fledwar.javaclient.connection.RequestCallback;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.Orbital;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.socket.Message;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class FocusState_System implements FocusState
{
    public static final Logger logger = Logger.getLogger(FocusState.class);
    
    boolean focusset;
    
    private GameException exception;
    
    private ObjectId focusid;
    
    private String focusname;
    
    private double xoff;
    
    private double yoff;
    
    private ClientConnection client;
    
    public FocusState_System(ClientConnection client,
                             String focusname,
                             String systemname)
    {
        this.exception = null;
        this.focusid = null;
        this.focusname = focusname;
        this.client = client;
        this.xoff = 0.0;
        this.yoff = 0.0;
        this.focusset = false;
        
        Map<String, Object> options = new HashMap<>();
        options.put("system", systemname);
        client.asyncQuery("SYSTEM", options, new RequestCallback() 
        {
            @Override
            public void callback(Object requested)
            {
                GalaxySystem focuscenter = new GalaxySystem((Map) requested);
                focusid = focuscenter.getId();
            }
            
            @Override
            public void onError(Message message)
            {
                logger.fatal(message);
                exception = new GameException(String.format(
                        "error during setting up focus: %s", 
                        message));
            }
        });
    }

    public FocusState_System(ClientConnection client, 
                             String focusname, 
                             ObjectId focusid)
    {
        this.exception = null;
        this.focusid = focusid;
        this.focusname = focusname;
        this.client = client;
        this.xoff = 0.0;
        this.yoff = 0.0;
        this.focusset = false;
    }

    @Override
    public void update(Viewport viewport, Map<ObjectId, GalaxySystem> systems)
            throws GameException
    {
        if (exception != null)
        {
            throw exception;
        }
        
        if (focusid == null)
        {
            return;
        }
        
        if (!focusset)
        {
            setFocus();
            focusset = true;
        }
        
        if (xoff == 0.0 && yoff == 0.0)
        {
            GalaxySystem focus = systems.get(focusid);
            if (focus == null || focus.getOrbitalNames().isEmpty())
            {
                return;
            }
            setupInitialFocusView(viewport, systems, focus);
        }
        
        if (!systems.containsKey(focusid))
        {
            logger.warn("unable to find focus system");
            return;
        }
        
        viewShiftUpdate(viewport, systems);
    }
    
    @Override
    public ObjectId getFocusId()
    {
        return focusid;
    }
    
    private void viewShiftUpdate(Viewport viewport, 
                                 Map<ObjectId, GalaxySystem> systems)
    {
        GalaxySystem focus = systems.get(focusid);
        OrbitStatus absoluteCenter = focus.getGalaxyOrbitStatus();
        double xcheck = absoluteCenter.getCartesianX();
        double ycheck = absoluteCenter.getCartesianY();

        Rectangle2D view = viewport.getView();
        double xchange = xoff - xcheck;
        double ychange = yoff - ycheck;
        
        if (xchange != 0 || ychange != 0)
        {
            logger.debug(String.format("orbit status: %s", absoluteCenter));
            logger.debug(String.format("changing focus-> x:%s, y:%s", xchange, ychange));
            logger.debug(String.format("before: %s", view));
            view.setRect(view.getX() - xchange, 
                         view.getY() - ychange, 
                         view.getWidth(), 
                         view.getHeight());
            logger.debug(String.format("after : %s", view));
        }

        xoff = xcheck;
        yoff = ycheck;
    }
    
    private void setFocus() 
            throws GameException
    {
        FocusQueryBuilder builder = new FocusQueryBuilder();
        builder.setViewLevelAll();
        builder.setFocusTypeList();
        builder.setFocusTimeout(2);
        builder.setEndFocus(false);
        builder.setFocusName(focusname);
        builder.addSystemIdToList(focusid.toString());

        try
        {
            client.setFocus(builder);
        }
        catch(IOException ex)
        {
            logger.error("could not send focus message", ex);
            throw new GameException("could not send focus message", ex);
        }
    }
    
    private void setupInitialFocusView(Viewport view, 
                                       Map<ObjectId, GalaxySystem> systems,
                                       GalaxySystem focuson)
    {
        systems.clear();
        systems.put(focuson.getId(), focuson);
        
        OrbitStatus absoluteCenter = focuson.getGalaxyOrbitStatus();
        xoff = absoluteCenter.getCartesianX();
        yoff = absoluteCenter.getCartesianY();
        logger.debug(String.format("orbit status: %s", 
                                   absoluteCenter));
        logger.debug(String.format("orbit status: [x:%s, y:%s]",
                                   absoluteCenter.getCartesianX(),
                                   absoluteCenter.getCartesianY()));
        
        double xoffinit = absoluteCenter.getDistance() * Math.cos(absoluteCenter.getAlpha());
        double yoffinit = absoluteCenter.getDistance() * Math.sin(absoluteCenter.getAlpha());
        
        double largestdist = -1;
        Iterator<String> children = focuson.getOrbitalNames().iterator();
        while(children.hasNext())
        {
            String name = children.next();
            Orbital orbital = focuson.getOrbital(name);
            double distcheck = orbital.getOrbitStatus().getDistance();
            
            if (distcheck > largestdist)
            {
                largestdist = distcheck;
            }
        }
        
        double dy = view.getReal().getHeight();
        double dx = view.getReal().getWidth();

        Rectangle2D newview;
        if (dy < dx)
        {
            double ratio = (dx / dy);
            newview = new Rectangle2D.Double(
                    xoffinit + largestdist * ratio * -1,
                    yoffinit + largestdist * -1,
                    largestdist * ratio * 2,
                    largestdist * 2);
        }
        else
        {
            double ratio = (dy / dx);
            newview = new Rectangle2D.Double(
                    xoffinit + largestdist * -1,
                    yoffinit + largestdist * ratio * -1,
                    largestdist * 2,
                    largestdist * ratio * 2);
        }
        
        logger.info(String.format("setting new view to %s", newview));
        view.setView(newview);
    }
}
