

dao_registry = [
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_scope",
		clazz       : com.fledwar.dao.GalaxyScopeDAO,
	],
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_point",
		clazz       : com.fledwar.dao.GalaxyPointDAO,
	],
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_unit",
		clazz       : com.fledwar.dao.GalaxyUnitDAO,
	],
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_unit_micro",
		clazz       : com.fledwar.dao.GalaxyMicroUnitDAO,
	],
	[
		database    : MONGO_DATABASE,
		collection  : "galaxy_unit_action",
		clazz       : com.fledwar.dao.GalaxyUnitActionDAO,
	],
	[
		database    : MONGO_DATABASE,
		collection  : "game_user",
		clazz       : com.fledwar.dao.GameUserDAO,
	],
];
