
import com.fledwar.vto.galaxy.util.SpaceConstents;
import org.apache.log4j.Logger;
def logger = Logger.getLogger(this.getClass());

if (!binding.variables.containsKey("user"))
{
    logger.error("user must be specified");
	throw new Exception("user must be specified");
}

def user_id = user.getId();
def user_name = user.getName();
def user_scope = user.getUserSettings().get("start_system");

engine.command("create/unit.groovy", [
  owner_id : user_id,
  name : "${user_name}-unit-1",
  type : "stuff1",
  scope : user_scope,
  location : [
    distance : SpaceConstents.AU * 100,
    alpha : (Math.PI / 2),
    dalpha : 0,
    id : null,
    type : null
  ],
  abilities : [:],
  micro_units : [:]
]);
