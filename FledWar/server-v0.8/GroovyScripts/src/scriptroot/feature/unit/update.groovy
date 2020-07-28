

/**
 * this script is the entrance script to updating the units
 * within a solar system, or single unit of scope.
 */
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GalaxyUnitActionDAO;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.util.SpaceFunctionUtil;
import org.apache.log4j.Logger;
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

def logger = Logger.getLogger("com.fledwar.scriptroot.feature.unit.update");

def delta = exec(scope_data,
                 logger, 
                 feature_config.getAsInteger("time_scale"),
                 [
                  feature_cache   : feature_cache, 
                  feature_config  : feature_config,
                  feature_drop_key: feature_drop_key,
                  scope_id        : scope_id,
                  scope_data      : scope_data,
                  scope_cache     : scope_cache,
                  dao_registry    : dao_registry,
                 ]);
               
if (!delta) {
  return null;
}

// first, update the scope
def scope_update = new BasicDBObject('$set', delta.get(ScopeData.SCOPE));

def unit_updates = [:];
def unit_update_deltas = delta.get(ScopeData.UNITS);
for(def entry : unit_update_deltas.entrySet()) {
  unit_updates.put(new ObjectId(entry.getKey()), 
                   new BasicDBObject('$set', entry.getValue()));
}

def unit_action_deletes = [];
synchronized(scope_data.unit_actions) {
  def it = scope_data.unit_actions.entrySet().iterator();
  while(it.hasNext()) {
    def entry = it.next();
    
    def unit_update;
    def unit_action_list = entry.getValue();
    while(!unit_action_list.isEmpty() && 
           unit_action_list.get(0).hasFinished()) {
      def action = unit_action_list.remove(0);
      unit_action_deletes << action.getId();
      
      if (!unit_update) {
        unit_update = unit_update_deltas.get(entry.getKey().toString());
        unit_action_update = unit_update.get("action");
        if (!unit_action_update) {
          unit_action_update = [:];
          unit_update.put("action", unit_action_update);
        }
        unit_action_update[action.getId().toString()] = "finished";
      }
    }
    
    if (unit_action_list.isEmpty()) {
      it.remove();
    }
  }
}

def scope_dao;
def unit_dao;
def unit_action_dao;

try {
  scope_dao = dao_registry.get(GalaxyScopeDAO.class);
  unit_dao = dao_registry.get(GalaxyUnitDAO.class);
  unit_action_dao = dao_registry.get(GalaxyUnitActionDAO.class);
  
  scope_dao.update(scope_id, scope_update);
  unit_dao.updateAll(unit_updates);
  
  for(def action_id : unit_action_deletes) {
    unit_action_dao.removeFromIdQuick(action_id);
  }
}
finally {
  if (scope_dao != null) {
    scope_dao.close();
  }
  if (unit_dao != null) {
    unit_dao.close();
  }
  if (unit_action_dao != null) {
    unit_action_dao.close();
  }
}

def update_delay = feature_config.getAsInteger("time_delay");
def update_apply_at = scope_data.scope.getLastUnitUpdate() + update_delay;
delta.put("update_time_at", update_apply_at);
delta.put("update_drop_key", feature_drop_key);

return delta;



def exec(def target, def logger, def timescale, def script_vars) {
  
  long last_update_time = target.scope.getLastUnitUpdate();
  target.scope.triggerLastUnitUpdate();
  long current_update_time = target.scope.getLastUnitUpdate();
  long systemtime_elapsed = (current_update_time - last_update_time);
  if (systemtime_elapsed < 1) {
    return null;
  }
  
  def scope_delta = new BasicDBObject();
  scope_delta.put(GalaxyScope.LAST_UNIT_UPDATE, 
                  target.scope.getLastUnitUpdate());
  def unit_delta = new BasicDBObject();
  
  def delta = new BasicDBObject();
  delta.put(ScopeData.SCOPE, scope_delta);
  delta.put(ScopeData.UNITS, unit_delta);
  
  double gamehours_elapsed = SpaceFunctionUtil.
      systemMSToGameHours(systemtime_elapsed) * timescale;
  
  // we do this to make sure that updates on units happen 
  // consistently. basically, if something happens to the scope
  // involving units while the update is happen (unit is created,
  // dies, enters the system, leaves the system), that the update 
  // wont have something undefined happen.
  def target_units;
  synchronized(target.units) {
    target_units = target.units.values().toArray();
  }
  for(def i = 0; i < target_units.length; i++) {
    def unit = target_units[i];
    if (logger.isDebugEnabled()) {
      logger.debug("updating unit ${unit.getName()} (id: ${unit.getId()})"+
          " for system ${target.scope.getName()} (id: ${target.scope.getId()})");
    }
    def unit_actions = target.unit_actions.get(unit.getId());
    if (!unit_actions) {
      continue;
    }
    
    double percent_time_spent = 0.0;
    for(def a = 0; a < unit_actions.size(); a++) {
      
      def unit_action = unit_actions[a];
      
      // if there is, then we want to know what type of
      // update script will have to be ran and then run it
      def update_type = unit_action.getActionType().name().toLowerCase();
      def script = "feature/unit/${update_type}.update.groovy";
      def update = GroovyWrapper.runScript(script, [
            feature_cache       : script_vars.feature_cache, 
            feature_config      : script_vars.feature_config,
            feature_drop_key    : script_vars.feature_drop_key,
            scope_id            : script_vars.scope_id,
            scope_data          : script_vars.scope_data,
            scope_cache         : script_vars.scope_cache,
            dao_registry        : script_vars.dao_registry,
            gamehours_elapsed   : gamehours_elapsed,
            systemtime_elapsed  : systemtime_elapsed,
            percent_time_spent  : percent_time_spent,
            current_update_time : current_update_time,
            last_update_time    : last_update_time,
            unit                : unit,
            action              : unit_action
          ]);

      if (update.unit_update) {
        unit_delta.put(unit.getId().toString(), update.unit_update);
        if (logger.isDebugEnabled()) {
          logger.debug("placed update for unit ${update_type} for unit"+
              " ${unit.getName()} (id: ${unit.getId()})");
        }
      }
      
      // check if the action is finished. if it is we will
      // need to remove the first action (because it is finished)
      // then make sure to start the next action. 
      // 
      // we want to start the next action because the actual transition 
      // from one action to another will most likely be between 2 updates
      // meaning that the next action would have started. also, some actions
      // may take less time then between the updates so we should keep 
      // performing updates until the actions are no longer finished or
      // until there are no more actions.
      // 
      // update scripts are required to keep track of the time required
      // to perform an action themselves.
      if (update.action_finished) {
        unit_action.markFinished();
        percent_time_spent += update.time_percent_spent;
        if (logger.isDebugEnabled()) {
          logger.debug("finished action ${update_type} for unit"+
              " ${unit.getName()} (id: ${unit.getId()})");
        }
      }
      else {
        break;
      }
      if (1.0 < percent_time_spent) {
        logger.warn("action ${update_type} caused 1.0 < percent_time_spent"+
            " for unit ${unit.getId()}: percent -> ${percent_time_spent}}");
        break;
      }
    }
  }
  
  return delta;
}

