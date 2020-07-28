
import com.fledwar.vto.galaxy.util.SpaceFunctionUtil;
import org.apache.log4j.Logger;

if (!binding.variables.containsKey("start_time")) {
  throw new Exception("start_time required");
}
if (!binding.variables.containsKey("action")) {
  throw new Exception("action required");
}
if (!binding.variables.containsKey("unit")) {
  throw new Exception("unit required");
}

action.setStartTime((long)start_time);

def unit_location = unit.getOrientation();
action.put("start_x", unit_location.getPosX());
action.put("start_y", unit_location.getPosY());

def dx = (action.get("start_x") - action.get("end_x"));
def dy = (action.get("start_y") - action.get("end_y"));
def distance = Math.sqrt(dx*dx + dy*dy);

def impulse_factor = unit.getAttributes().get("impulse_speed");
def unit_speed = SpaceFunctionUtil.impulseFactorToSpeed(impulse_factor);
def required_game_time = (distance / unit_speed);
long required_system_time = SpaceFunctionUtil.
    gameHoursToSystemMS(required_game_time);

action.put("required_system_time", required_system_time);

def simulation_scope = engine.getBlackboxSimulation().getScope(
  action.getScope());
simulation_scope.broadcast([
  update_drop_key : "new-action-${simulation_scope.getScopeId()}".toString(),
  action : action.getDataAsMap()
]);
