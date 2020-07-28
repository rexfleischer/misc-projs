
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/system/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.scope.GalaxyScopeType;
import com.fledwar.vto.galaxy.util.SpaceConstents;


unary_config = [
	dist_min : SpaceConstents.AU / 3,
	dist_max_small : SpaceConstents.AU * 17,
	dist_max_big : SpaceConstents.AU * 40,
	
	count_min : 7,
	count_max : 12,
	count_pow : 0.65,
	
	mass_max : util.PLANET_MASS_MAX,
	mass_pow : 0.5,
	
];





// here is where the actual script starts

if (!binding.variables.containsKey("name"))
{
	throw new Exception("name must be specified");
}


def result = new ScopeData();
result.scope = new GalaxyScope();
result.scope.generateId();
result.scope.setName(name);
result.scope.setScopeType(GalaxyScopeType.USER);


def points = [];

def star_name = String.format("%s-star0", name);
def star_params = [
	name : star_name
];
def root_star = GroovyWrapper.runScript(
		util.star_script,
		star_params);
root_star = root_star[0];
root_star.setIsRoot(true);
points << root_star;

root_star.setScope(result.scope.getId());

util.fill_planets_config(root_star,
                         points, 
                         unary_config);
                       
result.setPoints(points);



return result;
