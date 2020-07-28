
var SYSTEM_ZOOM = 100000000;
//var SYSTEM_ZOOM = 1;
var SYSTEM_ZOOM_I = (1 / SYSTEM_ZOOM);
var SYSTEM_AU = (AU / SYSTEM_ZOOM);
var SYSTEM_LY = (LIGHTYEAR / SYSTEM_ZOOM);

function SystemFocus() {
  
  var curr = this;
  
  this.initialized = false;
  
  this.render_flag = true;
  
  this.ready_unit = false;
  
  this.ready_point = false;
  
  this.following = undefined;
  
  this.system_max_zoom = undefined;
  
  this.system_min_zoom = undefined;
  
  this.system_max_dist_from_center = undefined;
  
  this.point_handler = new SystemFocus_Points(this);
  
  this.unit_handler = new SystemFocus_Units(this);
  
  this.input_handler = new SystemFocus_Inputs(this);
  
  
  /**
   * initial render view
   */
  render.clear();
  render.user_view_width = SYSTEM_AU;
  render.user_center_x = 0;
  render.user_center_y = 0;
  
  
  
  main_socket.query_system(game.userdata.start_system, true, true, true, true,
    function(success) {
//      console.log(JSON.stringify(success));
      var scope_data = success.response;
      if (!scope_data.units) {
        scope_data.units = {};
      }
      if (!scope_data.unit_actions) {
        scope_data.unit_actions = {};
      }
      
      curr.point_handler.set_init_data(scope_data);
      curr.unit_handler.set_init_data(scope_data);
      
      var distance = 0;
      for(var i in scope_data.points) {
        if (distance < scope_data.points[i].orientation.distance) {
          distance = scope_data.points[i].orientation.distance;
        }
      }
      render.user_view_width = (distance * SYSTEM_ZOOM_I * 2.1);
      console.log("set user width: "+render.user_view_width);
      
      curr.system_max_zoom = render.user_view_width * 10;
      console.log("set user max zoom: "+curr.system_max_zoom);
      
      curr.system_min_zoom = (SYSTEM_AU / 100);
      console.log("set user min zoom: "+curr.system_min_zoom);
      
      curr.system_max_dist_from_center = render.user_view_width;
      console.log("set user max dist from center: "+curr.system_max_dist_from_center);
      
      curr.reset_view();
      
      dialog.html("syncing data and time with server");
      
      main_socket.focus_on_system(game.userdata.start_system, 
        function(success){
          console.log("focus setup successful! waiting for sync...");
        },
        function(failed){
          dialog.html(JSON.stringify(failed));
          game.running = false;
        },
        function(_update){
          var update = _update.update;
          if (!update || !update.update_drop_key || !update.update_drop_key.indexOf) {
            console.log("unable to run update");
            console.log(JSON.stringify(_update));
          }
          if (update.update_drop_key.indexOf("galaxy_unit") >= 0) {
            if (!curr.ready_unit) {
              console.log("first unit sync... units ready");
              curr.ready_unit = true;
            }
            curr.unit_handler.set_update_data(update);
          }
          else if (update.update_drop_key.indexOf("galaxy_point") >= 0) {
            if (!curr.ready_point) {
              console.log("first point sync... points ready");
              curr.ready_point = true;
            }
            curr.point_handler.set_update_data(update);
          }
          else if (update.update_drop_key.indexOf("new-action") >= 0) {
            curr.unit_handler.new_action(update.action);
          }
          else if (update.update_drop_key.indexOf("new-unit") >= 0) {
            curr.input_handler.new_unit(update);
          }
          else {
            console.log("unknown update drop key: "+update.update_drop_key);
            console.log(JSON.stringify(update));
          }
        });
    },
    function(failed) {
      // ok, screw you!
      dialog.html("unable to query for system: "+failed.response);
      game.running = false;
    });
  
  
  
  
  
  
  
  this.reset_view = function() {
    menus.exit(system_main_point_json_br.id, true);
    menus.exit(system_main_point_json_tr.id, true);
    menus.exit(system_main_json_follow.id, true);
    $("#menu-bottom-left-widthslider").slider("value", 500);
    render.user_center_x = 0;
    render.user_center_y = 0;
    render.user_view_width = 
      (game.focus.system_max_zoom - game.focus.system_min_zoom) *
      Math.pow(0.5, 3) +
      game.focus.system_min_zoom;
  };
  
  
  
  
  // now its time for the menu
  this.static_menu_init = function() {
    menus.enter(system_main_json_bl, null, true);
    menus.enter(system_main_json_tl, null, true);
  };
  
  
  
  
  this.clean_up = function() {
    
  };
  
  this.game_tick = function() {
    
    if (!this.ready_unit || !this.ready_point) {
      return;
    }
    
    if (!this.initialized) {
      this.initialized = true;
      dialog.hide();
      this.static_menu_init();
    }
    
    this.point_handler.update();
    this.unit_handler.update();
    menus.update();
    
    if (this.render_flag) {
      render.clear();
      render.ready_transform();
      
      this.point_handler.render();
      this.unit_handler.render();
    }
    this.render_flag = !this.render_flag;
  };
}

var system_warp_factor_to_speed = function(warpfactor) {
  return (warpfactor * warpfactor * warpfactor) * LIGHTYEAR;
};

var system_impulse_factor_to_speed = function(impulse) {
  return 0.05 * impulse * LIGHTYEAR;
};
