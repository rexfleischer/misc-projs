
import org.apache.log4j.Logger;

import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.vto.galaxy.unit.GalaxyUnit;
import com.fledwar.vto.galaxy.util.ObjectOrientation;

import com.fledwar.dao.GalaxyMicroUnitDAO;
import com.fledwar.vto.galaxy.unit.MicroUnit;
import com.fledwar.vto.galaxy.unit.MicroUnitInType;
import org.bson.types.ObjectId;
import com.fledwar.vto.galaxy.util.SpaceConstents;


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

def logger = Logger.getLogger(this.getClass());


if (!binding.variables.containsKey("owner_id")) {
  logger.error("owner_id must be specified");
	throw new Exception("owner_id must be specified");
}
if (!binding.variables.containsKey("name")) {
  logger.error("name must be specified");
	throw new Exception("name must be specified");
}
if (!binding.variables.containsKey("scope")) {
  logger.error("scope must be specified");
	throw new Exception("scope must be specified");
}
if (!binding.variables.containsKey("type")) {
  logger.error("type must be specified");
	throw new Exception("type must be specified");
}
if (!binding.variables.containsKey("location")) {
  logger.error("location must be specified");
	throw new Exception("location must be specified");
}
if (!binding.variables.containsKey("micro_units")) {
  logger.error("micro_units must be specified");
	throw new Exception("micro_units must be specified");
}


def new_unit = new GalaxyUnit();
new_unit.generateId();
new_unit.setOwnerId(owner_id);
new_unit.setName(name);
new_unit.setType(type);
new_unit.setScope(scope);


if (location instanceof ObjectOrientation) {
  new_unit.getOrientation().setObjectOrientation(location);
}
else {
  new_unit.getOrientation().setObjectOrientation(
    location.distance, 
    location.alpha, 
    location.dalpha, 
    location.id, 
    location.type);
}


def abilities = new_unit.getAbilities();
def attributes = new_unit.getAttributes();
/**
 * we turn this off right now, but abilities and attributes
 * will be gotten from the abilities and attributes of the
 * micro units. basically:
 * - abilities are accumulitive
 * - attributes at the weakest link
def micro_it = micro_units.iterator();
while(micro_it.hasNext()) {
  def micro = micro_it.next();
  
  
}
 * 
 * 
 * but for now we are going to hardcode abilities and attributes
**/
attributes.put("impulse_speed", 5);
attributes.put("warp_speed", 2);



def unit_dao = null;
def micro_unit_dao = null;
try {
  unit_dao = engine.getDAOFactoryRegistry()
      .getDAOFactory(GalaxyUnitDAO.class);
  micro_unit_dao = engine.getDAOFactoryRegistry()
      .getDAOFactory(GalaxyMicroUnitDAO.class);
        
  unit_dao.insert(new_unit);
  def new_unit_id = new_unit.getId();
    
  def micro_it = micro_units.iterator();
  while(micro_it.hasNext()) {
    micro_unit = micro_it.next();
    micro_unit.setUnitIn(MicroUnitInType.UNIT, new_unit_id);
    micro_unit_dao.update(micro_unit);
  }
}
finally {
  if (unit_dao != null) {
    unit_dao.close();
  }
  if (micro_unit_dao != null) {
    micro_unit_dao.close();
  }
}
