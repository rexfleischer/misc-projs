
function BaseUnit(base_unit, actions) {
  this.view_type      = "unit";
  this.server_updates = 0;
  this.server_data    = base_unit;
  this._id            = (base_unit ? this.server_data._id : null);
  this.type           = (base_unit ? this.server_data.type : null);
  this.name           = (base_unit ? this.server_data.name : null);
  this.x              = 0;
  this.y              = 0;
  this.actions        = [];
  for(var index in actions) {
    var action = actions[index];
  }
  
  
  this.graphic = new Circle();
  this.graphic.color = "rgba(255, 255, 255, 0.5)";
  var dist  = base_unit.orientation.distance;
  var alpha = base_unit.orientation.alpha;
  this.x = this.graphic.x = (dist * Math.cos(alpha) * SYSTEM_ZOOM_I);
  this.y = this.graphic.y = (dist * Math.sin(alpha) * SYSTEM_ZOOM_I);
  this.actions = (actions ? actions : []);
  this.show_actions = false;
}


BaseUnit.prototype.render_actions = function() {
//  var client_x = 
//  for(var i in this.actions) {
//    var action = this.actions[i];
//    
//  }
};


BaseUnit.prototype.server_update = function(update) {     
  console.log("unit update from server");
  console.log(JSON.stringify(update));
  if (typeof(update["orientation.distance"]) === "number" &&
      typeof(update["orientation.alpha"]) === "number") {
    this.server_updates++;
    this.set_server_ad(update["orientation.alpha"], 
                       update["orientation.distance"]);
  }
  
  if (update["action"]) {
    for(var action_update_id in update["action"]) {
      var action_update = update["action"][action_update_id];
      if (action_update === "finished") {
        this.remove_action(action_update_id);
      }
    }
  }
};


BaseUnit.prototype.update = function(gamehours) {
  if (this.actions.length === 0) return;
  var action = this.actions[0];
  var finished = true;
  switch(action.type.toLowerCase()) {
    case "impulse":
      finished = this._impulse_update(action, gamehours);
      break;
  }
  if (finished) {
    this.actions.splice(0, 1);
  }
};


BaseUnit.prototype.add_action = function(action) {
  this.actions.push(action);
};


BaseUnit.prototype.remove_action = function(action) {
  var action_id = (typeof(action) === "string" ? action : action._id);
  for(var i = 0; i < this.actions.length; i++) {
    if (action_id === this.actions[i]._id) {
      this.actions.splice(i, 1);
      return true;
    }
  }
  return false;
};


BaseUnit.prototype.set_client_xy = function(x, y) {
  this.x = this.graphic.x = x;
  this.y = this.graphic.y = y;
  this.server_data.orientation.alpha = Math.atan2(y, x);
  this.server_data.orientation.distance = Math.sqrt(y*y + x*x) * SYSTEM_ZOOM;
};


BaseUnit.prototype.set_server_xy = function(x, y) {
  this.x = this.graphic.x = (x * SYSTEM_ZOOM_I);
  this.y = this.graphic.y = (y * SYSTEM_ZOOM_I);
  this.server_data.orientation.alpha = Math.atan2(y, x);
  this.server_data.orientation.distance = Math.sqrt(y*y + x*x);
};


BaseUnit.prototype.set_server_ad = function(alpha, distance) {
  this.server_data.orientation.alpha = alpha;
  this.server_data.orientation.distance = distance;
  this.x = this.graphic.x = (distance * Math.cos(alpha) * SYSTEM_ZOOM_I);
  this.y = this.graphic.y = (distance * Math.sin(alpha) * SYSTEM_ZOOM_I);
};


BaseUnit.prototype._impulse_update = function(action, time_delta) {
  var start_and_delay = (action.start_time + game.config_unit_timedelay);
  if (start_and_delay >= game.current_game_time) {
    // this means the action hasnt started yet
    console.log("action call while not started");
    return false;
  }
  
  if (action.gamehour_dx === undefined || 
      action.gamehour_dy === undefined ||
      action.gamehour_dd === undefined) {
    
    action.end_x_i = (action.end_x * SYSTEM_ZOOM_I);
    action.end_y_i = (action.end_y * SYSTEM_ZOOM_I);
    if (typeof(action.start_x) === "number" && 
        typeof(action.start_y) === "number") {
      this.set_server_xy(action.start_x, action.start_y);
    }
    action.start_x_i = this.x;
    action.start_y_i = this.y;
    var alpha = Math.atan2(action.end_y_i - action.start_y_i,
                           action.end_x_i - action.start_x_i);
    var unit_speed = system_impulse_factor_to_speed(action.impulse_used);
    action.gamehour_dd = (unit_speed * SYSTEM_ZOOM_I / GAME_HOUR / 3.5);
    action.gamehour_dx = (action.gamehour_dd * Math.cos(alpha));
    action.gamehour_dy = (action.gamehour_dd * Math.sin(alpha));
    console.log("action gamehour_dd: "+action.gamehour_dd);
    console.log("action gamehour_dx: "+action.gamehour_dx);
    console.log("action gamehour_dy: "+action.gamehour_dy);
  }
  
  var applied_dd = (time_delta * action.gamehour_dd);
//  console.log("unit impulse applied dd: "+applied_dd);
  
  var distance_x = (action.end_x_i - this.x);
  var distance_y = (action.end_y_i - this.y);
  var distance = Math.sqrt(distance_x*distance_x + distance_y*distance_y);
  if (distance <= applied_dd) {
    // we are finished
    this.set_server_xy(action.end_x, action.end_y);
    console.log("signaling action finished [2]");
    return true;
  }
  else {
    var new_x = (this.x + time_delta * action.gamehour_dx);
    var new_y = (this.y + time_delta * action.gamehour_dy);
    
    this.set_client_xy(new_x, new_y);
    return false;
  }
};
