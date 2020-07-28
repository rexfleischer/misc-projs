
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/system/util.groovy", [:]);

import com.fledwar.util.Equation;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.scope.GalaxyScopeType;
import com.fledwar.vto.galaxy.system.PointOrbitMarker;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.util.SpaceFunctionUtil;
import com.fledwar.vto.galaxy.util.SpaceConstents;



// here is where the actual script starts

if (!binding.variables.containsKey("name"))
{
	throw new Exception("name must be specified");
}

def random = new Random();

def result = new ScopeData();
result.scope = new GalaxyScope();
result.scope.generateId();
result.scope.setName(name);
result.scope.setScopeType(GalaxyScopeType.USER);


def points = [];


// first, lets build the two stars
def star_bigs = GroovyWrapper.runScript(
		util.star_script, [ name : "${name}-star0" ]);
star_bigs = star_bigs[0];
star_bigs.setScope(result.scope.getId());
def star_smalls = GroovyWrapper.runScript(
		util.star_script, [ name : "${name}-star1" ]);
star_smalls = star_smalls[0];
star_smalls.setScope(result.scope.getId());
def system_root = new PointOrbitMarker();
system_root.generateId();
system_root.setIsRoot(true);
system_root.setScope(result.scope.getId());
system_root.setName(name + "-center");
system_root.setMass(star_bigs.getMass() + star_smalls.getMass());
system_root.getChildren() << star_bigs.getId();
system_root.getChildren() << star_smalls.getId();

points << system_root;
points << star_bigs;
points << star_smalls;

// we assume that two stars orbiting each other are within
// 3 AU (very close) to 20 AU 
def orbit_dist = 17 * random.nextDouble() * 
					SpaceConstents.AU + (3.0 * SpaceConstents.AU);
def orbit_com = SpaceFunctionUtil.centerOfMassBetweenTwoObjects(
			star_bigs.getMass(), 
			star_smalls.getMass(), 
			orbit_dist);
def orbit_dalpha = 1 / SpaceFunctionUtil.peroidOfOrbit(
			star_bigs.getMass(), 
			star_smalls.getMass(), 
			orbit_dist);
def orbit_alpha = (random.nextDouble() * 2.0 * Math.PI);
def bigs_orbit = star_bigs.getObjectOrientation();
bigs_orbit.setAlphaDistance(orbit_alpha, orbit_com);
bigs_orbit.setDeltaAlpha(orbit_dalpha);
def smalls_orbit = star_smalls.getObjectOrientation();
smalls_orbit.setAlphaDistance((orbit_alpha + Math.PI) % (2 * Math.PI), 
                              orbit_dist - orbit_com);
smalls_orbit.setDeltaAlpha(orbit_dalpha);


//System.out.println(orbit_dist/SpaceConstents.AU);


// set up min distance for outer orbit

// if there are orbits directly around the inner
// stars, then put them together here
if (orbit_dist > 10 * SpaceConstents.AU)
{
	def bigs_max = star_bigs.getObjectOrientation().getDistance();
	def smalls_max = star_smalls.getObjectOrientation().getDistance();
	if (bigs_max > 3 * SpaceConstents.AU)
	{
		util.fill_planets_config(star_bigs, points, [
			dist_min : SpaceConstents.AU / 3,
			dist_max_small : bigs_max * 0.5,
			dist_max_big : bigs_max,
			
			count_min : 1,
			count_max : 3,
			count_pow : 0.65,
			
			mass_pow : 0.5,
			mass_max : util.PLANET_MASS_MIN * 100,
		]);
	}
	
	if (smalls_max > 3 * SpaceConstents.AU)
	{
		util.fill_planets_config(star_smalls, points, [
			dist_min : SpaceConstents.AU / 3,
			dist_max_small : smalls_max * 0.5,
			dist_max_big : smalls_max,
			
			count_min : 1,
			count_max : 3,
			count_pow : 0.65,
			
			mass_pow : 0.5,
			mass_max : util.PLANET_MASS_MIN * 100,
		]);
	}
}


def min_main_orbit_dist;
if (bigs_orbit.getDistance() > smalls_orbit.getDistance()) {
	min_main_orbit_dist = bigs_orbit.getDistance();
	
	def children = star_bigs.getChildren();
	if (!children.isEmpty()) {
    // find the largest distance
    def child;
    for(def point : points) {
      if (children.contains(point.getId())) {
        if (child == null) {
          child = point;
        }
        else if (child.getObjectOrientation().getDistance() < 
            point.getObjectOrientation().getDistance()) {
          child = point
        }
      }
    }
		min_main_orbit_dist += (child.getObjectOrientation().getDistance() * 2);
	}
	else {
		min_main_orbit_dist += (star_bigs.getRadius() * 50);
	}
}
else
{
	min_main_orbit_dist = smalls_orbit.getDistance();
	
	def children = star_smalls.getChildren();
	if (!children.isEmpty())
	{
    def child;
    for(def point : points) {
      if (children.contains(point.getId())) {
        if (child == null) {
          child = point;
        }
        else if (child.getObjectOrientation().getDistance() < 
            point.getObjectOrientation().getDistance()) {
          child = point
        }
      }
    }
		min_main_orbit_dist += (child.getObjectOrientation().getDistance() * 2);
	}
	else
	{
		min_main_orbit_dist += (star_smalls.getRadius() * 50);
	}
}


util.fill_planets_config(system_root, points, [
	dist_min : min_main_orbit_dist,
	dist_max_small : SpaceConstents.AU * 35,
	dist_max_big : SpaceConstents.AU * 80,
	
	count_min : 9,
	count_max : 20,
	count_pow : 0.65,
	
	mass_max : util.PLANET_MASS_MAX,
	mass_pow : 0.5,
]);

result.setPoints(points);


return result;
