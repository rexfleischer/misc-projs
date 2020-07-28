

INCLUDES = [
	"./engine.groovy",
	"./dao.groovy",
	"./endpoint.groovy"
];

WEBSOCKET_URL = "localhost:8080/fledwarsvc/user_ws";

mongo = [
    //hosts : ["localhost:27027", "localhost:27028", "localhost:27029"],
	hosts : ["localhost:27017"],
	
	// url : "mongo://localhost:27017/?"
];

MONGO_DATABASE = "fledwar";


