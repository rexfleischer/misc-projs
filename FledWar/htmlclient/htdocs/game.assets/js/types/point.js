

function Point(base_point) {
  this.view_type = "point";
  this.server_updates = 0;
  this.server_data = base_point;
  this._id = this.server_data._id;
  
  if (base_point.type === PT_ASTROID_BELT) {
    this.graphic = new Ring();
    this.graphic.color = "#FF0000";
    this.graphic.radius = (this.server_data.orientation.distance / SYSTEM_ZOOM);
    this.thickness_z = (2E7 / SYSTEM_ZOOM);
  }
  else if (base_point.type === PT_MOON) {
    this.graphic = new Circle();
    this.graphic.color = "rgba(255, 255, 255, 0.5)";
    this.radius_z = (this.server_data.radius / SYSTEM_ZOOM);
  }
  else if (base_point.type === PT_ORBIT_MARKER) {
    this.graphic = new Circle();
    this.graphic.color = "#FF0000";
    this.radius_z = (this.server_data.radius / SYSTEM_ZOOM);
  }
  else if (base_point.type === PT_PLANET) {
    this.graphic = new Circle();
    this.graphic.color = "#FF00FF";
    this.radius_z = (this.server_data.radius / SYSTEM_ZOOM);
  }
  else if (base_point.type === PT_STAR) {
    this.graphic = new Circle();
    this.graphic.color = "#FFFF00";
    this.radius_z = (this.server_data.radius / SYSTEM_ZOOM);
  }
  else {
    console.log("unknown type: "+this.type);
  }
  
  
  mouse.lclick_registry.push(this);
}


Point.prototype.clicked = function(x, y) {
  return this.graphic.clicked(x, y);
};


Point.prototype.lclick = function(x, y) {
  var animate = (!menus.contains(system_main_point_json_br.id));
  menus.enter(system_main_point_json_br, this, animate);
};


Point.prototype.server_update = function(update) {
  var orientation = this.server_data.orientation;
  orientation.alpha = update["orientation.alpha"];
  this.server_updates++;
  
  this.set_server_ad(orientation.alpha, orientation.distance);
};


Point.prototype.update = function(gamehours) {
  var orientation = this.server_data.orientation;
  orientation.alpha += (orientation.dalpha * gamehours);
  orientation.alpha %= (2 * Math.PI);

  if (orientation.alpha < 0.0) {
    orientation.alpha += (2 * Math.PI);
  }
  
  this.set_server_ad(orientation.alpha, orientation.distance);
};


Point.prototype.set_client_xy = function(x, y) {
  this.x = this.graphic.x = x;
  this.y = this.graphic.y = y;
  this.server_data.orientation.alpha = Math.atan2(y, x);
  this.server_data.orientation.distance = Math.sqrt(y*y + x*x) * SYSTEM_ZOOM;
};


Point.prototype.set_server_xy = function(x, y) {
  this.x = this.graphic.x = (x * SYSTEM_ZOOM_I);
  this.y = this.graphic.y = (y * SYSTEM_ZOOM_I);
  this.server_data.orientation.alpha = Math.atan2(y, x);
  this.server_data.orientation.distance = Math.sqrt(y*y + x*x);
};


Point.prototype.set_server_ad = function(alpha, distance) {
  this.server_data.orientation.alpha = alpha;
  this.server_data.orientation.distance = distance;
  this.x = this.graphic.x = (distance * Math.cos(alpha) * SYSTEM_ZOOM_I);
  this.y = this.graphic.y = (distance * Math.sin(alpha) * SYSTEM_ZOOM_I);
};


Point.prototype.render = function(parent_x, parent_y, min_radius) {
  var dist = this.server_data.orientation.distance;
  var alpha = this.server_data.orientation.alpha;

  var dx = dist * Math.cos(alpha) * SYSTEM_ZOOM_I;
  var dy = dist * Math.sin(alpha) * SYSTEM_ZOOM_I;

  this.x = this.graphic.x = (parent_x + dx);
  this.y = this.graphic.y = (parent_y + dy);

  if (this.server_data.type === PT_ASTROID_BELT) {
    if (this.thickness_z < min_radius) {
      this.graphic.thickness = min_radius;
    }
    else {
      this.graphic.thickness = this.thickness_z;
    }
    this.graphic.x = parent_x;
    this.graphic.y = parent_y;
  }
  else if (this.server_data.type === PT_MOON) {
    if ((this.radius_z < min_radius) && 
        (this.radius_z * 1000 > min_radius)) {
      this.graphic.radius = min_radius;
    }
    else {
      this.graphic.radius = this.radius_z;
    }
  }
  else if (this.server_data.type === PT_ORBIT_MARKER) {
    this.graphic.radius = min_radius;
  }
  else if (this.server_data.type === PT_PLANET) {
    if (this.radius_z < min_radius) {
      this.graphic.radius = min_radius;
    }
    else {
      this.graphic.radius = this.radius_z;
    }
  }
  else if (this.server_data.type === PT_STAR) {
    if (this.radius_z < min_radius) {
      this.graphic.radius = min_radius;
    }
    else {
      this.graphic.radius = this.radius_z;
    }
  }
  else {
    console.log("unknown type: "+this.server_data.type);
    return;
  }

  this.graphic.render(render.bg_context);
};
