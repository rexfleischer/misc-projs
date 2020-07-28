
//import com.fledwar.vto.system.GalaxySystemState;
import com.fledwar.groovy.GroovyWrapper;
import com.fledwar.vto.galaxy.util.SpaceConstents;
import com.fledwar.util.Equation;

planet_choice_config = [
	[
		chance_min : 0,
		chance_max : 85,
		script : "random/generate/planet.groovy",
		name : { base, count -> return "${base}-planet${count}" },
		dalpha : {
			cmass, mmass, dist -> 1 / 
			(0.0005555555555555 * Math.PI * 
				Math.sqrt(
					Math.pow(dist, 3) / 
					(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
				))
		}
	],
	[
		chance_min : 86,
		chance_max : 100,
		script : "random/generate/astroid_belt.groovy",
		name : { base, count -> return "${base}-belt${count}" },
		dalpha : {
			cmass, mmass, dist -> 1 / 
			(0.0005555555555555 * Math.PI * 
				Math.sqrt(
					Math.pow(dist, 3) / 
					(SpaceConstents.GRAVITATIONAL_CONSTANT * (cmass + mmass))
				))
		}
	],
];


_util = [
	PLANET_MASS_MIN : 1E21,
	PLANET_MASS_MAX : 1E28,
	star_script : "random/generate/star.groovy",
	
	choose_fill_point : { random -> 
		def percent = random.nextInt(100);
		for(def i = 0; i < planet_choice_config.size(); i++) 
		{
			def check = planet_choice_config[i];
			if (check.chance_min <= percent && percent <= check.chance_max) 
			{
				return check;
			}
		}
		throw new Exception("unable to find point config");
	},
	
		
	fill_planets_config :  { parent, 
                           container, 
                           orbit_config ->
		//[
		//	dist_min : dist_min,
		//	dist_max_small : dist_max_small,
		//	dist_max_big : dist_max_big,
		//	count_min : count_min,
		//	count_max : count_max,
		//	count_pow : count_pow,
		//	mass_max : mass_max,
		// 	mass_pow : mass_pow,
		//]
		
		def random = new Random();
		
		
		def max_dist = Equation.power(orbit_config.dist_max_big,
									  orbit_config.dist_max_small,
									  4).find(random.nextDouble());
		def dist_percent = (max_dist - orbit_config.dist_max_small) /
					(orbit_config.dist_max_big - orbit_config.dist_max_small);
		def dist_equation = Equation.power(orbit_config.dist_min,
										   max_dist,
										   0.65);
		
		def count_percent = (random.nextDouble() * dist_percent);
		def orbit_count = Equation.power(orbit_config.count_min,
										 orbit_config.count_max,
										 orbit_config.count_pow).find(count_percent);
		
		def mass_max_equation = Equation.power(
				0, orbit_config.mass_max, orbit_config.mass_pow);
		
		_util.fill_planets(parent,
                       container, 
                       (int) orbit_count,
                       dist_equation,
                       mass_max_equation);
	},
	
	fill_planets : { parent, container, point_count, 
                            dist_equation, mass_max_equation ->
		
		def diff_equation = Equation.linear(0.001, 0.4);
		def random = new Random();
		for(def i = 0; i < point_count; i++)
		{
			def choice = _util.choose_fill_point(random);
			
			def diff = diff_equation.find(i / point_count);
			def dist_min = (i / point_count);
			def dist_max = ((i + diff) / point_count);
			def dist_percent = (dist_max - dist_min) * random.nextDouble() + dist_min;
			
			def point_name = choice.name(parent.getName(), i);
			def point_params = [
				name : point_name,
				max_mass : mass_max_equation.find(dist_percent),
			];
			def new_points = GroovyWrapper.runScript(
				choice.script, 
				point_params);
			
      // the first one is assumed to be the root
      def new_point = new_points[0];
      
			def dist = dist_equation.find(dist_percent);
			def alpha = (Math.PI * 2 * random.nextDouble());
			def dalpha = choice.dalpha(parent.getMass(), new_point.getMass(), dist);
			def orientation = new_point.getObjectOrientation();
			orientation.setAlphaDistance(alpha, dist);
			orientation.setDeltaAlpha(dalpha);
      parent.getChildren() << new_point.getId();
			
			container.addAll(new_points);
		}
    
    for(def point : container) {
      point.setScope(parent.getScope());
    }
	},
];

return _util;
