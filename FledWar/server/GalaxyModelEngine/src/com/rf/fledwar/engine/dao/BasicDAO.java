/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.fledwar.engine.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.rf.fledwar.model.BasicVTO;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class BasicDAO<T extends BasicVTO>
{
    private DBCollection collection;
    
    private Constructor constructor;
    
    public BasicDAO(DBCollection collection, Class<T> clazz) 
            throws BasicDAOException
    {
        this.collection = collection;
        try
        {
            this.constructor = clazz.getConstructor(Map.class);
        }
        catch(NoSuchMethodException | SecurityException ex)
        {
            throw new BasicDAOException("could not find constructor", ex);
        }
    }

    public void insert(T object)
    {
        collection.insert(object.getDBObject());
    }
    
    public void update(T object)
    {
        collection.update(new BasicDBObject().append("_id", object.get("_id")), 
                          object.getDBObject());
    }

    public T findOneFromId(ObjectId id)
            throws BasicDAOException
    {
        return findOne(new BasicDBObject().append("_id", id));
    }
    
    public T findOne(DBObject query)
            throws BasicDAOException
    {
        DBObject data = collection.findOne(query);
        return (data != null) ? factory(data) : null;
    }
    
    public T removeFromId(String id)
            throws BasicDAOException
    {
        return removeOne(new BasicDBObject().append("_id", id));
    }
    
    public T removeOne(DBObject query)
            throws BasicDAOException
    {
        DBObject data;
        try
        {
            data = collection.findAndRemove(query);
        }
        catch(NullPointerException ex)
        {
            return null;
        }
        return factory(data);
    }
    
    public void removeFromIdQuick(ObjectId id)
    {
        removeQuick(new BasicDBObject().append("_id", id));
    }
    
    public void removeFromIdQuick(String id)
    {
        removeQuick(new BasicDBObject().append("_id", id));
    }
    
    public void removeQuick(DBObject query)
    {
        try
        {
            collection.remove(query);
        }
        catch(NullPointerException ex) { }
    }
    
    protected void ensureIndex(DBObject index)
    {
        collection.ensureIndex(index);
    }
    
    public final DBCollection getCollection()
    {
        return collection;
    }
    
    private T factory(DBObject data)
            throws BasicDAOException
    {
        T result = null;
        try
        {
            result = (T) constructor.newInstance((BasicDBObject) data);
        }
        catch(InstantiationException | 
              IllegalAccessException | 
              IllegalArgumentException | 
              InvocationTargetException ex)
        {
            throw new BasicDAOException("could not create new instance", ex);
        }
        return result;
    }
}
