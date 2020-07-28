/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fledwar.vto.galaxy.util;

import com.fledwar.dao.BasicDAOException;
import com.fledwar.dao.DAOFactoryRegistry;
import com.fledwar.dao.GalaxyPointDAO;
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GalaxyUnitActionDAO;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.util.JsonHelper;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.system.SystemPoint;
import com.fledwar.vto.galaxy.unit.GalaxyUnit;
import com.fledwar.vto.galaxy.unit.UnitAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.types.ObjectId;

/**
 *
 * @author REx
 */
public class ScopeData
{
  public static final String SCOPE = "scope";
  
  public static final String POINTS = "points";
  
  public static final String UNITS = "units";
  
  public static final String UNIT_ACTIONS = "unit_actions";
  
  public static ScopeData get(DAOFactoryRegistry dao_registry,
                              ObjectId scope_id,
                              boolean scope,
                              boolean points,
                              boolean units,
                              boolean unit_actions) 
          throws BasicDAOException
  {
    if (!scope && !points && !units && !unit_actions) {
      throw new IllegalArgumentException("all scope data parts are null");
    }
    ScopeData result = new ScopeData();
    if (scope) {
      try(GalaxyScopeDAO dao = dao_registry
              .get(GalaxyScopeDAO.class)) {
        result.scope = dao.findOneFromId(scope_id);
      }
    }
    if (points) {
      try(GalaxyPointDAO dao = dao_registry
              .get(GalaxyPointDAO.class)) {
        result.setPoints(dao.findWithScope(scope_id));
      }
    }
    if (units) {
      try(GalaxyUnitDAO dao = dao_registry
              .get(GalaxyUnitDAO.class)) {
        result.setUnits(dao.findWithScope(scope_id));
      }
    }
    if (unit_actions) {
      try(GalaxyUnitActionDAO dao = dao_registry
              .get(GalaxyUnitActionDAO.class)) {
        result.setUnitActions(dao.findWithScope(scope_id));
      }
    }
    return result;
  }
  
  public static ScopeData get(DAOFactoryRegistry dao_registry,
                              ObjectId scope_id)
          throws BasicDAOException
  {
    return get(dao_registry, scope_id, true, true, true, true);
  }
  
  public GalaxyScope scope;
  
  public Map<ObjectId, SystemPoint> points;
  
  public Map<ObjectId, GalaxyUnit> units;
  
  public Map<ObjectId, List<UnitAction>> unit_actions;
  
  public ScopeData()
  {
    
  }
  
  public ScopeData(GalaxyScope scope)
  {
    this.scope = scope;
  }
  
  public void setPoints(List<SystemPoint> new_points)
  {
    points = new HashMap<>();
    Iterator<SystemPoint> it = new_points.iterator();
    while(it.hasNext()) {
      SystemPoint point = it.next();
      points.put(point.getId(), point);
    }
  }
  
  public void setUnits(List<GalaxyUnit> new_units)
  {
    units = new HashMap<>();
    Iterator<GalaxyUnit> it = new_units.iterator();
    while(it.hasNext()) {
      GalaxyUnit unit = it.next();
      units.put(unit.getId(), unit);
    }
  }
  
  public void setUnitActions(List<UnitAction> new_actions)
  {
    unit_actions = new HashMap<>();
    Iterator<UnitAction> it = new_actions.iterator();
    while(it.hasNext()) {
      UnitAction action = it.next();
      List<UnitAction> action_list = unit_actions.get(action.getUnitId());
      if (action_list == null) {
        action_list = new ArrayList<>();
        unit_actions.put(action.getUnitId(), action_list);
      }
      
      action_list.add(action);
    }
    
//    Iterator<Entry<ObjectId, List<UnitAction>>> it_ = unit_actions.entrySet().iterator();
    
  }
  
  public Map toMap() 
  {
    if (scope == null && 
        (points == null || points.isEmpty()) && 
        (units == null || units.isEmpty()) && 
        (unit_actions == null || unit_actions.isEmpty())) {
      return null;
    }
    
    Map result = new HashMap();
    if (scope != null) {
      result.put(SCOPE, scope.getDataAsMap());
    }
    if (points != null && !points.isEmpty()) {
      Map result_points = new HashMap();
      for(Entry<ObjectId, SystemPoint> entry : points.entrySet()) {
        result_points.put(entry.getKey(), entry.getValue().getDataAsMap());
      }
      result.put(POINTS, result_points);
    }
    if (units != null && !units.isEmpty()) {
      Map result_units = new HashMap();
      for(Entry<ObjectId, GalaxyUnit> entry : units.entrySet()) {
        result_units.put(entry.getKey(), entry.getValue().getDataAsMap());
      }
      result.put(UNITS, result_units);
    }
    if (unit_actions != null && !unit_actions.isEmpty()) {
      Map result_unit_actions = new HashMap();
      for(Entry<ObjectId, List<UnitAction>> entry : unit_actions.entrySet()) {
        List action_list = new ArrayList();
        Iterator<UnitAction> it = entry.getValue().iterator();
        while(it.hasNext()) {
          action_list.add(it.next().getDataAsMap());
        }
        result_unit_actions.put(entry.getKey(), action_list);
      }
      result.put(UNIT_ACTIONS, result_unit_actions);
    }
    return result;
  }
  
  public String toPrettyString() 
  {
    return JsonHelper.toPrettyJson(toMap());
  }
}
