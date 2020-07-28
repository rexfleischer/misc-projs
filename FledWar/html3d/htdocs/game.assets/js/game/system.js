
var SYSTEM_ZOOM = 100000000;
var SYSTEM_ZOOM_I = (1 / SYSTEM_ZOOM);
var SYSTEM_AU = (AU / SYSTEM_ZOOM);
var SYSTEM_LY = (LIGHTYEAR / SYSTEM_ZOOM);

var CAMERA_FOV = (AU * SYSTEM_ZOOM_I);
var CAMERA_NEAR = 0.1;
var CAMERA_FAR = (AU * SYSTEM_ZOOM_I * 100);

function SystemFocus() {
  
  var curr = this;
  
  this.scene = new THREE.Scene();
  
  this.camera = new THREE.PerspectiveCamera(
          CAMERA_FOV, 
          DEFAULT_WIDTH / DEFAULT_HEIGHT,
          CAMERA_NEAR,
          CAMERA_FAR);
  this.set_view = function(width, height) {
    this.camera.aspect = (width / height);
    this.camera.up = new THREE.Vector3(0, 0, 1);
    this.camera.updateProjectionMatrix();
  };
  this.set_view(game.viewport_width, game.viewport_height);
  
  this.initialized = false;
  
  this.ready_unit = false;
  
  this.ready_point = false;
  
  this.move_controls = undefined;
  
  this.following = undefined;
  
  this.system_max_zoom = undefined;
  
  this.system_min_zoom = undefined;
  
  this.system_max_dist_from_center = undefined;
  
  this.point_handler = new SystemFocus_Points(this);
  
//  this.unit_handler = new SystemFocus_Units(this);
  
  
  
  this.menu_system_nav = null;
  
  this.keyboard = new Keyboard();
  
  this.mouse = new Mouse("#gameinterfacescreen", this.camera, this.scene);
  
  
  
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
//      curr.unit_handler.set_init_data(scope_data);
      
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
          if (!update || !update.update_drop_key || 
                  !update.update_drop_key.indexOf) {
            console.log("unable to run update");
            console.log(JSON.stringify(_update));
          }
          if (update.update_drop_key.indexOf("galaxy_unit") >= 0) {
            if (!curr.ready_unit) {
              console.log("first unit sync... units ready");
              curr.ready_unit = true;
            }
//            curr.unit_handler.set_update_data(update);
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
    this.camera.position.x = 0;
    this.camera.position.y = -(AU * 1 * SYSTEM_ZOOM_I);
    this.camera.position.z = (AU * 1 * SYSTEM_ZOOM_I);
    this.camera.lookAt(new THREE.Vector3(0, 0, 0));
    this.setup_free_controls();
  };
  
  
  
  
  
  
  
  
  
  this.follow_object = function(object) {
    if (this.move_controls) {
      this.move_controls.looking_at = object.graphic.position;
      if (object.server_data && object.server_data.radius) {
        var radius = object.server_data.radius * 100 * SYSTEM_ZOOM_I;
        this.move_controls.offset.normalize().multiplyScalar(radius);
      }
    }
  };
  
  this.dont_follow_objects = function() {
    this.following = undefined;
    this.setup_free_controls();
  };
  
  
  
  
  
  this.setup_free_controls = function() {
    this.keyboard.unbind();
    this.keyboard.bind();
    this.mouse.unbind();
    this.mouse.bind();
    
//    this.fly_controls = new SystemFlyControls(this.camera, this.keyboard);
//    this.mouse_click = new SystemClickDefault(this.mouse);
    this.move_controls = new SystemOrbitControls(
            this.camera, this.keyboard, this.mouse);
  };
  
  
  
  
  
  // now its time for the menu
  this.static_menu_init = function() {
    this.menu_system_nav = new main_point_nav_menu(
            this,
            game.render_container,
            this.point_handler.focus_data,
            this.point_handler.focus_root._id);
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
    
    this.scene.updateMatrixWorld();
    this.point_handler.update();
    
//    this.fly_controls.update(game.last_frame_delta);
//    this.mouse_click.update(game.last_frame_delta);
    if (this.move_controls) {
      this.move_controls.update();
    }
    
    game.renderer.render(this.scene, this.camera);
  };
}

var system_warp_factor_to_speed = function(warpfactor) {
  return (warpfactor * warpfactor * warpfactor) * LIGHTYEAR;
};

var system_impulse_factor_to_speed = function(impulse) {
  return 0.05 * impulse * LIGHTYEAR;
};
