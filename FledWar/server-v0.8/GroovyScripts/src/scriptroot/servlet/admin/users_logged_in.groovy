
import com.fledwar.dao.GameUserDAO;

def raw_results;
def users_dao = null;
try
{
    users_dao = engine.getDAOFactoryRegistry().get(GameUserDAO.class);
    
    raw_results = users_dao.getLoggedInUsers();
}
finally
{
    if (users_dao != null)
    {
        users_dao.close();
    }
}

def results = [];
def it = raw_results.iterator();
while(it.hasNext())
{
    def user = it.next();
    result = [:];
    result.username = user.getName();
    result.email = user.getEmail();
    result.user_state = user.getUserState();
    result.session_id = user.getSessionId().toString();
    result.session_time = user.getSessionTime();
    result.rights = user.getRights();
            
    results << result;
}

return results;
