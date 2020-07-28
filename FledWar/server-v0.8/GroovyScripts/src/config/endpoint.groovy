
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
