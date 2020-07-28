/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.query;

import com.mongodb.DBObject;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.server.connect.focus.ViewLevel;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author REx
 */
public class Query_Range implements Query
{
    
    public static final String VIEW_LEVEL = "view_level";
    
    public static final String CENTER_DISTANCE = "center_d";
    
    public static final String CENTER_ALPHA = "center_a";
    
    public static final String CENTER_RADIUS = "center_r";
    

    @Override
    public Object exec(ConnectionLiaison liaison, Message message) throws Exception
    {
        double distance;
        double radius;
        double alpha;
        DBObject view;
        
        try
        {
            distance = (double) message.getValue(CENTER_DISTANCE);
        }
        catch(Exception ex)
        {
            throw new QueryException(String.format("could not get %s: %s", 
                                                   CENTER_DISTANCE, 
                                                   ex.getMessage()));
        }
        
        try
        {
            radius = (double) message.getValue(CENTER_RADIUS);
        }
        catch(Exception ex)
        {
            throw new QueryException(String.format("could not get %s: %s", 
                                                   CENTER_RADIUS, 
                                                   ex.getMessage()));
        }
        
        try
        {
            alpha = (double) message.getValue(CENTER_ALPHA);
        }
        catch(Exception ex)
        {
            throw new QueryException(String.format("could not get %s: %s", 
                                                   CENTER_ALPHA, 
                                                   ex.getMessage()));
        }
        
        try
        {
            String viewcheck = (String) message.getValue(VIEW_LEVEL);
            view = ViewLevel.valueOf(viewcheck).getView();
        }
        catch(Exception ex)
        {
            throw new QueryException(String.format("could not get %s: %s", 
                                                   VIEW_LEVEL, 
                                                   ex.getMessage()));
        }
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        List<GalaxySystem> systems = gsdao.getFromPoint(alpha, 
                                                        distance, 
                                                        radius, 
                                                        view);
        if (systems.isEmpty())
        {
            return null;
        }
        
        List result = new ArrayList();
        for(GalaxySystem system : systems)
        {
            result.add(system.getDBObject());
        }
        
        return result;
    }
    
}
