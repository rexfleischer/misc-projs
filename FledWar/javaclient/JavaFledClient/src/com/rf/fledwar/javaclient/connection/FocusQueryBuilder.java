/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.javaclient.connection;

import com.rf.fledwar.socket.Message;
import com.sun.corba.se.spi.ior.ObjectId;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author REx
 */
public class FocusQueryBuilder
{
    public static final String FOCUS_END = "focusend";
    
    public static final String FOCUS_NAME = "focusname";
    
    public static final String FOCUS_TYPE = "focustype";
    
    public static final String FOCUS_TYPE_LIST = "list";
    
    public static final String FOCUS_TYPE_RANGE = "range";
    
    public static final String LIST_IDS = "ids";
    
    public static final String CENTER_DISTANCE = "center_d";
    
    public static final String CENTER_ALPHA = "center_a";
    
    public static final String CENTER_RADIUS = "center_r";
    
    public static final String FOCUS_TIMEOUT = "focus_timeout";
    
    public static final String NAME_CACHE_TIMEOUT = "name_cache_timeout";
    
    public static final String VIEW_LEVEL = "view_level";
    
    public static final String VIEW_LEVEL_ALL = "ALL";
    
    public static final String VIEW_LEVEL_NO_ORBITALS = "NO_ORBITALS";
    
    Message message;
    
    public FocusQueryBuilder()
    {
        message = new Message();
    }
    
    public void setEndFocus(boolean end)
    {
        message.putValue(FOCUS_END, end);
    }
    
    public void setFocusName(String name)
    {
        message.putValue(FOCUS_NAME, name);
    }
    
    public void setFocusTypeList()
    {
        message.putValue(FOCUS_TYPE, FOCUS_TYPE_LIST);
    }
    
    public void setFocusTypeRange()
    {
        message.putValue(FOCUS_TYPE, FOCUS_TYPE_RANGE);
    }
    
    public void setDistance(double distance)
    {
        message.putValue(CENTER_DISTANCE, distance);
    }
    
    public void setAlpha(double alpha)
    {
        message.putValue(CENTER_ALPHA, alpha);
    }
    
    public void setRadius(double radius)
    {
        message.putValue(CENTER_RADIUS, radius);
    }
    
    public void setFocusTimeout(int iterations)
    {
        message.putValue(FOCUS_TIMEOUT, iterations);
    }
    
    public void setFocusNameCacheTimeout(int iterations)
    {
        message.putValue(NAME_CACHE_TIMEOUT, iterations);
    }
    
    public void setNoFocusNameCacheTimeout()
    {
        message.putValue(NAME_CACHE_TIMEOUT, Integer.MAX_VALUE);
    }
    
    public void setViewLevelAll()
    {
        message.putValue(VIEW_LEVEL, VIEW_LEVEL_ALL);
    }
    
    public void setViewLevelNoOrbitals()
    {
        message.putValue(VIEW_LEVEL, VIEW_LEVEL_NO_ORBITALS);
    }
    
    public void addSystemIdToList(String id)
    {
        List ids = (List) message.getValue(LIST_IDS);
        if (ids == null)
        {
            ids = new ArrayList();
            message.putValue(LIST_IDS, ids);
        }
        ids.add(id);
    }
    
    public Message getMessage()
    {
        return message;
    }
}
