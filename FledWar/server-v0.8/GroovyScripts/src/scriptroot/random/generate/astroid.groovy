
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/generate/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.PointAstroid;
import com.fledwar.vto.galaxy.system.Material;

ASTROID_MASS_MIN = 1E9;
ASTROID_MASS_MAX = 1E13;

ASTROID_TOTAL_MATERIAL = 120;

ASTROID_MATERIAL = [
	[
		type : Material.ORE_TYPE1,
		min : 5,
		max : 10
	],
	[
		type : Material.ORE_TYPE2,
		min : 10,
		max : 15
	],
	[
		type : Material.ORE_TYPE3,
		min : 15,
		max : 20
	],
	[
		type : Material.ORE_TYPE4,
		min : 20,
		max : 25
	],
	[
		type : Material.ORE_TYPE_RARE1,
		min : 15,
		max : 20
	],
	[
		type : Material.ORE_TYPE_RARE2,
		min : 15,
		max : 20
	],
	[
		type : Material.ORE_TYPE_RARE3,
		min : 15,
		max : 20
	],
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
	if (max_mass >= ASTROID_MASS_MAX) 
	{
		max_mass = ASTROID_MASS_MAX;
	}
	_mass = Equation.power(ASTROID_MASS_MIN, max_mass, 6).find(percent);
}
else if (binding.variables.containsKey("mass"))
{
	_mass = mass;
}
else
{
	def percent = (new Random()).nextDouble();
	_mass = Equation.power(
				ASTROID_MASS_MIN, 
				ASTROID_MASS_MAX, 6).find(percent);
}




def result = new PointAstroid();
result.generateId();
result.setMass(_mass);
result.setName(name);
result.setHealth((byte) 100);

util.set_material_map(result, ASTROID_MATERIAL, ASTROID_TOTAL_MATERIAL);


return [ result ];
