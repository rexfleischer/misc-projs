

function SystemFocus_Inputs(parent) 
{
  
  // now lets set up the mouse activity
  mouse.reset();
  keyboard.reset();
  
  mouse.ldrag = function() {
    
    if (mouse.start_x === undefined ||
        mouse.start_y === undefined) {
      mouse.origin_x = render.user_center_x;
      mouse.origin_y = render.user_center_y;
      mouse.start_x = render.pixels_to_game_length(mouse.drag_start_x);
      mouse.start_y = render.pixels_to_game_length(mouse.drag_start_y);
    }
    
    var new_x = render.pixels_to_game_length(mouse.x);
    var new_y = render.pixels_to_game_length(mouse.y);
    var new_dx = (mouse.start_x - new_x) * 0.5;
    var new_dy = (mouse.start_y - new_y) * 0.5;
    
    render.user_center_x = (mouse.origin_x - new_dx);
    render.user_center_y = (mouse.origin_y - new_dy);
    
    // now we need to check that the user doesnt 
    // get lost by moving too far out
    var dist_from_center = Math.sqrt(
      render.user_center_x * render.user_center_x +
      render.user_center_y * render.user_center_y);
    if (dist_from_center > parent.system_max_dist_from_center) {
      var out_dist = (dist_from_center - parent.system_max_dist_from_center);
      var out_alpha = Math.atan2(render.user_center_y, render.user_center_x);
      var out_dx = out_dist * Math.cos(out_alpha);
      var out_dy = out_dist * Math.sin(out_alpha);
      mouse.render_target.user_center_x -= out_dx;
      mouse.render_target.user_center_y -= out_dy;
    }
  };
  mouse.lenddrag = function() {
    mouse.start_x = undefined;
    mouse.start_y = undefined;
    mouse.origin_x = undefined;
    mouse.origin_y = undefined;
  };
  mouse.lclick_miss = function() {
    menus.exit(system_main_point_json_br.id, true);
    menus.exit(system_main_point_json_tr.id, true);
  };
  
  mouse.wheel_up = function() {
    var percent = (20 / render.screen_height);
    if (keyboard.shift) percent *= 2;
    render.user_view_width = (render.user_view_width * (1 - percent));
    
    if (render.user_view_width < parent.system_min_zoom) {
      render.user_view_width = parent.system_min_zoom;
    }
  };
  
  mouse.wheel_down = function() {
    var percent = (20 / render.screen_height);
    if (keyboard.shift) percent *= 2;
    render.user_view_width = (render.user_view_width * (1 + percent));
    
    if (render.user_view_width > parent.system_max_zoom) {
      render.user_view_width = parent.system_max_zoom;
    }
  };
  
  
  // now for the key input
  keyboard.register(KEY_N, function(){
    game.focus_on_system_nav();
  });
  keyboard.register(KEY_S, function(){
    if (menus.contains(system_main_json_nav_status.id)) {
      menus.exit(system_main_json_nav_status.id, true);
    }
    else {
      menus.enter(system_main_json_nav_status, null, true);
    }
  });
  keyboard.register(KEY_R, function(){
    parent.reset_view();
  });
  keyboard.register(KEY_A, mouse.wheel_up);
  keyboard.register(KEY_Z, mouse.wheel_down);
  keyboard.register(KEY_UP, function(){
    var change = (5 / render.screen_height) * render.user_view_width;
    if (keyboard.shift) change *= 2;
    render.user_center_y += change;
  });
  keyboard.register(KEY_DOWN, function(){
    var change = (5 / render.screen_height) * render.user_view_width;
    if (keyboard.shift) change *= 2;
    render.user_center_y -= change;
  });
  keyboard.register(KEY_LEFT, function(){
    var change = (5 / render.screen_width) * render.user_view_width;
    if (keyboard.shift) change *= 2;
    render.user_center_x += change;
  });
  keyboard.register(KEY_RIGHT, function(){
    var change = (5 / render.screen_width) * render.user_view_width;
    if (keyboard.shift) change *= 2;
    render.user_center_x -= change;
  });
  
  
  
  
  this.send_impulse_command = function(unit, end_x, end_y) {
    var input = {unit_id : unit._id, end_x : end_x, end_y : end_y};
    main_socket.user_action("unit.impulse", game.userdata.start_system, input, 
    function(success) {
      console.log("command successful: ");
      console.log("action input: "+JSON.stringify(input));
      console.log("response from server: "+JSON.stringify(success));
    }, 
    function(failed){
      console.log("failed to perfrom user action: ");
      console.log("action input: "+JSON.stringify(input));
      console.log("response from server: "+JSON.stringify(failed));
    });
  };
  
  this.new_action = function(action) {
    console.log("attempting to handle new action: ");
//    console.log(JSON.stringify(action));
    
    // first lets find the unit that is doing the action
    var unit = parent.unit_handler.focus_units[action.unit_id];
    if (!unit) {
      // just return because this isnt a failure, it probably
      // means you dont have rights to see the unit
      return;
    }
    
    if (!this.ready_action_update_function(unit, action)) {
      console.log("unable to setup update function");
      return;
    }
    
    parent.unit_handler.focus_actions[action._id] = action;
    var found = false;
    for(var i in unit.actions) {
      var action_check = unit.actions[i];
      if (action_check._id === action._id) {
        unit.actions[i] = action;
        found = true;
      }
    }
    if (found) {
      console.log("replaced action with a server action");
    }
    else {
      console.log("received new action from server");
      unit.actions.push(action);
    }
    
    console.log("successfully handled action");
  };
  
  this.ready_action_update_function = function(unit, action) {
    var action_type = action.type.toLowerCase();
    switch(action_type) {
      case "impulse":
        action.update_function = function(time_delta) { 
          return unit_impulse_update(unit, action, time_delta);
        };
        return true;
        
      default:
        console.log("unable to handle action: unknown action type ("+
                action_type+")");
        return false;
    }
  };
  
  this.remove_action = function(action_id) {
    var action = parent.unit_handler.focus_actions[action_id];
    if (!action) return;
    var unit = parent.unit_handler.focus_units[action.unit_id];
    if (!unit) return;
    
    for(var i in unit.actions) {
      var check = unit.actions[i];
      if (check._id === action_id) {
        unit.actions.splice(i, 1);
        break;
      }
    }
    
    if (parent.unit_handler.focus_actions[action_id]) {
      delete parent.unit_handler.focus_actions[action_id];
    }
    
    console.log("remove attempt on action ("+
      action_id+") for unit ("+action.unit_id+")");
  };
  
  this.new_unit = function(unit) {
    
  };
  
  this.remove_unit = function(unit) {
    
  };
  
}
