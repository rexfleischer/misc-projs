
import com.fledwar.dao.GameUserDAO;


if (!action.key || !action.value)
{
	throw new Exception("key and value must be specified");
}

switch(action.key)
{
	case "start_system":
		break;
		
	default:
		throw new Exception(
			"key ${action.key} is not allowed to be set");
}

if (action.value.toString().length() > 100) 
{
	throw new Exception(
		"value ${action.value} for ${action.key} is too large");
}


def user = connection.getUser();
user.getUserSettings().put(action.key, action.value);

def user_dao;
try
{
	user_dao = engine.dao_registry.getDAOFactory(GameUserDAO.class);
	
	user_dao.update(user);
}
finally
{
	if (user_dao != null)
	{
		user_dao.close();
	}
}

return [:];