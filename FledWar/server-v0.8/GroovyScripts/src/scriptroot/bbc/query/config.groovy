
def config = engine.getConfiguration();
//return config.getRootObject();

def point_timescale = "simulation.features.galaxy_point.time_scale";
def point_timedelay = "simulation.features.galaxy_point.time_delay";
def unit_timescale = "simulation.features.galaxy_unit.time_scale";
def unit_timedelay = "simulation.features.galaxy_unit.time_delay";

return [
	"point.timescale" : config.getAsInteger(point_timescale),
  "point.timedelay" : config.getAsInteger(point_timedelay),
	"unit.timescale" 	: config.getAsInteger(unit_timescale),
  "unit.timedelay"  : config.getAsInteger(unit_timedelay)
];
