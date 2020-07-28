/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.rf.fledwar.engine.GalaxyModelEngine;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.SpaceConstents;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import com.rf.fledwar.socket.Message;
import java.util.List;

/**
 *
 * @author REx
 */
class ViewQueryRange
{
    double distance;
    
    double radius;
    
    double alpha;
    
    long lastQuery;
    
    String init(Message message)
    {
        try
        {
            distance = (double) message.getValue(FocusQuery_Range.CENTER_DISTANCE);
        }
        catch(Exception ex)
        {
            return String.format("could not get %s: %s", FocusQuery_Range.CENTER_DISTANCE, 
                                                         ex.getMessage());
        }
        
        try
        {
            radius = (double) message.getValue(FocusQuery_Range.CENTER_RADIUS);
        }
        catch(Exception ex)
        {
            return String.format("could not get %s: %s", FocusQuery_Range.CENTER_RADIUS, 
                                                         ex.getMessage());
        }
        
        try
        {
            alpha = (double) message.getValue(FocusQuery_Range.CENTER_ALPHA);
        }
        catch(Exception ex)
        {
            return String.format("could not get %s: %s", FocusQuery_Range.CENTER_ALPHA, 
                                                         ex.getMessage());
        }
        
        lastQuery = System.currentTimeMillis();
        
        return null;
    }

    List<GalaxySystem> query(ViewLevel view) throws Exception
    {
        long now = System.currentTimeMillis();
        long elapseTime = (now - lastQuery);
        lastQuery = now;
        
        
        
        // we will rotate with the galaxy spin just to make sure
        // that we keep in focus with the initial origin
        double timescale = GalaxyModelEngine.getTimeScale();
        double gamehours = SpaceFunctionUtil.systemMSToGameHours(elapseTime) * timescale;
        double galaxySpin = SpaceConstents.GALAXY_ANGULAR_SPEED * gamehours;
        alpha += galaxySpin;
        alpha %= (2 * Math.PI);
        if (alpha < 0.0)
        {
            alpha += (2 * Math.PI);
        }
        
        
        
        // now the query is pretty simple
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        return gsdao.getFromPoint(alpha, distance, radius, view.getView());
    }
    
    
    @Override
    public String toString()
    {
        return String.format("[distance: %s, alpha: %s, radius: %s]",
                             distance, 
                             alpha, 
                             radius);
    }
}
