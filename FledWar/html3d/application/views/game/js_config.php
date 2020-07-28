
<?php $baseurl = $this->config->item("base_url"); ?>


// specific urls
var BASE_URL = "<?php echo $baseurl; ?>";
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
var AU        = 149597870700; 
var LIGHTYEAR = 9460528400000000; 
var NH_LIGHTYEAR = (-0.5 * LIGHTYEAR);
var I_LIGHTYEAR = (1 / LIGHTYEAR);

var ST_USER = "USER";
var ST_CLUSTER = "CLUSTER";
