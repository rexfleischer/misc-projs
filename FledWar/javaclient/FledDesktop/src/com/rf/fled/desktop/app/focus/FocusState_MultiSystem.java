/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fled.desktop.app.focus;

import com.rf.fled.desktop.app.view.WindowFocusType;
import com.rf.fled.desktop.game.GameException;
import com.rf.fled.desktop.game.Viewport;
import com.rf.fledwar.javaclient.connection.ClientConnection;
import com.rf.fledwar.javaclient.connection.FocusQueryBuilder;
import com.rf.fledwar.javaclient.connection.RequestCallback;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import com.rf.fledwar.socket.Message;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class FocusState_MultiSystem implements FocusState
{
    private static final Logger logger = Logger.getLogger(FocusState_MultiSystem.class);
    
    private static final long FOCUS_UPDATE_WAIT = 10000;
    
    private long lastFocusUpdate;
    
    private boolean focusset;
    
    private GameException exception;
    
    private ObjectId focusid;
    
    private String focusname;
    
    private double xoff;
    
    private double yoff;
    
    private ClientConnection client;
    
    public FocusState_MultiSystem(ClientConnection client, 
                                  String focusname,
                                  ObjectId focusid)
    {
        this.exception = null;
        this.focusset = false;
        this.lastFocusUpdate = -1;
        this.focusid = focusid;
        this.focusname = focusname;
        this.client = client;
        this.xoff = 0.0;
        this.yoff = 0.0;
    }

    @Override
    public ObjectId getFocusId()
    {
        return focusid;
    }
    
    @Override
    public void update(Viewport viewport, Map<ObjectId, GalaxySystem> systems)
            throws GameException
    {
        if (exception != null)
        {
            throw exception;
        }
        
        if (!focusset)
        {
            initiateFocus(viewport, systems);
            focusset = true;
        }
        
        if (System.currentTimeMillis() > (FOCUS_UPDATE_WAIT + lastFocusUpdate))
        {
            updateFocusingOn(viewport);
        }
        
        viewShiftUpdate(viewport, systems);
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
        
        
        ObjectId newidcheck = findClosestCenter(viewport, systems);
        if (!newidcheck.equals(focusid))
        {
            // if they are not equal, then we need to change
            // the focus id to the new one. but first we need
            // to reset the x and y offset to make sure the
            // view stays smooth.
            GalaxySystem newfocus = systems.get(newidcheck);
            OrbitStatus newcenter = newfocus.getGalaxyOrbitStatus();
            xoff = newcenter.getCartesianX();
            yoff = newcenter.getCartesianY();
            focusid = newidcheck;
            
            if (logger.isInfoEnabled())
            {
                logger.info(String.format("new focus id set to %s", focusid));
            }
        }
    }
    
    private ObjectId findClosestCenter(Viewport viewport, 
                                       Map<ObjectId, GalaxySystem> systems)
    {
        double cx = viewport.getView().getCenterX();
        double cy = viewport.getView().getCenterY();
        double targetdist = Math.sqrt(cx * cx + cy * cy);
        double targetalpha = Math.atan2(cy, cx);
        
        ObjectId result = null;
        double distance = Double.MAX_VALUE;
        for(GalaxySystem system : systems.values())
        {
            OrbitStatus status = system.getGalaxyOrbitStatus();
            double check = SpaceFunctionUtil.distanceBetweenTwoObjects(
                    targetalpha, 
                    targetdist, 
                    status.getAlpha(), 
                    status.getDistance());
            if (distance > check)
            {
                distance = check;
                result = system.getId();
            }
        }
        return result;
    }
    
    private void initiateFocus(Viewport viewport, Map<ObjectId, GalaxySystem> systems)
    {
        GalaxySystem focuson = systems.get(focusid);
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
        
        double largestdist = WindowFocusType.MULTI_SYSTEM.maxin * 1.1;
        
        double dy = viewport.getReal().getHeight();
        double dx = viewport.getReal().getWidth();

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
        viewport.setView(newview);
        
        updateFocusingOn(viewport);
    }
    
    private void updateFocusingOn(Viewport viewport)
    {
        double cx = viewport.getView().getCenterX();
        double cy = viewport.getView().getCenterY();
        double targetdist = Math.sqrt(cx * cx + cy * cy);
        double targetalpha = Math.atan2(cy, cx);
        
        Map<String, Object> options = new HashMap<>();
        options.put("view_level", "NO_ORBITALS");
        options.put("center_d", targetdist);
        options.put("center_a", targetalpha);
        options.put("center_r", WindowFocusType.MULTI_SYSTEM.maxout);
        lastFocusUpdate = System.currentTimeMillis();
        
        client.asyncQuery("RANGE", options, new RequestCallback() 
        {
            @Override
            public void callback(Object requested)
            {
                List abouttofocus = (List) requested;
                
                FocusQueryBuilder builder = new FocusQueryBuilder();
                builder.setViewLevelNoOrbitals();
                builder.setFocusTypeList();
                builder.setFocusTimeout(5);
                builder.setEndFocus(false);
                builder.setFocusName(focusname);
                for(Object rawsystem : abouttofocus)
                {
                    GalaxySystem system = new GalaxySystem((Map) rawsystem);
                    builder.addSystemIdToList(system.getId().toString());
                }
                
                try
                {
                    client.setFocus(builder);
                }
                catch(IOException ex)
                {
                    logger.error("could not send focus message", ex);
                    exception = new GameException("could not send focus message", ex);
                }
            }
            
            @Override
            public void onError(Message error)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
}
