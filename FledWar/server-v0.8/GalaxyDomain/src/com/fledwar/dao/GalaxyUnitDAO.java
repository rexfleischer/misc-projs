/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.vto.galaxy.unit.GalaxyUnit;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxyUnitDAO extends BasicDAO<GalaxyUnit>
{
  
  public GalaxyUnitDAO(DBCollection collection) throws BasicDAOException
  {
    super(collection);
  }
  
  @Override
  protected GalaxyUnit factory(Map data)
  {
    return new GalaxyUnit(data);
  }
  
  public List<GalaxyUnit> findWithScope(ObjectId system_id)
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject(GalaxyUnit.SCOPE, system_id);
    return find(query);
  }
}
