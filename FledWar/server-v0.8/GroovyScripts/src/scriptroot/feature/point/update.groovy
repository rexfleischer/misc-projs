

import com.fledwar.dao.GalaxyPointDAO;
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.system.SystemPoint;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.util.SpaceFunctionUtil;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;


/**
 * the variables that are in this script are:
 * feature_cache : values that will be saved from one call to another..
 *                 only able to be seen by this feature
 * feature_config : the config for the given feature
 * feature_drop_key : the drop key for this specific feature... this
 *                    is unique to all features and scopes
 * scope_id : the scope of this feature for update
 * scope_data : the object that contains the shared data
 *    - scope : the actual scope
 *    - points : the galaxy points in the scope
 *    - units : the units in the scope
 * scope_cache : values saved for the entire scope
 * dao_registry : the dao registry
 */


def delta = exec(scope_data, 
                 get_orbiting(scope_data, feature_cache, dao_registry), 
                 feature_config.getAsInteger("time_scale"));
               
if (!delta) {
  return null;
}

// first, update the scope
def scope_update = new BasicDBObject('$set', delta.get(ScopeData.SCOPE));

def point_updates = [:];
for(def entry : delta.get(ScopeData.POINTS).entrySet()) {
  point_updates.put(new ObjectId(entry.getKey()), 
                    new BasicDBObject('$set', entry.getValue()));
}

def scope_dao;
def point_dao;

try {
  scope_dao = dao_registry.getDAOFactory(GalaxyScopeDAO.class);
  point_dao = dao_registry.getDAOFactory(GalaxyPointDAO.class);
  
  scope_dao.update(scope_id, scope_update);
  point_dao.updateAll(point_updates);
}
finally {
  if (scope_dao != null) {
    scope_dao.close();
  }
  if (point_dao != null) {
    point_dao.close();
  }
}

def update_delay = feature_config.getAsInteger("time_delay");
def update_apply_at = scope_data.scope.getLastSystemUpdate() + update_delay;
delta.put("update_time_at", update_apply_at);
delta.put("update_drop_key", feature_drop_key);

return delta;


def exec(def target, def orbiting, def timescale)
{
  // no need to update if it was just updated
  long timeelapse = (System.currentTimeMillis() - 
                     target.scope.getLastSystemUpdate());
  target.scope.triggerLastSystemUpdate();
  if (timeelapse < 1) {
    return null;
  }
  def delta = new BasicDBObject();
  def scope_delta = new BasicDBObject(
          GalaxyScope.LAST_SYSTEM_UPDATE, 
          target.scope.getLastSystemUpdate());
  delta.put(ScopeData.SCOPE, scope_delta);



  def gamehours = SpaceFunctionUtil
          .systemMSToGameHours(timeelapse) * timescale;

  def points_delta = new BasicDBObject();
  for(def point : target.points.values()) {
    def point_id = point.getId();
    def orientation = point.getObjectOrientation();
    orientation.updateElapseTime(gamehours);
    points_delta.put(point_id.toString(), new BasicDBObject(
            GalaxyPointDAO.ORIENTATION_ALPHA, 
            orientation.getAlpha()));
  }
  delta.put(ScopeData.POINTS, points_delta);



  // if there is a center specified, then we will update
  // the system orbit status...
  if (orbiting != null) {

    def orbiting_location = orbiting.getGalaxyOrientation();
    def orientation = target.scope.getObjectOrientation();
    orientation.updateElapseTime(gamehours);

    target.scope.getGalaxyOrientation().setAlphaDistance(orbiting_location, 
                                                         orientation);
    def target_location = target.scope.getGalaxyOrientation();

    scope_delta.put(GalaxyScope.ORIENTATION_POS_X, 
                    target_location.getPosX());
    scope_delta.put(GalaxyScope.ORIENTATION_POS_Y, 
                    target_location.getPosY());
    scope_delta.put(GalaxyScope.ORIENTATION_ALPHA, 
                    target_location.getAlpha());
    scope_delta.put(GalaxyScope.ORIENTATION_DISTANCE, 
                    target_location.getDistance());

    scope_delta.put(GalaxyScope.ORBITING_ALPHA,
                    orientation.getAlpha());
  }

  return delta;
}

def get_orbiting(def scope_data,
                 def feature_cache,
                 def dao_registry)
{
  if (feature_cache.orbiting) {
    return feature_cache.orbiting;
  }
  else if (scope_data.scope.orbiting.object_id) {
    def dao;
    def check;
    try {
      dao = dao_registry.get(GalaxyScopeDAO.class);
      check = dao.findOneFromId(scope_data.scope.orbiting.object_id);
    }
    finally {
      if (dao != null) {
        dao.close();
      }
    }
    if (check == null) {
      throw new Exception("unable to find orbiting when orbiting id was set");
    }
    feature_cache.orbiting = check;
  }
  return null;
}
