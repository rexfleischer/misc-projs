
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/generate/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.PointCloud;
import com.fledwar.vto.galaxy.system.Material;

CLOUD_MASS_MIN = 1E7;
CLOUD_MASS_MAX = 1E10;

CLOUD_TOTAL_MATERIAL = 120;

CLOUD_MATERIAL = [
	[
		type : Material.GAS_TYPE1,
		min : 5,
		max : 10
	],
	[
		type : Material.GAS_TYPE2,
		min : 10,
		max : 15
	],
	[
		type : Material.GAS_TYPE3,
		min : 15,
		max : 20
	],
	[
		type : Material.GAS_TYPE_RARE1,
		min : 15,
		max : 20
	],
	[
		type : Material.GAS_TYPE_RARE2,
		min : 15,
		max : 20
	],
	[
		type : Material.GAS_TYPE_RARE3,
		min : 15,
		max : 20
	],
	[
		type : Material.GAS_TYPE_RARE4,
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
	if (max_mass >= CLOUD_MASS_MAX) 
	{
		max_mass = CLOUD_MASS_MAX;
	}
	_mass = Equation.power(CLOUD_MASS_MIN, max_mass, 6).find(percent);
}
else if (binding.variables.containsKey("mass"))
{
	_mass = mass;
}
else
{
	def percent = (new Random()).nextDouble();
	_mass = Equation.power(
				CLOUD_MASS_MIN, 
				CLOUD_MASS_MAX, 6).find(percent);
}




def result = new PointCloud();
result.generateId();
result.setMass(_mass);
result.setName(name);
result.setHealth((byte) 100);

util.set_material_map(result, CLOUD_MATERIAL, CLOUD_TOTAL_MATERIAL);

return [ result ];
