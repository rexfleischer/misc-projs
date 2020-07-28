

//INCLUDES = [
//	"./engine.groovy",
//	"./dao.groovy",
//	"./endpoint.groovy"
//];



//WEBSOCKET_URL = "gearwears.com/user_ws";
WEBSOCKET_URL = "75.126.81.222:25530/user_ws";

mongo = [
    //hosts : ["localhost:27027", "localhost:27028", "localhost:27029"],
	hosts : ["localhost:14871"],
	
	// url : "mongo://localhost:27017/?"
];

MONGO_DATABASE = "fledwar";


dao_registry = [
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_scope",
		clazz       : com.fledwar.dao.GalaxyScopeDAO,
	],
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_user",
		clazz       : com.fledwar.dao.GameUserDAO,
	],
];


engine = [

    balancer : com.fledwar.blackbox.balancer.DefaultGalaxyScopeBalancer,
	
	start_scripts : [
		"mgt/fledwar_init.groovy"
	],
	
	start_master_scripts : [
		"mgt/start_master.groovy"
	],
	
	shutdown_scripts : [
		
	],

    simulation : [
        pool_size : 4,
    ],

    features : [
        galaxy_scope : [
            clazz : com.fledwar.blackbox.feature.galaxy.GalaxySystemFeature,
            delay_init : 50,
            delay : 2000,
            
            galaxy_time_scale : 10,
			galaxy_time_delay : 100,
			orbiting_refresh : 10,
        ]
    ],

];

websocket = [
	active		: true,
	pool_size	: 2,
	delay_init 	: 50,
	delay 		: 2000,
	conn_init	: "bbc/init_connection.groovy",
];

servlets = [
	admin : [
		rights_format : "admin-%s",
		script_format : "servlet/admin/%s.groovy",
		secure : false
	],
	
	user : [
		rights_format : "user-%s",
		script_format : "servlet/user/%s.groovy",
		login_action  : "attempt_login"
	],
];



