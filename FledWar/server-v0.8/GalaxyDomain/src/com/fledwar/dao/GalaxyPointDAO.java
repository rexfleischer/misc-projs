/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.vto.galaxy.system.SystemPoint;
import com.fledwar.vto.galaxy.system.SystemPointType;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class GalaxyPointDAO extends BasicDAO<SystemPoint>
{
  public static final String ORIENTATION_ALPHA = 
          String.format("%s.%s", SystemPoint.ORIENTATION,
                                 ObjectOrientation.ALPHA);

  public GalaxyPointDAO(DBCollection collection) 
          throws BasicDAOException
  {
    super(collection);
  }
  
  public List<SystemPoint> findWithScope(ObjectId scope) 
          throws BasicDAOException
  {
    BasicDBObject query = new BasicDBObject(SystemPoint.SCOPE, scope);
    return find(query);
  }

  @Override
  protected SystemPoint factory(Map data)
  {
    return SystemPointType.factorySystemPoint(data);
  }
  
}
