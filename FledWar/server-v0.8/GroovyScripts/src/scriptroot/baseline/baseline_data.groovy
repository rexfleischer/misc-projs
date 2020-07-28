
import com.mongodb.BasicDBObject;
import com.fledwar.dao.GameUserDAO;
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GalaxyPointDAO;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.dao.GalaxyUnitActionDAO;
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.UserState;
import com.fledwar.vto.galaxy.util.SpaceConstents;


def user_dao;
def scope_dao;
def point_dao;
def unit_dao;
def unit_action_dao;
// we want to wrap everything we are about to
// do in a try statement to make sure that
// there is no memory leak with the daos
try
{
	
	user_dao = engine.dao_registry.get(GameUserDAO.class);
	def user_collection = user_dao.getCollection();
	user_collection.ensureIndex(
		["name" : 1] as BasicDBObject,
		[unique : 1] as BasicDBObject
	);
	user_collection.ensureIndex(
		["email" : 1] as BasicDBObject,
		[unique : 1] as BasicDBObject
	);
    
	
	scope_dao = engine.dao_registry.get(GalaxyScopeDAO.class);
	def scope_collection = scope_dao.getCollection();
	scope_collection.ensureIndex(
		["orientation.posx" : 1] as BasicDBObject,
	);
	scope_collection.ensureIndex(
		["orientation.posy" : 1] as BasicDBObject,
	);
  
  
  point_dao = engine.dao_registry.get(GalaxyPointDAO.class);
  def point_collection = point_dao.getCollection();
	scope_collection.ensureIndex(
		["scope" : 1] as BasicDBObject,
	);
    
        
  unit_dao = engine.dao_registry.get(GalaxyUnitDAO.class);
	def unit_collection = unit_dao.getCollection();
  unit_collection.ensureIndex(
      ["scope" : 1] as BasicDBObject
  );
  unit_collection.ensureIndex(
      ["owner_id" : 1] as BasicDBObject
  );
  
	
  unit_action_dao = engine.dao_registry.get(GalaxyUnitActionDAO.class);
	def unit_action_collection = unit_action_dao.getCollection();
  unit_action_collection.ensureIndex(
      ["scope" : 1] as BasicDBObject
  );
  unit_action_collection.ensureIndex(
      ["unit_id" : 1] as BasicDBObject
  );
	
  
	// now we're going to build the cluster
	def params = [
		name				: "cluster",
		center_distance		: SpaceConstents.LIGHTYEAR * 10000,
		center_alpha		: Math.PI, 
		cluster_dalpha		: 0.000000000001,
		cluster_radius		: SpaceConstents.LIGHTYEAR * 10,
		cluster_seperation	: SpaceConstents.LIGHTYEAR * 3,
	];
	def cluster = engine.command("random/cluster/simple.groovy", params);
	for(def system : cluster)
	{
		System.out.println(system.scope.getName());
		scope_dao.insert(system.scope);
    if (system.points) {
      point_dao.insertAll(system.points.values());
    }
	}
	
	
	
	// this is the default starting system for all users
	def starting_system = cluster.get(2).scope.getId();
    
	def rex_params = [
		username : "rexf",
		password : "testing",
		type : "admin",
		email : "rex@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	def rex_user = engine.command("create/user.groovy", rex_params);
    
  def rex_unit_params = [
      user : rex_user,
  ];
  engine.command("baseline/baseline_user_units.groovy", rex_unit_params);
  
  
	
	def mike_params = [
		username : "mike",
		password : "testing",
		type : "player",
		email : "mike@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", mike_params);
	
	def jason_params = [
		username : "jason",
		password : "testing",
		type : "player",
		email : "jason@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", jason_params);
	
	def test_params = [
		username : "testing",
		password : "testing",
		type : "tester",
		email : "testing@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", test_params);
	
	def test1_params = [
		username : "testing1",
		password : "testing",
		type : "tester",
		email : "testing1@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", test1_params);
	
	def test2_params = [
		username : "testing2",
		password : "testing",
		type : "tester",
		email : "testing2@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", test2_params);
	
	def test3_params = [
		username : "testing3",
		password : "testing",
		type : "tester",
		email : "testing3@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", test3_params);
	
	def test4_params = [
		username : "testing4",
		password : "testing",
		type : "tester",
		email : "testing4@no-place.com",
		dao : user_dao,
		start_system : starting_system
	];
	engine.command("create/user.groovy", test4_params);
    
}
finally
{
	if (user_dao != null)
	{
		user_dao.close();
	}
	if (unit_dao != null)
	{
		unit_dao.close();
	}
	if (scope_dao != null)
	{
		scope_dao.close();
	}
	if (point_dao != null)
	{
		point_dao.close();
	}
}

return null;