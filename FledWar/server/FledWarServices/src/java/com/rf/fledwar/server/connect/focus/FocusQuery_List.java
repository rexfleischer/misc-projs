/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.server.connect.focus;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.rf.fledwar.engine.dao.GalaxySystemDAO;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.util.ObjectIdHelper;
import com.rf.fledwar.socket.Message;
import com.rf.fledwar.socket.connection.ConnectionLiaison;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class FocusQuery_List implements FocusQuery
{
    private static Logger logger = Logger.getLogger(FocusQuery_List.class);
    
    public static final String LIST_IDS = "ids";
    
    public static final String FOCUS_TIMEOUT = "focus_timeout";
    
    public static final int FOCUS_TIMEOUT_DEFAULT = 1;
    
    ConnectionLiaison liaison;
    
    ViewLevel viewLevel;
    
    ViewQueryIdUpdate query;
    
    List ids;
    
    int timeouts;
    
    int focusTimeout;

    @Override
    public String init(ConnectionLiaison liaison, Message message) throws Exception
    {
        logger.info(String.format("initiating list focus: %s", message));
        if (this.liaison != null)
        {
            throw new IllegalStateException("init cannot be called twice");
        }
        this.liaison = Objects.requireNonNull(liaison, "liaison");
        this.query = new ViewQueryIdUpdate();
        return updateFocus(message);
    }

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
        
        if (focusTimeout < 1)
        {
            return String.format("%s cannot be less than 1 [value:%s]", 
                                 FOCUS_TIMEOUT, 
                                 focusTimeout);
        }
        logger.debug(String.format("setting focus timeout to %s", focusTimeout));
        
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
        logger.debug(String.format("setting view level to %s", viewLevel));
        
        try
        {
            ids = (List) message.getValue(LIST_IDS);
        }
        catch(Exception ex)
        {
            return String.format("could not get %s: %s", LIST_IDS, 
                                                         ex.getMessage());
        }
        
        if (ids == null || ids.isEmpty())
        {
            return String.format("%s must specify a list of GalaxySystem ids", 
                                 LIST_IDS);
        }
        logger.debug(String.format("setting ids to %s", ids));
        
        query.invalidate();
        
        return null;
    }

    @Override
    public Object check() throws Exception
    {
        List<GalaxySystem> querycheck;
        
        if (!query.isInit())
        {
            BasicDBObject initquery = new BasicDBObject();
            BasicDBList idors = new BasicDBList();
            for(Object rawid : ids)
            {
                ObjectId id = ObjectIdHelper.parseObject(rawid);
                idors.add(new BasicDBObject(GalaxySystem.ID, id));
            }
            initquery.append("$or", idors);
            GalaxySystemDAO gsdao = new GalaxySystemDAO();
            DBCursor cursor = gsdao.getCollection().find(initquery, viewLevel.getView());
            querycheck = new ArrayList<>(cursor.size());
            while(cursor.hasNext())
            {
                GalaxySystem system = new GalaxySystem((Map) cursor.next());
                querycheck.add(system);
            }
            
            if (logger.isDebugEnabled())
            {
                List<ObjectId> found = new ArrayList<>(querycheck.size());
                for(GalaxySystem system : querycheck)
                {
                    found.add(system.getId());
                }
                logger.debug(String.format("query ran: %s", initquery));
                logger.debug(String.format("systems found: %s", found));
            }
            
            query.init(querycheck);
            
        }
        else
        {
            timeouts++;
            querycheck = (timeouts % focusTimeout == 0) 
                    ? query.getUpdated(viewLevel) 
                    : null;
        }
        
        
        if (querycheck == null || querycheck.isEmpty())
        {
            return null;
        }
        
        List result = new ArrayList(querycheck.size());
        for(GalaxySystem system : querycheck)
        {
            result.add(system.getDBObject());
        }
        return result;
    }
    
}
