
GLOBAL_TIMESCALE = 1000;

engine = [
  
	start_scripts : [
		"mgt/fledwar_init.groovy"
	],
	
	start_master_scripts : [
		"mgt/start_master.groovy"
	],
	
	create_user_script : "cmd/create_user.groovy",
	
	shutdown_scripts : [
		
	],
    
  connection : [
    validate_threashold : 10 * 1000, // 10 seconds
    session_timeout : 10 * 60 * 1000 // 10 minutes
  ],
  
  simulation : [
    pool_size : 4,
    
    balancer : com.fledwar.blackbox.balancer.DefaultGalaxyScopeBalancer,
    
    features : [
      galaxy_point : [
        feature_type        : "galaxy_point",
        feature_drop_format : "galaxy_point-%s",
        
        clazz : com.fledwar.blackbox.scope.BaseGroovyFeature,
        update_script : "feature/point/update.groovy",
        message_script: "feature/point/message.groovy",
        delay_init    : 50,
        delay         : 2000,

        time_scale : GLOBAL_TIMESCALE,
        time_delay : 100,
      ],
      galaxy_unit : [
        feature_type        : "galaxy_unit",
        feature_drop_format : "galaxy_unit-%s",
        
        clazz : com.fledwar.blackbox.scope.BaseGroovyFeature,
        update_script : "feature/unit/update.groovy",
        message_script: "feature/unit/message.groovy",
        delay_init    : 50,
        delay         : 2000,

        // update level configuration
        time_scale  : GLOBAL_TIMESCALE,
        time_delay  : 100,
      ]
    ],
  ],
    
];
