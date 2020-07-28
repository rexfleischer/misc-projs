
def user = connection.getUser();

return [
	"username" : user.getName(),
	"start_system" : user.getUserSettings().get("start_system"),
	"user_settings" : user.getUserSettings().getDataAsMap(),
	"session_vars" : user.getSessionVars().getDataAsMap(),
];
