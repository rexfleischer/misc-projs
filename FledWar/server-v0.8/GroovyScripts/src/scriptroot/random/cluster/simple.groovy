
import com.fledwar.groovy.GroovyWrapper;
util = GroovyWrapper.runScript("random/system/util.groovy", [:]);

import org.bson.types.ObjectId;
import com.fledwar.vto.galaxy.util.SpaceConstents;
import com.fledwar.vto.galaxy.util.SpaceFunctionUtil;
import com.fledwar.vto.galaxy.util.ScopeData;
import com.fledwar.vto.galaxy.scope.GalaxyScope;
import com.fledwar.vto.galaxy.scope.GalaxyScopeType;



// here is where the actual script starts

if (!binding.variables.containsKey("name"))
{
	throw new Exception("name must be specified");
}

if (!binding.variables.containsKey("center_distance"))
{
	throw new Exception("center_distance must be specified");
}

if (!binding.variables.containsKey("center_alpha"))
{
	throw new Exception("center_alpha must be specified");
}

if (!binding.variables.containsKey("cluster_radius"))
{
	throw new Exception("radius must be specified");
}

if (!binding.variables.containsKey("cluster_seperation"))
{
	throw new Exception("seperation must be specified");
}

if (!binding.variables.containsKey("cluster_dalpha"))
{
	throw new Exception("seperation must be specified");
}





def cluster = [];

def cluster_center = new GalaxyScope();
cluster_center.generateId();
cluster_center.setScopeType(GalaxyScopeType.CLUSTER);
cluster_center.setName(name);
def cluster_orientation = cluster_center.getGalaxyOrientation();
cluster_orientation.setAlphaDistance(center_alpha, center_distance);
cluster << new ScopeData(cluster_center);

def random = new Random();
while(true)
{
	def system_name = "${name}-system${cluster.size()}";
	def new_system = GroovyWrapper.runScript(
			"random/system/system.groovy", 
			[ name : system_name ]);
	
	def found_place = false;
	for(def i = 0; i < 50 && !found_place; i++)
	{
		def distance = (random.nextDouble() * cluster_radius);
		def alpha = (random.nextDouble() * 2 * Math.PI);
		
		def collision = false;
		for(def j = 1; j < cluster.size() && !collision; j++)
		{
			def checking = cluster.get(j);
			def checking_o = checking.scope.getObjectOrientation();
			def checking_distance = SpaceFunctionUtil
					.distanceBetweenTwoObjects(
							alpha, 
							distance, 
							checking_o.getAlpha(), 
							checking_o.getDistance());
			
			if (checking_distance < cluster_seperation)
			{
				collision = true;
			}
		}
		
		if (!collision)
		{
			def system_status = new_system.scope.getObjectOrientation();
			system_status.setAlphaDistance(alpha, distance);
			system_status.setDeltaAlpha(cluster_dalpha);
			system_status.setOrientationIdType(cluster_center.getId(), "cluster");
            
			def galaxy_orientation = new_system.scope.getGalaxyOrientation();
      galaxy_orientation.setAlphaDistance(cluster_orientation, system_status);
			
			found_place = true;
		}
	}
	
	
	if (!found_place)
	{
		break;
	}
	else
	{
		cluster << new_system;
	}
}




return cluster;
