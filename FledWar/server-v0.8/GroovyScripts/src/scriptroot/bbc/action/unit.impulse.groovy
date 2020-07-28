
/**
 * input vars:
 * - connection
 * - engine
 * - user
 * - action
**/

if (!action.scope || !action.unit_id) {
  throw new Exception("scope and unit_id must be set");
}

def end_x = false;
def end_y = false;
if (action.containsKey("end_x") && action.containsKey("end_y")) {
  end_x = action.end_x;
  end_y = action.end_y;
}
else if (action.containsKey("end_dist") && action.containsKey("end_alpha")) {
  end_x = (action.end_dist * Math.cos(action.end_alpha));
  end_y = (action.end_dist * Math.sin(action.end_alpha));
}

if (end_x == false || end_y == false) {
  throw new Exception("end location values not set")
}


return connection.command("feature/unit/impulse.commit.groovy", [
  scope : action.scope,
  unit_id :action.unit_id,
  end_x : end_x,
  end_y : end_y
]);
