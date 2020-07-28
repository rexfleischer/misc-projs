
return [

	get_type : { mass, config ->

		def choosing = [];
		for(def i = 0; i < config.size(); i++) 
		{
			def check = config[i];
			if (check.min_mass <= mass && mass <= check.max_mass) 
			{
				choosing << check;
			}
		}
		
		if (choosing.isEmpty())
		{
			throw new Exception("mass ${mass} is not in range of any moon config");
		}
		if (choosing.size() == 1)
		{
			return choosing[0];
		}
		
		def choice = (new Random()).nextInt(choosing.size());
		return choosing[choice];
	},

	get_radius : { mass, type_config ->

		def percent = (mass - type_config.min_mass) / 
				(type_config.max_mass - type_config.min_mass)
		return (int) com.fledwar.util.Equation.power(
					type_config.min_radius, 
					type_config.max_radius,
					type_config.radius_pow).find(percent);
	},

	set_material_map : { point, material_config, total_allowed ->
	
		def random = new Random();
		def total = 0;
		def material = point.getMaterialMap();
		for(def i = 0; i < material_config.size() && total < total_allowed; i++)
		{
			def part = material_config[i];
			def amount = random.nextInt(part.max - part.min) + part.min;
			material.setMaterialAmount(part.type.name(), (byte) amount);
			total += amount;
		}
	}
];