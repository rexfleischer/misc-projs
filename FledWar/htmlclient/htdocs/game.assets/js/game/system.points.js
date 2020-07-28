
function SystemFocus_Points(parent) {
    
  var curr = this;
  
  this.focus_update = undefined;
  
  this.focus_data = undefined;
  
  this.focus_root = undefined;
  
  this.set_update_data = function(data) {
    this.focus_update = data;
  };
  
  
  this.update = function() {
    
    if (this.focus_update !== undefined && 
        this.focus_update["update_time_at"] <= game.current_game_time) {
      
//      console.log("update from server");
//      console.log(JSON.stringify(this.focus_update));
      var focus_system_update = this.focus_update;
      this.focus_update = undefined;
      for(var update_id in focus_system_update.points) {
        var child_update = focus_system_update.points[update_id];
        var child = this.focus_data[update_id];
        child.server_update(child_update);
      }
      
      // then, if there are any access time left, we need to apply
      // that time as well.
      var update_time = focus_system_update["update_time_at"];
      var leftover_delta = (game.current_game_time - update_time);
      if (leftover_delta > 0) {
        var leftover_time_delta = (game.config["point.timescale"] * 
              leftover_delta / GAME_HOUR);
        this.update_manually(leftover_time_delta);
      }
    }
    else {
      // update mathematically for smooth transition between
      // from the server
      var game_time_delta = (game.config["point.timescale"] * 
          game.last_frame_delta / GAME_HOUR);
      this.update_manually(game_time_delta);
    }
  };
  
  
  this.update_manually = function(game_time_delta) {
    for(var id in curr.focus_data) {
      var updating = curr.focus_data[id];
      updating.update(game_time_delta);
    }
  };
  
  
  this.render = function() {
    var min_radius = render.pixels_to_game_length(4);
    this.render_recurse(this.focus_root._id, 0, 0, min_radius);
  };
  
  
  this.render_recurse = function(id, x, y, min_radius) {
    var root = this.focus_data[id];
    root.render(x, y, min_radius);
    for(var i in root.server_data.children) {
      var child_id = root.server_data.children[i];
      this.render_recurse(child_id, root.graphic.x, root.graphic.y, min_radius);
    }
  };
  
  
  this.set_init_data = function(raw_data) {
    this.focus_data = {};
    for(var id in raw_data.points) {
      var server_point = raw_data.points[id];
      if (server_point.is_root) {
        this.focus_root = server_point;
      }
      this.focus_data[id] = new Point(server_point);
    }
  };
  
}

