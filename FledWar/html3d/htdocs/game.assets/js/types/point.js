
var PT_ORBIT_MARKER = "ORBIT_MARKER";
var PT_STAR = "STAR";
var PT_PLANET = "PLANET";
var PT_MOON = "MOON";
var PT_CLOUD = "CLOUD";
var PT_ASTROID = "ASTROID";
var PT_ASTROID_BELT = "ASTROID_BELT";

function factory_point(base_point) {
  
  var result = null;
  switch(base_point.type) {
    
    case PT_ORBIT_MARKER:  
      result = new PointMarker(base_point);
      break;
      
    case PT_STAR:
      result = new PointStar(base_point);
      break;
      
    case PT_PLANET:
      result = new PointPlanet(base_point);
      break;
      
    case PT_MOON:
      result = new PointMoon(base_point);
      break;
      
    case PT_ASTROID_BELT:
      result = new PointBelt(base_point);
      break;
      
    case PT_CLOUD:
    case PT_ASTROID:
      console.log("unsupported point type: "+base_point.type);
      break;
      
    default:
      console.log("unknown point type: "+base_point.type);
      break;
  }
  return result;
}


function BasePoint(base_point) {
  this.view_type      = "point";
  this.server_updates = 0;
  this.server_data    = base_point;
  this._id            = (base_point ? base_point._id : null);
  this.type           = (base_point ? base_point.type : null);
  this.name           = (base_point ? base_point.name : null);
  this.x              = 0;
  this.y              = 0;
}


BasePoint.prototype.server_update = function(parent_x, parent_y, update) {
  this.server_updates++;
  this.set_server_position(parent_x, parent_y,
                           update["orientation.alpha"], 
                           this.server_data.orientation.distance);
};


BasePoint.prototype.update = function(parent_x, parent_y, gamehours) {
  var orientation = this.server_data.orientation;
  var alpha = orientation.alpha;
  alpha += (orientation.dalpha * gamehours);
  alpha %= (2 * Math.PI);
  if (alpha < 0.0) { alpha += (2 * Math.PI); }
  
  this.set_server_position(parent_x, parent_y, alpha, orientation.distance);
};


BasePoint.prototype.set_server_position = function(parent_x, 
                                                   parent_y, 
                                                   alpha, 
                                                   distance) {
  var dist_i = (distance * SYSTEM_ZOOM_I);
  this.server_data.orientation.alpha = alpha;
  this.server_data.orientation.distance = distance;
  this.x = (dist_i * Math.cos(alpha) + parent_x);
  this.y = (dist_i * Math.sin(alpha) + parent_y);
  if (this.graphic) {
     this.graphic.position.x = this.x;
     this.graphic.position.y = this.y;
  }
};

