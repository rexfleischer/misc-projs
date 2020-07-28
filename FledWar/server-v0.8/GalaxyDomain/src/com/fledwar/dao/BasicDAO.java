/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.util.PoolManager.Poolable;
import com.fledwar.vto.BasicVTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public abstract class BasicDAO<T extends BasicVTO> implements
        Poolable, AutoCloseable
{
  public static final String ID = "_id";
  
  protected static final String QUERY_GT = "$gt";
  
  protected static final String QUERY_LT = "$lt";
  
  protected static final String QUERY_GTE = "$gte";
  
  protected static final String QUERY_LTE = "$lte";

  protected static final String QUERY_NOT_EQUAL = "$ne";
  
  protected static final String UPDATE_PUSH_ARRAY = "$push";
  
  protected static final String UPDATE_PULL_ARRAY_ELEMENT = "$pull";
  
  protected static final String UPDATE_POP_ARRAY = "$pop";
  
  protected static final String UPDATE_SET = "$set";
  
  protected static final String UPDATE_UNSET = "$unset";
  
  protected static final BasicDBObject EMPTY_QUERY =
          new BasicDBObject();
  
  protected static final BasicDBObject ONLY_ID_VIEW =
          new BasicDBObject("_id", 1);

  
  protected abstract T factory(Map data);
  
  private DBCollection collection;
  
  private boolean active;

  public BasicDAO(DBCollection collection)
          throws BasicDAOException
  {
    this.collection = collection;
  }

  @Override
  public void setToActive()
  {
    active = true;
  }

  @Override
  public boolean isActive()
  {
    return active;
  }

  @Override
  public void close() throws BasicDAOException
  {
    active = false;
  }

  @Override
  public void finish()
  {
  }

  public void insert(T object)
  {
    collection.insert(object.getDataAsDBObject());
  }
  
  public void insertAll(Collection<T> objects) 
  {
    List<DBObject> inserts = new ArrayList<>(objects.size());
    for(T object : objects) {
      inserts.add(object.getDataAsDBObject());
    }
    collection.insert(inserts);
  }

  public void update(T object)
  {
    BasicDBObject data = object.getDataAsDBObject();
    collection.update(new BasicDBObject().append("_id", data.get("_id")),
                      data);
  }
  
  public void updateAll(Collection<T> objects) 
  {
    for(T object : objects) {
      update(object);
    }
  }
  
  public void update(ObjectId id, DBObject update)
  {
    collection.update(new BasicDBObject(ID, id), update);
  }
  
  public void updateAll(Map<ObjectId, DBObject> id_to_updates) 
  {
    Iterator<Entry<ObjectId, DBObject>> it = 
            id_to_updates.entrySet().iterator();
    while(it.hasNext()) {
      Entry<ObjectId, DBObject> entry = it.next();
      ObjectId id = entry.getKey();
      DBObject update = entry.getValue();
      update(id, update);
    }
  }

  public T findOneFromId(ObjectId id)
          throws BasicDAOException
  {
    return findOne(new BasicDBObject().append("_id", id), null);
  }

  public T findOneFromId(ObjectId id, DBObject view)
          throws BasicDAOException
  {
    return findOne(new BasicDBObject().append("_id", id), view);
  }

  public T findOne(DBObject query)
          throws BasicDAOException
  {
    return findOne(query, null);
  }

  public T findOne(DBObject query, DBObject view)
          throws BasicDAOException
  {
    DBObject data = collection.findOne(query, view);
    return (data != null) ? factory((Map) data) : null;
  }

  public T findOneAndModify(DBObject query, DBObject update)
  {
    DBObject data = collection.findAndModify(
            query, null, null, false, update, true, false);
    return (data != null) ? factory((Map) data) : null;
  }

  public List<T> findAll()
  {
    DBCursor cursor = collection.find();
    return packageCursor(cursor);
  }
  
  public List<T> find(DBObject query)
          throws BasicDAOException
  {
    return find(query, null);
  }

  public List<T> find(DBObject query, DBObject view)
          throws BasicDAOException
  {
    DBCursor cursor = collection.find(query, view);
    return packageCursor(cursor);
  }

  public List<ObjectId> getAllIds()
  {
    DBCursor cursor = collection.find(EMPTY_QUERY, ONLY_ID_VIEW);

    List<ObjectId> result = new ArrayList<>(cursor.count());
    while(cursor.hasNext()) {
      DBObject object = cursor.next();
      result.add((ObjectId) object.get("_id"));
    }
    return result;
  }

  public T removeFromId(ObjectId id)
          throws BasicDAOException
  {
    return removeOne(new BasicDBObject().append("_id", id));
  }

  public T removeOne(DBObject query)
          throws BasicDAOException
  {
    DBObject data;
    try {
      data = collection.findAndRemove(query);
    }
    catch(NullPointerException ex) {
      return null;
    }
    return factory((Map) data);
  }

  public void removeFromIdQuick(ObjectId id)
  {
    removeQuick(new BasicDBObject().append("_id", id));
  }

  public void removeQuick(DBObject query)
  {
    try {
      collection.remove(query);
    }
    catch(NullPointerException ex) {
    }
  }

  public final DBCollection getCollection()
  {
    return collection;
  }

  protected List<T> packageCursor(DBCursor cursor)
  {
    List<T> result = new ArrayList<>(cursor.count());
    while(cursor.hasNext()) {
      T document = factory((Map) cursor.next());
      result.add(document);
    }
    return result;
  }
}
