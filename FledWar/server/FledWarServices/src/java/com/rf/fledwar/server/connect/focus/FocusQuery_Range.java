/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 *
 * @author REx
 */
public class FocusQuery_Range implements FocusQuery
{
    private static final Logger logger = Logger.getLogger(FocusQueries.class);
    
    public static final String CENTER_DISTANCE = "center_d";
    
    public static final String CENTER_ALPHA = "center_a";
    
    public static final String CENTER_RADIUS = "center_r";
    
    public static final String FOCUS_TIMEOUT = "focus_timeout";
    
    public static final String NAME_CACHE_TIMEOUT = "name_cache_timeout";
    
    public static final int FOCUS_TIMEOUT_DEFAULT = 1;
    
    public static final int NAME_CACHE_TIMEOUT_DEFAULT = 10;
    
    ConnectionLiaison liaison;
    
    ViewLevel viewLevel;
    
    int timeouts;
    
    int focusTimeout;
    
    int nameCacheTimeout;
    
    ViewQueryRange initial;
    
    ViewQueryIdUpdate cachedNames;
    
    /**
     * return a string if an error occurs
     * @param message
     * @return 
     */
    @Override
    public String init(ConnectionLiaison liaison, Message message) throws Exception
    {
        if (this.liaison != null)
        {
            throw new IllegalStateException("init cannot be called twice");
        }
        this.initial = new ViewQueryRange();
        this.cachedNames = new ViewQueryIdUpdate();
        this.liaison = Objects.requireNonNull(liaison, "liaison");
        return updateFocus(message);
    }
    
    /**
     * 
     * @param message
     * @return
     * @throws Exception 
     */
    @Override
    public String updateFocus(Message message) throws Exception
    {
        Objects.requireNonNull(message, "message");
        timeouts = 0;
        
        Object check = message.getValue(FOCUS_TIMEOUT);
        if (check != null)
        {
            this.focusTimeout = ((Number) check).intValue();
        }
        else
        {
            this.focusTimeout = FOCUS_TIMEOUT_DEFAULT;
        }
        check = message.getValue(NAME_CACHE_TIMEOUT);
        if (check != null)
        {
            this.nameCacheTimeout = ((Number) check).intValue();
        }
        else
        {
            this.nameCacheTimeout = NAME_CACHE_TIMEOUT_DEFAULT;
        }
        
        if (focusTimeout < 1)
        {
            return String.format("%s cannot be less than 1 [value:%s]", 
                                 FOCUS_TIMEOUT, 
                                 focusTimeout);
        }
        if (nameCacheTimeout < 1)
        {
            return String.format("%s cannot be less than 1 [value:%s]", 
                                 NAME_CACHE_TIMEOUT, 
                                 nameCacheTimeout);
        }
        
        try
        {
            initial.init(message);
        }
        catch(Exception ex)
        {
            return String.format("could not get %s: %s", VIEW_LEVEL, 
                                                         ex.getMessage());
        }
        
        try
        {
            viewLevel = ViewLevel.valueOf(
                    message.getValue(VIEW_LEVEL).toString());
        }
        catch(Exception ex)
        {
            return String.format("could not get %s: %s", VIEW_LEVEL, 
                                                         ex.getMessage());
        }
        
        return null;
    }
    
    /**
     * check the status
     * @return 
     */
    @Override
    public Object check() throws Exception
    {
        
        if (timeouts % nameCacheTimeout == 0)
        {
            logger.debug("invalidating name cache");
            cachedNames.invalidate();
        }
        
        List result = null;
        if (timeouts % focusTimeout == 0)
        {
            List<GalaxySystem> galaxies = null;
            logger.debug("checking for updates");
            if (cachedNames.isInit())
            {
                logger.debug("name cache valid, updating from names");
                galaxies = cachedNames.getUpdated(viewLevel);
            }
            else
            {
                logger.debug("name cache not valid");
                List<GalaxySystem> newlist = initial.query(viewLevel);
                if (!newlist.isEmpty())
                {
                    cachedNames.init(newlist);
                    galaxies = newlist;
                }
            }
            
            if (galaxies != null)
            {
                result = new ArrayList(galaxies.size());
                for(int i = 0; i < galaxies.size(); i++)
                {
                    result.add(galaxies.get(i).getDBObject());
                }
            }
            
            logger.debug(String.format("amount of updates: %s", 
                                       galaxies != null ? galaxies.size() : 0));
        }
        
        timeouts++;
        return result;
    }   
}
