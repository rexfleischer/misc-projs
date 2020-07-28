/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.vto.galaxy.unit.UnitAction;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxyUnitActionDAO extends BasicDAO<UnitAction>
{

  public GalaxyUnitActionDAO(DBCollection collection) throws BasicDAOException
  {
    super(collection);
  }

  @Override
  protected UnitAction factory(Map data)
  {
    return new UnitAction(data);
  }
  
  public List<UnitAction> findWithScope(ObjectId scope)
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject(UnitAction.SCOPE, scope);
    return find(query);
  }
  
}
