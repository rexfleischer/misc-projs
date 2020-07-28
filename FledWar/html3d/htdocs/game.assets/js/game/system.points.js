
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
      this.update_server_recurse(this.focus_root._id, 0, 0, 
                                 focus_system_update.points);
      
      // then, if there are any access time left, we need to apply
      // that time as well.
      var update_time = focus_system_update["update_time_at"];
      var leftover_delta = (game.current_game_time - update_time);
      if (leftover_delta < 0 ) leftover_delta = 0;
      var leftover_time_delta = (game.config["point.timescale"] * 
            leftover_delta / GAME_HOUR);
      this.update_manually(leftover_time_delta);
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
    this.update_manually_recurse(this.focus_root._id, 0, 0, game_time_delta);
  };
  
  
  this.update_manually_recurse = function(id, x, y, game_delta) {
    var root = this.focus_data[id];
    root.update(x, y, game_delta);
    for(var i in root.server_data.children) {
      var child_id = root.server_data.children[i];
      this.update_manually_recurse(child_id, root.x, root.y, game_delta);
    }
  };
  
  
  this.update_server_recurse = function(id, x, y, updates) {
    var root = this.focus_data[id];
    if (updates[root._id]) {
      root.server_update(x, y, updates[root._id]);
    }
    for(var i in root.server_data.children) {
      var child_id = root.server_data.children[i];
      this.update_server_recurse(child_id, root.x, root.y, updates);
    }
  };
  
  
  this.set_init_data = function(raw_data) {
    this.focus_data = {};
    for(var id in raw_data.points) {
      var server_point = raw_data.points[id];
      var point = factory_point(server_point);
      if (!point) {
        console.log("skipping point because nothing was factoried:");
        console.log(JSON.stringify(server_point));
        continue;
      }
      if (server_point.is_root) {
        this.focus_root = point;
      }
      
      this.focus_data[id] = point;
      if (point.graphic) {
        parent.scene.add(point.graphic);
      }
    }
  };
  
}

