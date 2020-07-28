
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GalaxyPointDAO;
import com.fledwar.dao.GalaxyUnitDAO;
import com.fledwar.vto.galaxy.util.ScopeData;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;

//return query.scope_id;

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
		scope_id = new ObjectId(query.scope_id[0]);
	}
}
else
{
	throw new Exception("unable to handle query ${query}");
}


return ScopeData.get(engine.dao_registry, scope_id).toMap();
