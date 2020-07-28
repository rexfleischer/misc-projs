
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/generate/util.groovy", [:]);

import com.fledwar.vto.galaxy.system.PointPlanet;
import com.fledwar.vto.galaxy.system.PlanetType;
import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.Material;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.vto.galaxy.util.SpaceConstents;

PLANET_MASS_MIN = 1E21;
PLANET_MASS_MAX = 1E28;

PLANET_TOTAL_MATERIAL = 100;

GENERATE_MOON_SCRIPT = "random/generate/moon.groovy";

main_config = [
	[
		type : PlanetType.JOVIAN,
		max_mass : 1E28,
		min_mass : 1E26,
		max_radius : 85000000,
		min_radius : 50000000,
		radius_pow : 0.65,
		moons : [
			count_min : 4,
			count_max : 15,
			dist_min : 20,
			dist_max : 200,
			dist_pow : 3,
			max_mass_percent : 0.01,
			dalpha : {
				cmass, mmass, dist -> 1 / 
				(0.0005555555555555 * Math.PI * 
					Math.sqrt(
						Math.pow(dist, 3) / 
						(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
					))
			}
		],
		ring : [
			chance : 0.40,
			inner : 2.00,
			width : 0.25
		],
		material : [
			[
				type : Material.GAS_TYPE1,
				min : 50,
				max : 70
			],
			[
				type : Material.GAS_TYPE2,
				min : 30,
				max : 40
			],
			[
				type : Material.GAS_TYPE3,
				min : 20,
				max : 25
			],
		]
	],
	[
		type : PlanetType.OCEAN,
		max_mass : 1E28,
		min_mass : 1E24,
		max_radius : 55000000,
		min_radius : 20000000,
		radius_pow : 0.65,
		moons : [
			count_min : 2,
			count_max : 7,
			dist_min : 20,
			dist_max : 200,
			dist_pow : 3,
			max_mass_percent : 0.01,
			dalpha : {
				cmass, mmass, dist -> 1 / 
				(0.0005555555555555 * Math.PI * 
					Math.sqrt(
						Math.pow(dist, 3) / 
						(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
					))
			}
		],
		ring : [
			chance : 0.20,
			inner : 2.00,
			width : 0.25
		],
		material : [
			[
				type : Material.GAS_TYPE1,
				min : 50,
				max : 70
			],
			[
				type : Material.GAS_TYPE2,
				min : 30,
				max : 40
			],
			[
				type : Material.GAS_TYPE3,
				min : 20,
				max : 25
			],
		]
	],
	[
		type : PlanetType.CARBON,
		max_mass : 5E26,
		min_mass : 1E21,
		max_radius : 9000000,
		min_radius : 4000000,
		radius_pow : 0.65,
		moons : [
			count_min : 0,
			count_max : 2,
			dist_min : 20,
			dist_max : 200,
			dist_pow : 3,
			max_mass_percent : 0.01,
			dalpha : {
				cmass, mmass, dist -> 1 / 
				(0.0005555555555555 * Math.PI * 
					Math.sqrt(
						Math.pow(dist, 3) / 
						(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
					))
			}
		],
		ring : [
			chance : 0.05,
			inner : 2.00,
			width : 0.10
		],
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
		type : PlanetType.SILICATE,
		max_mass : 1E26,
		min_mass : 1E21,
		max_radius : 8500000,
		min_radius : 4000000,
		radius_pow : 0.65,
		moons : [
			count_min : -1,
			count_max : 2,
			dist_min : 30,
			dist_max : 200,
			dist_pow : 3,
			max_mass_percent : 0.01,
			dalpha : {
				cmass, mmass, dist -> 1 / 
				(0.0005555555555555 * Math.PI * 
					Math.sqrt(
						Math.pow(dist, 3) / 
						(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
					))
			}
		],
		ring : [
			chance : 0.00,
		],
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
		type : PlanetType.IRON,
		max_mass : 1E25,
		min_mass : 1E21,
		max_radius : 3500000,
		min_radius : 1500000,
		radius_pow : 0.65,
		moons : [
			count_min : -4,
			count_max : 1,
			dist_min : 30,
			dist_max : 200,
			dist_pow : 3,
			max_mass_percent : 0.01,
			dalpha : {
				cmass, mmass, dist -> 1 / 
				(0.0005555555555555 * Math.PI * 
					Math.sqrt(
						Math.pow(dist, 3) / 
						(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
					))
			}
		],
		ring : [
			chance : 0.00,
		],
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


def set_planet_ring(planet, type_config)
{
	def ring = planet.getPlanetRing();
	def ring_config = type_config.ring;
	def random = new Random();
	def chance = random.nextDouble();
	if (chance > ring_config.chance) 
	{
		return;
	}
	def base_radius = planet.getRadius();
	def inner = (base_radius * ring_config.inner);
	def inner_off = (inner * (random.nextDouble() - 0.5));
	inner += inner_off;
	def width = (base_radius * ring_config.width);
	def width_off = (width * (random.nextDouble() - 0.5));
	width += width_off;
	
	ring.setInnerDistance(base_radius + inner);
	ring.setOuterDistance(base_radius + inner + width);
}

def set_planet_moons(planet, container, type_config, 
          moon_name_format, max_moon_distance)
{
	// first, lets just see if we are even going make moon
	def random = new Random();
	def moon_config = type_config.moons;
	def moons = planet.getChildren();
	
	def moon_count_range = Math.abs(moon_config.count_max - moon_config.count_min);
	def moon_count = (random.nextInt(moon_count_range) + moon_config.count_min);
	if (moon_count <= 0)
	{
		return;
	}
	
	def max_moon_mass = planet.getMass() * moon_config.max_mass_percent;
	
	def dist_equation = Equation.power(
    moon_config.dist_min * planet.getRadius(), 
    moon_config.dist_max * planet.getRadius(), 
    moon_config.dist_pow);
	
	for(def i = 0; i < moon_count; i++)
	{
		def dist_min = ((i + 0.1) / moon_count);
		def dist_max = ((i + 0.9) / moon_count);
		def dist_percent = (dist_max - dist_min) * random.nextDouble() + dist_min;
		def moon_dist = dist_equation.find(dist_percent);
		if (max_moon_distance && max_moon_distance < moon_dist)
		{
			return;
		}
		
		def moon_name = String.format(moon_name_format, planet.getName(), i);
		def moon_params = [
			name 	: moon_name,
			max_mass: max_moon_mass
		];
		def moon = GroovyWrapper.runScript(
      GENERATE_MOON_SCRIPT, 
      moon_params);
    moon = moon[0];
		
		def dalpha = moon_config.dalpha(
      planet.getMass(), 
      moon.getMass(), 
      moon_dist);
		def alpha = (Math.PI * 2 * random.nextDouble());
		
		def orientation = moon.getObjectOrientation();
		orientation.setAlphaDistance(alpha, moon_dist);
		orientation.setDeltaAlpha(dalpha);
    
    
    planet.getChildren() << moon.getId();
		container << moon;
	}
}















// here is where the actual script starts

if (!binding.variables.containsKey("name"))
{
	throw new Exception("name must be specified");
}

def _mass;
if (binding.variables.containsKey("max_mass"))
{
	def percent = (new Random()).nextDouble();
	if (max_mass >= PLANET_MASS_MAX) 
	{
		max_mass = PLANET_MASS_MAX;
	}
	if (max_mass < 10 * PLANET_MASS_MIN)
	{
		max_mass = 10 * PLANET_MASS_MIN;
	}
	_mass = Equation.power(PLANET_MASS_MIN, max_mass, 6).find(percent);
}
else if (binding.variables.containsKey("mass"))
{
	_mass = mass;
}
else
{
	def percent = (new Random()).nextDouble();
	_mass = Equation.power(PLANET_MASS_MIN, PLANET_MASS_MAX, 6).find(percent);
}

def _moons;
if (binding.variables.containsKey("moons"))
{
	_moons = moons;
}
else
{
	_moons = true;
}

def _moon_name_format;
if (binding.variables.containsKey("moon_name_format"))
{
	_moon_name_format = moon_name_format;
}
else
{
	_moon_name_format = "%s-moon%s";
}

def _max_moon_distance;
if (binding.variables.containsKey("max_moon_distance"))
{
	_max_moon_distance = max_moon_distance;
}
else
{
	_max_moon_distance = false;
}




def planet = new PointPlanet();
planet.generateId();
planet.setMass(_mass);
planet.setName(name);

def type_config = util.get_type(_mass, main_config);
planet.setPlanetType(type_config.type);
planet.setHealth((byte) 100);
planet.setRadius(util.get_radius(_mass, type_config));
planet.getObjectOrientation();
util.set_material_map(planet, type_config.material, PLANET_TOTAL_MATERIAL);
set_planet_ring(planet, type_config);

def result = [];
result << planet;
if (_moons)
{
	set_planet_moons(planet, result, type_config, 
    _moon_name_format, _max_moon_distance);
}


return result;

