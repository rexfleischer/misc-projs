
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.vto.galaxy.scope.GalaxyScope;

def results = [];
def scope_dao = null;
try
{
	scope_dao = engine.dao_registry.getDAOFactory(GalaxyScopeDAO.class)

	def center_dist;
	def center_alpha;
	if (query.scope_id && query.radius)
	{
    def query_id = (query.scope_id instanceof ObjectId) ?
            query.scope_id : new ObjectId(query.scope_id);
		def center_system = scope_dao.findOneFromId(query_id);
		def orientation = center_system.getGalaxyOrientation();
		center_dist = orientation.getDistance();
		center_alpha = orientation.getAlpha();
	}
	else if (query.radius && query.center_dist && query.center_alpha)
	{
		center_dist = query.center_dist;
		center_alpha = query.center_alpha;
	}
	else
	{
		throw new Exception("not all required fields are specified");
	}
	
	/*
	System.out.println("alpha: ${center_alpha}");
	System.out.println("dist: ${center_dist}");
	System.out.println("radius: ${query.radius}");
	*/
	
	def galaxy_scopes = scope_dao.getFromPoint(
			center_alpha,
			center_dist,
			query.radius,
			new BasicDBObject("rootpoint", 0));
	
	for(def galaxy : galaxy_scopes)
	{
		results << galaxy.getDataAsMap();
	}
}
finally
{
	if (scope_dao != null)
	{
		scope_dao.close();
	}
}

return results;
