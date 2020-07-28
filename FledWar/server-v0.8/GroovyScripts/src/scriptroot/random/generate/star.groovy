
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/generate/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.system.PointStar;

STAR_MASS_MIN = 5.967E29;
STAR_MASS_MAX = 2E34;


star_config = [
	[
		max_mass    : 2E34,
		min_mass    : 5.967E29,
		max_radius  : 26883537119,
		min_radius  : 316861199,
		radius_pow  : 0.65,
		luminosity  : 1.0,
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
	if (max_mass >= STAR_MASS_MAX) 
	{
		max_mass = STAR_MASS_MAX;
	}
	_mass = Equation.power(STAR_MASS_MIN, max_mass, 6).find(percent);
}
else if (binding.variables.containsKey("mass"))
{
	_mass = mass;
}
else
{
	def percent = (new Random()).nextDouble();
	_mass = Equation.power(STAR_MASS_MIN, STAR_MASS_MAX, 6).find(percent);
}




def type_config = util.get_type(_mass, star_config);

def result = new PointStar();
result.generateId();
result.setMass(_mass);
result.setName(name);

result.setRadius(util.get_radius(_mass, type_config));
result.setLuminosity(_mass * type_config.luminosity);

return [ result ];
