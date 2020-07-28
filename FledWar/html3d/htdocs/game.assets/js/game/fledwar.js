

function FledWarGame() {
  
  var curr = this;
  
  this.focus = undefined;
  
  this.session_id = undefined;
  
  this.username = undefined;
  
  this.userdata = undefined;
  
  this.config = undefined;
  
  this.config_point_timescale = undefined;
  
  this.config_point_timedelay = undefined;
  
  this.config_unit_timescale = undefined;
  
  this.config_unit_timedelay = undefined;
  
  this.running = false;
  
  this.last_render_time = 0;
  
  this.start_time = get_time();
  
  this.current_game_time = 0;
  
  this.last_frame_delta = 0;
  
  this.viewport_width = 0;
  
  this.viewport_height = 0;
  
  this.set_viewport_size = function(width, height) {
    this.viewport_width = width;
    this.viewport_height = height;
    if (curr.focus) {
      curr.focus.set_view(width, height);
    }
    curr.renderer.setSize(width, height);
    curr.renderer.clear();
  };
  
  this.renderer = new THREE.WebGLRenderer();
//  this.renderer.setSize(window.innerWidth, window.innerHeight);
//  window.addEventListener('resize', function() {
//    curr.renderer.setSize(window.innerWidth, window.innerHeight);
//    curr.focus.set_view(window.innerWidth, window.innerHeight);
//  }, false);
  this.renderer.setClearColor(0x171717, 1);
  this.set_viewport_size(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  
  this.stats = new Stats();
  this.stats.domElement.style.position = 'absolute';
  this.stats.domElement.style.top = '0px';
  this.stats.domElement.style.right = '0px';
  
  this.render_container = $("#gameinterfacescreen");
  this.render_container.append(this.renderer.domElement);
  this.render_container.append(this.stats.domElement);
  
  
  this.focus_on_system = function(system_id) {
    this.ready_focus_change();
    main_socket.set_user_setting("start_system", system_id);
    this.userdata.start_system = system_id;
    this.focus = new SystemFocus();
  };
    
  this.focus_on_system_nav = function() {
    this.ready_focus_change();
    this.focus = new SystemNavFocus();
  };
    
  this.ready_focus_change = function() {
    main_socket.refresh();
    
    if (this.focus) {
      this.focus.clean_up();
      delete this.focus;
    }
  };
    
  this.init = function() {
    dialog.html("initializing game");
        
    var result;
    $.ajax({
      url : START_URL,
      cache : false,
      dataType : "json",
      async : false
    }).done(function (data) {
      result = data;
    }).fail(function(jqXHR, textStatus){
      dialog.html(textStatus);
    });

    if (result === null || !result.ok) {
      console.log("unable to get connection location");
      throw "unable to get connection location";
    }
        
    this.session_id = result.response.session_id;
    this.username = result.response.username;
        
    var connect_callback = function() {
      main_socket.send_message({
          query_type : "config"
        },
        function(result) {
          
          console.log("config: "+JSON.stringify(result.response));
          
          curr.config = result.response;
          curr.config_point_timedelay = curr.config["point.timedelay"];
          curr.config_point_timescale = curr.config["point.timescale"];
          curr.config_unit_timedelay = curr.config["unit.timedelay"];
          curr.config_unit_timescale = curr.config["unit.timescale"];
          curr.init_done_check();
        },
        function(failed) {
          curr.init_failed("could not get game config: "+JSON.stringify(failed));
          return;
        }
      );
      main_socket.send_message({
          query_type : "userdata"
        }, 
        function(result) {
          console.log("userdata: "+JSON.stringify(result.response));
          
          curr.userdata = result.response;
          curr.init_done_check();
        },
        function(failed) {
          curr.init_failed("could not get game config: "+JSON.stringify(failed));
          return;
        }
      ); 
    };
    
    main_socket.init(
        result.response.websocket, 
        this.username, 
        this.session_id,
        connect_callback
      );
    
  };
  
  this.init_done_check = function() {
    
    if (this.config !== undefined &&
        this.userdata !== undefined) {
      
      console.log("finished game init!");
      dialog.html("finished game init!");
      
      if (this.userdata.start_system) {
        this.focus_on_system(this.userdata.start_system);
//        this.focus_on_system_nav(this.userdata.start_system);
        
        // and away we go...
        this.running = true;
        this.last_render_time = get_time();
        this.render_loop();
      }
      else {
        console.log("user has no starting system");
      }
    }
  };
  
  this.init_failed = function(reason) {
    this.running = false;
    this.clean_up();
    dialog.html("unable to init game: " + reason);
  };
  
  this.clean_up = function() {
    
  };
  
  
  this.render_flip = true;
  this.render_loop = function() {
    if (curr.running) {
      requestAnimationFrame(curr.render_loop);    
    }
    
    this.render_flip = !this.render_flip;
    if (!this.render_flip) {
      return;
    }
    
    
    var last_time_ran = curr.current_game_time;
    curr.current_game_time = main_socket.get_server_time();
    curr.last_frame_delta = (curr.current_game_time - last_time_ran);
    
    if (curr.focus !== null) {
      curr.focus.game_tick();
      curr.stats.update();
    }
  };
  
};




