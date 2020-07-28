

function SystemFocus_Units(parent) {
    
  var curr = this;

  this.focus_units = undefined;

  this.focus_update = undefined;
  
  
  this.set_update_data = function(data) {
    this.focus_update = data;
  };
  
  this.update = function() {
    
    if (this.focus_update !== undefined && 
        this.focus_update["update_time_at"] <= game.current_game_time) {
      
      var focus_unit_update = this.focus_update;
      this.focus_update = undefined;
      for(var update_id in focus_unit_update.units) {
        var unit_update = focus_unit_update.units[update_id];
        var unit = this.focus_units[update_id];
        unit.server_update(unit_update);
      }
      
      // then, if there are any access time left, we need to apply
      // that time as well.
      var update_time = focus_unit_update["update_time_at"];
      var leftover_delta = (game.current_game_time - update_time);
      if (leftover_delta > 0) {
        var gamehours_left = (game.config["unit.timescale"] * 
              leftover_delta / GAME_HOUR);
        this.update_manually(gamehours_left);
      }
    }
    else {
      // update mathematically for smooth transition between
      // from the server
      var gamehours_update = (game.config["unit.timescale"] * 
          game.last_frame_delta / GAME_HOUR);
      this.update_manually(gamehours_update);
    }
  };
  
  this.update_manually = function(gamehours) {
    for(var unit_id in this.focus_units) {
      var unit = this.focus_units[unit_id];
      unit.update(gamehours);
    }
  };
  
  this.render = function() {
    var radius = render.pixels_to_game_length(5);
    for(var id in this.focus_units) {
      var unit = this.focus_units[id];
      unit.render(radius);
    }
  };
  
  this.set_init_data = function(scope_data) {
    this.focus_units = [];
    for(var id in scope_data.units) {
      var unit_data= scope_data.units[id];
      var actions = scope_data.unit_actions[id];
      var unit = new Unit(unit_data, actions);
      this.focus_units[id] = unit;
    }
  };
  
  this.new_action = function(action) {
    var unit = this.focus_units[action.unit_id];
    if (!unit) return;
    
    unit.add_action(action);
  };
}
