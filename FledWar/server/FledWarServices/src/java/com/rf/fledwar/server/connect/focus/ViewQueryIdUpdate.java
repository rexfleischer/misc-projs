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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
class ViewQueryIdUpdate
{
    private BasicDBObject query;
    
    private Map<String, BasicDBObject> namesToQuery;
    
    ViewQueryIdUpdate()
    {
        query = null;
        namesToQuery = null;
    }
    
    boolean isInit()
    {
        return query != null;
    }
    
    void invalidate()
    {
        query = null;
    }
    
    void init(List<GalaxySystem> systems)
    {
        if (systems.isEmpty())
        {
            return;
        }
        
        query = new BasicDBObject();
        namesToQuery = new HashMap<>();
        BasicDBList systemQuery = new BasicDBList();
        
        for(GalaxySystem system : systems)
        {
            BasicDBObject finding = new BasicDBObject()
                    .append(GalaxySystem.ID, system.getId())
                    .append(GalaxySystem.UPDATE_COUNT, new BasicDBObject("$ne", system.getUpdateCount()));
            systemQuery.add(finding);
            namesToQuery.put(system.getName(), finding);
        }
        
        query.append("$or", systemQuery);
    }
    
    List<GalaxySystem> getUpdated(ViewLevel view) throws Exception
    {
        if (query == null)
        {
            return null;
        }
        
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        DBCursor cursor = gsdao.getCollection().find(query, view.getView());
        
        if (cursor.count() == 0)
        {
            return null;
        }
        
        String[] updatenames = new String[cursor.count()];
        int[] updatecount = new int[cursor.count()];
        List<GalaxySystem> result = new ArrayList<>(cursor.count());
        
        int at = 0;
        while(cursor.hasNext())
        {
            GalaxySystem update = new GalaxySystem((Map) cursor.next());
            updatenames[at] = update.getName();
            updatecount[at] = update.getUpdateCount();
            result.add(update);
            
            at++;
        }
        
        for(int i = 0; i < updatenames.length; i++)
        {
            BasicDBObject systemquery = namesToQuery.get(updatenames[i]);
            systemquery.put(GalaxySystem.UPDATE_COUNT, new BasicDBObject("$ne", updatecount[i]));
        }
        
        return result;
    }
}
