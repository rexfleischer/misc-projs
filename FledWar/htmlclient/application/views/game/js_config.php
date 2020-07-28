
<?php $baseurl = $this->config->item("base_url"); ?>


// specific urls
var START_URL = "<?php echo $baseurl."game/start/"; ?>";
var INVALIDATE_URL = "<?php echo $baseurl."logout/invalidate_session/"; ?>";



// client game specific
var DEFAULT_WIDTH = 960;
var DEFAULT_HEIGHT = 700;
var MENU_CONTAINER = "gameinterfacescreen";
var RENDER_DEVICE_BG = "gamebackgroundcanvas";
var RENDER_DEVICE_FG = "gameforegroundcanvas";



// constents
var GAME_HOUR = 2500;
var AU = 149597870700; 
var LIGHTYEAR = 9460528400000000; 
var NH_LIGHTYEAR = (-0.5 * LIGHTYEAR);
var I_LIGHTYEAR = (1 / LIGHTYEAR);

var PT_ORBIT_MARKER = "ORBIT_MARKER";
var PT_STAR = "STAR";
var PT_PLANET = "PLANET";
var PT_MOON = "MOON";
var PT_CLOUD = "CLOUD";
var PT_ASTROID = "ASTROID";
var PT_ASTROID_BELT = "ASTROID_BELT";

var ST_USER = "USER";
var ST_CLUSTER = "CLUSTER";
