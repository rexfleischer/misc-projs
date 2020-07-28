
import com.fledwar.vto.galaxy.unit.UnitAction;
import com.fledwar.vto.galaxy.unit.UnitActionType;
import com.fledwar.dao.GalaxyUnitActionDAO;
import org.bson.types.ObjectId

if (!binding.variables.containsKey("scope")) {
  throw new Exception("unit must be specified");
}
if (!binding.variables.containsKey("unit_id")) {
  throw new Exception("unit must be specified");
}
if (!binding.variables.containsKey("end_x")) {
  throw new Exception("end_x must be specified");
}
if (!binding.variables.containsKey("end_y")) {
  throw new Exception("end_y must be specified");
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

def new_action = new UnitAction();
new_action.generateId();
new_action.setActionType(UnitActionType.IMPULSE);
new_action.setScope(scope_id);
new_action.setUnitId(actual_unit_id);
new_action.put("end_x", end_x);
new_action.put("end_y", end_y);

if (binding.variables.containsKey("impulse_used")) {
  new_action.put("impulse_used", impulse_used);
}
else {
  new_action.put("impulse_used", unit.attributes.impulse_speed);
}

def unit_action_dao;
try {
  unit_action_dao = engine.dao_registry.get(GalaxyUnitActionDAO.class);
  unit_action_dao.insert(new_action);
}
finally {
  if (unit_action_dao != null) {
    unit_action_dao.close();
  }
}

def unit_actions;
synchronized(scope_data.unit_actions) {
  unit_actions = scope_data.unit_actions.get(actual_unit_id);
  if (!unit_actions) {
    unit_actions = [];
    scope_data.unit_actions.put(actual_unit_id, unit_actions);
  }
}
synchronized(unit_actions) {
  if (unit_actions.isEmpty()) {
    connection.command("feature/unit/impulse.start.groovy", [
      action : new_action,
      unit : unit,
      start_time : System.currentTimeMillis()
    ])
  }
  unit_actions.add(new_action);
}

return new_action.getDataAsMap();
