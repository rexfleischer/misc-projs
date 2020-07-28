/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.rf.fledwar.engine.MongoConnect;
import com.rf.fledwar.model.GalaxySystem;
import com.rf.fledwar.model.GalaxySystemState;
import com.rf.fledwar.model.util.OrbitStatus;
import com.rf.fledwar.model.util.SpaceFunctionUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author REx
 */
public class GalaxySystemDAO extends BasicDAO<GalaxySystem>
{
    public static final String ORBIT_DISTANCE = 
                String.format("%s.%s", GalaxySystem.ORBIT_STATUS_GALAXY, 
                                       OrbitStatus.DISTANCE);
    
    public static final String ORBIT_ALPHA = 
                String.format("%s.%s", GalaxySystem.ORBIT_STATUS_GALAXY, 
                                       OrbitStatus.ALPHA);
    
    public static final String ORBIT_DELTA_ALPHA = 
                String.format("%s.%s", GalaxySystem.ORBIT_STATUS_GALAXY, 
                                       OrbitStatus.DELTA_ALPHA);
    
    public static final String ORBIT_PATH = 
                String.format("%s.%s", GalaxySystem.ORBIT_STATUS_GALAXY, 
                                       OrbitStatus.ORBIT_PATH);
    
    public static void ensureAllIndexes() throws Exception
    {
        GalaxySystemDAO thisdao = new GalaxySystemDAO();
        thisdao.ensureIndex(new BasicDBObject().append(
                GalaxySystem.STATE, 1));
        thisdao.ensureIndex(new BasicDBObject().append(
                GalaxySystem.SYSTEM_TYPE, 1));
        thisdao.ensureIndex(new BasicDBObject().append(
                GalaxySystem.LAST_UPDATE, -1));
        thisdao.ensureIndex(new BasicDBObject().append(
                GalaxySystem.ORBIT_STATUS, 1));
        thisdao.ensureIndex(new BasicDBObject().append(ORBIT_ALPHA, 1));
        thisdao.ensureIndex(new BasicDBObject().append(ORBIT_DELTA_ALPHA, 1));
        thisdao.ensureIndex(new BasicDBObject().append(ORBIT_DISTANCE, 1));
        thisdao.ensureIndex(new BasicDBObject().append(ORBIT_PATH, 1));
    }
    
    public static void ensureNullState() throws Exception
    {
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        gsdao.getCollection().update(
                new BasicDBObject(),
                new BasicDBObject().append(
                    "$set", new BasicDBObject(GalaxySystem.STATE, 0)
                ), false, true);
        gsdao.getCollection().update(
                new BasicDBObject(),
                new BasicDBObject().append(
                    "$set", new BasicDBObject(GalaxySystem.UPDATE_COUNT, 0)
                ), false, true);
    }

    public static void ensureNullTime() throws Exception
    {
        GalaxySystemDAO gsdao = new GalaxySystemDAO();
        long currenttime = System.currentTimeMillis();
        gsdao.getCollection().update(
                new BasicDBObject(),
                new BasicDBObject().append(
                    "$set", new BasicDBObject(GalaxySystem.LAST_UPDATE, currenttime)
                ), false, true);
    }
    
    public static final String GALAXY_SYSTEM = "galaxy_system";

    public GalaxySystemDAO() throws BasicDAOException
    {
        super(MongoConnect.getSpaceDB().getCollection(GALAXY_SYSTEM), 
              GalaxySystem.class);
    }
    
    public GalaxySystem findWithStateAndFlag(String name, 
                                             GalaxySystemState state)
    {
        BasicDBObject query = new BasicDBObject()
                .append(GalaxySystem.STATE, state.ordinal())
                .append(GalaxySystem.NAME, name);
        BasicDBObject update = new BasicDBObject().append(
                "$set", new BasicDBObject().append(
                    GalaxySystem.STATE, 
                    GalaxySystemState.UPDATING.ordinal()));
        DBObject rawsystem = getCollection().findAndModify(query, update);
        return (rawsystem != null) ? new GalaxySystem((BasicDBObject) rawsystem) : null;
    }
    
    public GalaxySystem findLastUpdatedAndFlag()
    {
        BasicDBObject query = new BasicDBObject().append(
                GalaxySystem.STATE, 
                GalaxySystemState.NONE.ordinal());
        BasicDBObject update = new BasicDBObject().append(
                "$set", new BasicDBObject().append(
                    GalaxySystem.STATE, 
                    GalaxySystemState.UPDATING.ordinal()));
        BasicDBObject sort = new BasicDBObject().append(
                GalaxySystem.LAST_UPDATE, 
                1);
        DBObject rawsystem = getCollection().findAndModify(query, sort, update);
        return (rawsystem != null) ? new GalaxySystem((BasicDBObject) rawsystem) : null;
    }
    
    public void unflagWithoutUpdate(GalaxySystem system)
    {
        BasicDBObject update = new BasicDBObject().append(
                "$set", new BasicDBObject().append(
                    GalaxySystem.STATE, 
                    GalaxySystemState.NONE.ordinal()));
        getCollection().update(new BasicDBObject().append("_id", system.getId()), 
                               update);
    }
    
    public void finishedWithUpdateAndUnflag(GalaxySystem system)
    {
        system.triggerLastUpdate();
        system.setState(GalaxySystemState.NONE);
        system.incrementUpdateCount();
        getCollection().update(new BasicDBObject().append("_id", system.getId()), 
                               system.getDBObject());
    }
    
    @Override
    public void insert(GalaxySystem system)
    {
        system.triggerLastUpdate();
        system.setState(GalaxySystemState.NONE);
        super.insert(system);
    }
    
    public void insertAsIs(GalaxySystem system)
    {
        super.insert(system);
    }
    
    public GalaxySystem findByName(String name) throws BasicDAOException
    {
        return findOne(new BasicDBObject().append(GalaxySystem.NAME, name));
    }
    
    public List<GalaxySystem> getInRange(double delta1, 
                                         double delta2, 
                                         double dist1, 
                                         double dist2)
    {
        return getInRange(delta1, delta2, dist1, dist2, null);
    }
    
    
    public List<GalaxySystem> getInRange(double delta1, 
                                         double delta2, 
                                         double dist1, 
                                         double dist2,
                                         DBObject view)
    {
        delta1 = SpaceFunctionUtil.nomalizeRadianForDB(delta1);
        delta2 = SpaceFunctionUtil.nomalizeRadianForDB(delta2);
        
        BasicDBObject query = new BasicDBObject()
                .append(ORBIT_DISTANCE, new BasicDBObject().append("$gt", dist1)
                                                           .append("$lt", dist2));
        
        
        
        // if delta1 is greater than delta2, then that means we 
        // have to check for over the 0 -> 2*PI division
        if (delta1 < delta2)
        {
            query.append(ORBIT_ALPHA, new BasicDBObject().append("$gt", delta1)
                                                         .append("$lt", delta2));
        }
        else
        {
            BasicDBList ors = new BasicDBList();
            ors.add(new BasicDBObject().append(ORBIT_ALPHA, new BasicDBObject().append("$gt", delta1)));
            ors.add(new BasicDBObject().append(ORBIT_ALPHA, new BasicDBObject().append("$lt", delta2)));
            
            query.append("$or", ors);
        }
        
        DBCursor cursor = getCollection().find(query, view);
        List<GalaxySystem> result = new ArrayList<>(cursor.count());
        while(cursor.hasNext())
        {
            result.add(new GalaxySystem((BasicDBObject) cursor.next()));
        }
        return result;
    }
    
    public List<GalaxySystem> getFromPoint(double alpha, 
                                           double distance, 
                                           double radius)
    {
        return getFromPoint(alpha, distance, radius, null);
    }
    
    public List<GalaxySystem> getFromPoint(double alpha, 
                                           double distance, 
                                           double radius,
                                           DBObject view)
    {
        double alphaDiff = Math.sin(radius / distance);
        List<GalaxySystem> result = getInRange(alpha - alphaDiff, 
                                               alpha + alphaDiff, 
                                               distance - radius, 
                                               distance + radius,
                                               view);
        
        Iterator<GalaxySystem> it = result.iterator();
        while(it.hasNext())
        {
            GalaxySystem system = it.next();
            OrbitStatus systemstatus = system.getGalaxyOrbitStatus();
            double distfromcenter = SpaceFunctionUtil
                    .distanceBetweenTwoObjects(alpha, 
                                               distance, 
                                               systemstatus.getAlpha(), 
                                               systemstatus.getDistance());
            if (distfromcenter > radius)
            {
                it.remove();
            }
        }
        
        return result;
    }
}
