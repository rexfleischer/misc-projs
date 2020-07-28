

function SystemNavFocus_Systems(parent) {
  
  var curr = this;
  
  this.ready = false;
  
  this.system_max_dist_from_unknown = undefined;
  
  this.focus_last_update = 0;
  
  this.focus_timeout = 10000;// 10 seconds
  
  this.focus_center_dist = 0;
  
  this.focus_center_alpha = 0;
  
  this.focus_radius = (LIGHTYEAR * 50);
  
  this.focus_data = undefined;
  
  
  
  // setup the data that we want to be updated with.
  // first, we query for the current state of the system,
  // then we set a focus on it to get delta updates.
  main_socket.query_system_layout({
      scope_id : game.userdata.start_system, 
      radius : this.focus_radius
    },
    function(success) {
      curr.focus_data = success.response;
      
      if (!curr.focus_data.length) {
        console.log("unable to find any systems for nav");
        dialog.html("unable to find any systems for nav");
        return;
      }
      
      var center = curr.focus_data[0];
      for(var i = 0; i < curr.focus_data.length; i++) {
        var system = curr.focus_data[i];
        
        if (system._id == game.userdata.start_system) {
          center = system;
          break;
        }
      }
      
      var dist = center.orientation.distance;
      var alpha = center.orientation.alpha;
      var center_x = (dist * Math.cos(alpha));
      var center_y = (dist * Math.sin(alpha));
      
      curr.set_focus_center(center_x, center_y);
      curr.ready = true;
      curr.do_mouse_setup();
      console.log("systems:\n" + JSON.stringify(success.response));
      
    },
    function(failed) {
      // well, crap... 
      // i guess we should just give up
      dialog.html("unable to query for initial system: "+failed.response);
      parent.clean_up();
    }
  );
  
  this.set_focus_center = function(x, y) {
    render.user_center_x = (x * SYSTEMNAV_ZOOM_I);
    render.user_center_y = (y * SYSTEMNAV_ZOOM_I);
    this.focus_center_dist = Math.sqrt(x*x + y*y);
    this.focus_center_alpha = Math.atan2(y, x);
    this.focus_center_alpha %= (Math.PI * 2);
    if (this.focus_center_alpha < 0) {
      this.focus_center_alpha += (Math.PI * 2);
    }
  }
  
  
  
  
  
  this.render = function() {
    var radius = render.pixels_to_game_length(4);
        
    for(var i = 0; i < this.focus_data.length; i++) {
      this.focus_data[i].render(radius);
    }
  }
  
  
  
  
  
  this.update = function() {
    if (game.last_frame_delta > (this.focus_last_update + this.focus_timeout)) {
      console.log("time for another systemnav update!");
            
      this.do_system_update();
    }
        
  }
    
  this.do_system_update = function() {
    console.log("initiating system update");
    this.focus_last_update = get_time();
    
    main_socket.query_system_layout({
          center_dist : this.focus_center_dist, 
          center_alpha : this.focus_center_alpha,
          radius : this.focus_radius
        },
        function(success) {
          curr.focus_data = success.response;

          curr.do_mouse_setup();

        },
        function(failed) {
          dialog.html("unable to update system list: "
            + failed.response);
        }
      );
  }
  
  this.do_mouse_setup = function() {
    mouse.lclick_registry = [];
    for(var i = 0; i < curr.focus_data.length; i++) {
      var system = curr.focus_data[i];
      
      var c_distance = (system.orientation.distance * SYSTEMNAV_ZOOM_I);
      var c_alpha = system.orientation.alpha;
      system.orientation.x = (c_distance * Math.cos(c_alpha));
      system.orientation.y = (c_distance * Math.sin(c_alpha));
      
      system.graphic = new Circle();
      system.graphic.color = "#ff0000";
      system.graphic.radius = render.pixels_to_game_length(4);
      system.graphic.x = -system.orientation.x * 2;
      system.graphic.y = -system.orientation.y * 2;
      
      system.render = function(radius) {
        this.graphic.radius = radius;
                
        this.graphic.render(render.bg_context);
      }
      
      system.clicked = $.proxy(system.graphic.clicked, system.click);
      system.lclick = function(x, y) {
        var animate = !menus.contains(systemnav_main_json_br.id);
        menus.enter(systemnav_main_json_br, this, animate);
      }
      mouse.lclick_registry.push(system);
    }
  }
}
