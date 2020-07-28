
import com.fledwar.dao.GalaxyScopeDAO;

def results = [];
def scope_dao = null;
try {
	scope_dao = engine.dao_registry.getDAOFactory(GalaxyScopeDAO.class);
	
	def galaxy_scopes = scope_dao.findAll();
	
	for(def galaxy : galaxy_scopes) {
		results << galaxy.getDataAsMap();
	}
}
finally {
	if (scope_dao != null) {
		scope_dao.close();
	}
}

return results;
