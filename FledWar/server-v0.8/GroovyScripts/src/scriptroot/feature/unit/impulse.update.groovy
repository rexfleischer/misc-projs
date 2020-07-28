
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.vto.galaxy.unit.GalaxyUnit;
import com.fledwar.vto.galaxy.util.ObjectOrientation;
import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;

/**
 * the variables that are in this script are:
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
 */

def finished = false;
def time_spent = 0;

def logger = Logger.getLogger("scriptroot.feature.unit.impulse.update");


// if the action just started, then "start_time" may not
// be set, so we should set it if it isnt
long update_start_time = (last_update_time + (systemtime_elapsed * percent_time_spent));
if (!action.hasStarted()) {
  GroovyWrapper.runScript("feature/unit/impulse.start.groovy", [
    action : action,
    unit : unit,
    start_time : update_start_time
  ]);
  logger.info("started impulse action: ${action.toString()}");
}
long action_start_time = action.getStartTime();
if (update_start_time < action_start_time) {
  update_start_time = action_start_time;
}
long canceled_at = action.getCanceledTime();


long required_system_time = action.get("required_system_time");
def start_x = action.get("start_x");
def start_y = action.get("start_y");
def end_x = action.get("end_x");
def end_y = action.get("end_y");


def percent_finished;
def orientation = unit.getOrientation();
if (canceled_at != -1 && canceled_at <= current_update_time) {
  logger.info("impulse was canceled");
  // the action was canceled at some time during the elapsed time
  if (canceled_at > update_start_time) {
    time_spent = ((canceled_at - update_start_time) / systemtime_elapsed);
    percent_finished = ((canceled_at - action_start_time) / required_system_time);
  }
  finished = true;
}
else if ((action_start_time + required_system_time) <= current_update_time) {
  logger.info("impulse is finishing");
  // the action finished
  def finished_time = action_start_time + required_system_time;
  time_spent = ((finished_time - update_start_time) / systemtime_elapsed);
  percent_finished = 1.0;
  finished = true;
}
else {
  // we will use the full time avaliable
  logger.info("impulse is using all available time");
  time_spent = ((current_update_time - update_start_time) / 
    systemtime_elapsed);
  percent_finished = ((current_update_time - action_start_time) / 
    required_system_time);
  finished = false;
}

def unit_update = false;
if (percent_finished) {
  def new_x = (start_x + (end_x - start_x) * percent_finished);
  def new_y = (start_y + (end_y - start_y) * percent_finished);
  orientation.setXY(new_x, new_y);

  unit_update = new BasicDBObject();
  unit_update.put("${GalaxyUnit.ORIENTATION}.${ObjectOrientation.ALPHA}".toString(),
                  orientation.getAlpha());
  unit_update.put("${GalaxyUnit.ORIENTATION}.${ObjectOrientation.DISTANCE}".toString(),
                  orientation.getDistance());
}

if (logger.isDebugEnabled()) {
  logger.debug("finished: ${finished}");
  logger.debug("time_percent_spent: ${time_spent}");
  logger.debug("unit_update: ${unit_update}");
}
return [
  action_finished : finished,
  time_percent_spent : time_spent,
  unit_update : unit_update
];
