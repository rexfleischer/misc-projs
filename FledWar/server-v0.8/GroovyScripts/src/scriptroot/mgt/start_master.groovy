
import com.mongodb.BasicDBObject;
import com.fledwar.dao.GalaxyScopeDAO;
import com.fledwar.dao.GameUserDAO;


def user_dao;
def system_dao;
// we want to wrap everything we are about to
// do in a try statement to make sure that
// there is no memory leak with the daos
try
{
	
	
	user_dao = engine.getDAOFactoryRegistry()
        .getDAOFactory(GameUserDAO.class);
	system_dao = engine.getDAOFactoryRegistry()
        .getDAOFactory(GalaxyScopeDAO.class);

	
	
	
	def user_collection = user_dao.getCollection();
	user_collection.update(
			new BasicDBObject(),
			new BasicDBObject().append(
				'$set', new BasicDBObject("session_id", null)
			), false, true);
	user_collection.update(
			new BasicDBObject(),
			new BasicDBObject().append(
				'$set', new BasicDBObject("session_time", -1)
			), false, true);
	
	
	def system_collection = system_dao.getCollection();
	
			
			
}
finally
{
	if (user_dao != null)
	{
		user_dao.close();
	}
	if (system_dao != null)
	{
		system_dao.close();
	}
}

