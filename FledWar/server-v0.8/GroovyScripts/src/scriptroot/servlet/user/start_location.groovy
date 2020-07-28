
// this script is meant to find out where 
// the use should connect...
//
// but, being this is not a clustered system
// yet, we know the user must connect to the
// current server.

import com.fledwar.server.FledWarServer;

def websocket_url = FledWarServer.getConfiguration()
			.getAsString("WEBSOCKET_URL")

return [
	"username" 	: user.getName(),
	"session_id": user.getSessionId().toString(),
	"websocket" : websocket_url,
];