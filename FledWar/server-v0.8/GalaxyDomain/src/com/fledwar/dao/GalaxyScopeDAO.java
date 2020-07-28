/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.dao;

import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.util.GalaxyOrientation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author REx
 */
public class GalaxyScopeDAO extends BasicDAO<GalaxyScope>
{

  public GalaxyScopeDAO(DBCollection collection) throws BasicDAOException
  {
    super(collection);
  }

  public List<GalaxyScope> getFromPoint(double alpha,
                                        double distance,
                                        double radius)
          throws BasicDAOException
  {
    return getFromPoint(alpha, distance, radius, null);
  }

  public List<GalaxyScope> getFromPoint(double alpha,
                                        double distance,
                                        double radius,
                                        DBObject view)
          throws BasicDAOException
  {
    GalaxyOrientation center = new GalaxyOrientation(alpha, distance);
    BasicDBObject query = new BasicDBObject();
    query.put(GalaxyScope.ORIENTATION_POS_X, new BasicDBObject()
            .append(BasicDAO.QUERY_GTE, center.getPosX() - radius)
            .append(BasicDAO.QUERY_LTE, center.getPosX() + radius));
    query.put(GalaxyScope.ORIENTATION_POS_Y, new BasicDBObject()
            .append(BasicDAO.QUERY_GTE, center.getPosY() - radius)
            .append(BasicDAO.QUERY_LTE, center.getPosY() + radius));
    
    List<GalaxyScope> result = find(query, view);
    
    Iterator<GalaxyScope> it = result.iterator();
    while(it.hasNext()) {
      GalaxyScope system = it.next();
      GalaxyOrientation location = system.getGalaxyOrientation();
      double distance_check = center.distanceFrom(location);
      if (distance_check > radius) {
        it.remove();
      }
    }
    
    return result;
  }

  @Override
  protected GalaxyScope factory(Map data)
  {
    return new GalaxyScope(data);
  }
}
