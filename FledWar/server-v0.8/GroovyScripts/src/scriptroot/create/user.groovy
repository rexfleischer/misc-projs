
import com.fledwar.vto.user.GameUser;
import com.fledwar.vto.user.UserState;
import com.fledwar.vto.user.UserType;

if (!binding.variables.containsKey("username") ||
	!binding.variables.containsKey("password") ||
	!binding.variables.containsKey("type") ||
	!binding.variables.containsKey("email") ||
	!binding.variables.containsKey("dao"))
{
	throw new IllegalArgumentException(
			"username, password, type, email, and dao must be specified");
}

def new_user = new GameUser();
new_user.generateId();
new_user.setName(username);
new_user.setPassword(password);
new_user.setEmail(email);
new_user.setUserState(UserState.UNACTIVATED);


def user_rights = new_user.getRights();

if (type instanceof String)
{
	type = UserType.valueOf(type.toUpperCase());
}

// general game rights
user_rights << "focus-galaxy_scope";

// 
user_rights << "query-config";
user_rights << "query-system";
user_rights << "query-system_layout";
user_rights << "query-system_units";
user_rights << "query-userdata";

// actions that can be performed
user_rights << "action-end_focus";
user_rights << "action-ping";
user_rights << "action-set_session_var";
user_rights << "action-set_user_setting";
user_rights << "action-unit.impulse";
user_rights << "action-unit.cancelaction";

// user servlet rights
user_rights << "user-start_location";
user_rights << "user-attempt_logout";


if (type == UserType.ADMIN)
{
  user_rights << "admin-config";
	user_rights << "admin-refresh";
	user_rights << "admin-reset";
	user_rights << "admin-scope_list";
	user_rights << "admin-scope_view";
	user_rights << "admin-status";
  user_rights << "admin-users_logged_in";
  user_rights << "action-command";
}
else if (type == UserType.TESTER)
{
	user_rights << "focus-test";
	user_rights << "query-test";
	user_rights << "action-test";
}
else if (type == UserType.PLAYER)
{
	
}


// extra business logic specific user data
def user_vars = new_user.getUserSettings();
if (binding.variables.containsKey("start_system"))
{
	user_vars.append("start_system", start_system);
}

dao.insert(new_user);

return new_user;

