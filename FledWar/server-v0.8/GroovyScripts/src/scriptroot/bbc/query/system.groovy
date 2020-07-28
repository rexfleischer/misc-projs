
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GalaxyPointDAO;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GalaxyUnitActionDAO;
import com.fledwar.vto.galaxy.util.ScopeData;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;


// first we are need to figure out the actual query
def scope_id;
if (query.scope_id)
{
	if (query.scope_id instanceof ObjectId)
	{
		scope_id = query.scope_id;
	}
	else
	{
		scope_id = new ObjectId(query.scope_id);
	}
}
else
{
	throw new Exception("unable to handle query ${query}");
}

def scope = query.scope;
def points = query.points;
def units = query.units;
def unit_actions = query.unit_actions;
if (!scope && !points && !units && !unit_actions) {
  scope = true;
  points = true;
  units = true;
  unit_actions = true;
}


def result = new ScopeData();
def scope_dao;
def point_dao;
def unit_dao;
def unit_action_dao;
try {
  if (scope) {
    scope_dao = engine.dao_registry.get(GalaxyScopeDAO.class);
    result.scope = scope_dao.findOneFromId(scope_id);
  }
  if (points) {
    point_dao = engine.dao_registry.get(GalaxyPointDAO.class);
    result.setPoints(point_dao.findWithScope(scope_id));
  }
  if (units) {
    unit_dao = engine.dao_registry.get(GalaxyUnitDAO.class);
    result.setUnits(unit_dao.findWithScope(scope_id));
  }
  if (unit_actions) {
    unit_action_dao = engine.dao_registry.get(GalaxyUnitActionDAO.class);
    result.setUnitActions(unit_action_dao.findWithScope(scope_id));
  }
}
finally {
	if (scope_dao != null) {
		scope_dao.close();
	}
	if (point_dao != null) {
		point_dao.close();
	}
	if (unit_dao != null) {
		unit_dao.close();
	}
  if (unit_action_dao != null) {
    unit_action_dao.close();
  }
}


return result.toMap();
