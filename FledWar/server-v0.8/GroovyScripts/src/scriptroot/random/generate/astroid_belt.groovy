
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/generate/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.PointAstroidBelt;
import com.fledwar.vto.galaxy.system.Material;

ASTROID_BELT_MASS_MIN = 1E21;
ASTROID_BELT_MASS_MAX = 1E28;

ASTROID_BELT_TOTAL_MATERIAL = 120;

ASTROID_BELT_MATERIAL = [
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
	if (max_mass >= ASTROID_BELT_MASS_MAX) 
	{
		max_mass = ASTROID_BELT_MASS_MAX;
	}
	_mass = Equation.power(ASTROID_BELT_MASS_MIN, max_mass, 6).find(percent);
}
else if (binding.variables.containsKey("mass"))
{
	_mass = mass;
}
else
{
	def percent = (new Random()).nextDouble();
	_mass = Equation.power(
				ASTROID_BELT_MASS_MIN, 
				ASTROID_BELT_MASS_MAX, 6).find(percent);
}




def result = new PointAstroidBelt();
result.generateId();
result.setMass(_mass);
result.setName(name);

util.set_material_map(result, ASTROID_BELT_MATERIAL, ASTROID_BELT_TOTAL_MATERIAL);

return [ result ];
