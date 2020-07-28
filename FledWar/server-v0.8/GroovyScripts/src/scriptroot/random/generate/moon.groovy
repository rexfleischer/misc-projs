
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/generate/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.Material;
import com.fledwar.vto.galaxy.system.PointMoon;
import com.fledwar.vto.galaxy.system.PlanetMoonType;
import com.fledwar.vto.galaxy.util.SpaceConstents;

MOON_MASS_MIN = 1E19;
MOON_MASS_MAX = 1E24;

MOON_TOTAL_MATERIAL = 90;

main_config = [
	[
		type : PlanetMoonType.CARBON,
		max_mass : 1E24,
		min_mass : 1E21,
		max_radius : 3000000,
		min_radius : 1300000,
		radius_pow : 0.65,
		material : [
			[
				type : Material.ORE_TYPE1,
				min : 50,
				max : 70
			],
			[
				type : Material.ORE_TYPE2,
				min : 30,
				max : 40
			],
			[
				type : Material.ORE_TYPE3,
				min : 20,
				max : 25
			],
			[
				type : Material.ORE_TYPE4,
				min : 10,
				max : 15
			],
		]
	],
	[
		type : PlanetMoonType.SILICATE,
		max_mass : 1E24,
		min_mass : 1E20,
		max_radius : 3000000,
		min_radius : 1250000,
		radius_pow : 0.65,
		material : [
			[
				type : Material.ORE_TYPE1,
				min : 50,
				max : 70
			],
			[
				type : Material.ORE_TYPE2,
				min : 30,
				max : 40
			],
			[
				type : Material.ORE_TYPE3,
				min : 20,
				max : 25
			],
			[
				type : Material.ORE_TYPE4,
				min : 10,
				max : 15
			],
		]
	],
	[
		type : PlanetMoonType.IRON,
		max_mass : 1E24,
		min_mass : 1E19,
		max_radius : 3000000,
		min_radius : 1200000,
		radius_pow : 0.65,
		material : [
			[
				type : Material.ORE_TYPE1,
				min : 50,
				max : 70
			],
			[
				type : Material.ORE_TYPE2,
				min : 30,
				max : 40
			],
			[
				type : Material.ORE_TYPE3,
				min : 20,
				max : 25
			],
			[
				type : Material.ORE_TYPE4,
				min : 10,
				max : 15
			],
		]
	]
];








// here is where the actual script starts

if (!binding.variables.containsKey("name"))
{
	throw new Exception("name must be specified");
}

def _mass;
if (binding.variables.containsKey("max_mass"))
{
	def percent = (new Random()).nextDouble();
	if (max_mass >= MOON_MASS_MAX) 
	{
		max_mass = MOON_MASS_MAX;
	}
	_mass = Equation.power(MOON_MASS_MIN, max_mass, 6).find(percent);
}
else if (binding.variables.containsKey("mass"))
{
	_mass = mass;
}
else
{
	def percent = (new Random()).nextDouble();
	_mass = Equation.power(MOON_MASS_MIN, MOON_MASS_MAX, 6).find(percent);
}



def result = new PointMoon();
result.generateId();
result.setMass(_mass);
result.setName(name);

def type_config = util.get_type(_mass, main_config);
result.setPlanetMoonType(type_config.type);
result.setHealth((byte) 100);
result.setRadius(util.get_radius(_mass, type_config));
util.set_material_map(result, type_config.material, MOON_TOTAL_MATERIAL);

return [ result ];
