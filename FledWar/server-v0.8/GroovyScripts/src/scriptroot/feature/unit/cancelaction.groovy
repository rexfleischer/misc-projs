
import org.bson.types.ObjectId;

if (!binding.variables.containsKey("scope")) {
  throw new Exception("unit must be specified");
}
if (!binding.variables.containsKey("unit_id")) {
  throw new Exception("unit must be specified");
}
if (!binding.variables.containsKey("action_id")) {
  throw new Exception("end_x must be specified");
}
if (!binding.variables.containsKey("user")) {
  throw new Exception("user must be specified");
}

def scope_id = new ObjectId(scope);
def simulation_scope = engine.getBlackboxSimulation().getScope(scope_id);
if (!simulation_scope) {
  throw new Exception("scope not found");
}

def scope_data = simulation_scope.getScopeData();

def actual_unit_id = new ObjectId(unit_id)
def unit = scope_data.units.get(actual_unit_id);
if (!unit) {
  throw new Exception("unit not found");
}

if (!user.getId().equals(unit.getOwnerId())) {
  throw new Exception("user can only command units they own");
}

def actual_action_id = new ObjectId(action_id);
def unit_action_list = scope_data.unit_actions.get(actual_unit_id);
def found = false;
if (unit_action_list) {
  synchronized(unit_action_list) {
    for(def action : unit_action_list) {
      if (action.getId().equals(actual_action_id)) {
        action.setCanceledTime(System.currentTimeMillis());
        found = true;
        break;
      }
    }
  }
}

if (found) {
  simulation_scope.broadcast([
    update_drop_key : "remove-action-${scope}",
    unit_id : unit_id,
    action_id : action_id
  ]);
  return [
    "canceled" : "action successfully canceled"
  ]
}
else {
  return [
    "canceled" : "action not found"
  ]
}
