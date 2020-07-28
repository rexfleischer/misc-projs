
import com.fledwar.dao.GameUserDAO;
import com.fledwar.blackbox.BlackboxRightsException;


if (query.username == null ||
    query.password == null)
{
	throw new BlackboxRightsException(
			"unable to find all user data to login");
}

def session_id = engine.userLogin(
		query.username[0], 
		query.password[0]);

if (session_id == null)
{
	throw new BlackboxRightsException(
			"unable to login user");
}

def user = null;
def user_dao = null;
try
{
    user_dao = engine.getDAOFactoryRegistry().get(GameUserDAO.class);
    user = user_dao.fetchSessionedUser(query.username[0], session_id);
}
finally
{
    if (user_dao != null)
    {
        user_dao.close();
    }
}

return [
    username : user.getName(),
    email : user.getEmail(),
    user_state : user.getUserState(),
    rights : user.getRights(),
    session_id : user.getSessionId().toString(),
    messages : user.getMessages(),
    user_settings : user.getUserSettings().getDataAsMap(),
    session_vars : user.getSessionVars().getDataAsMap()
];
