

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
  
  
  this.focus_on_system = function(system_id) {
    this.ready_focus_change();
    main_socket.set_user_setting("start_system", system_id);
    this.userdata.start_system = system_id;
    this.focus = new SystemFocus();
  }
    
  this.focus_on_system_nav = function() {
    this.ready_focus_change();
    this.focus = new SystemNavFocus();
  }
    
  this.ready_focus_change = function() {
    
    menus.reset(true);
    mouse.reset();
    main_socket.refresh();
    render.bg_clear();
    render.fg_clear();
    
    if (this.focus) {
      this.focus.clean_up();
      delete this.focus;
    }
  }
    
  this.init = function() {
    dialog.html("initializing game");
    
    render.bg_clear();
    render.fg_clear();
        
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

    if (result == null || result.result == "unsuccessful") {
      console.log("unable to get connection location");
      throw "unable to get connection location";
    }
        
    this.session_id = result.response.session_id;
    this.username = result.response.username;
        
    var connect_callback = function() {
      main_socket.send_message({
          "query_type" : "config"
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
          "query_type" : "userdata"
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
    
  }
  
  this.init_done_check = function() {
    
    if (this.config != undefined &&
        this.userdata != undefined) {
      
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
  }
  
  this.init_failed = function(reason) {
    this.running = false;
    this.clean_up();
    dialog.html("unable to init game: " + reason);
  }
  
  this.clean_up = function() {
    
  }
  
  this.render_loop = function() {
//    console.log("render_loop()");
    
    var last_time_ran = game.current_game_time;
    game.current_game_time = main_socket.get_server_time();
    game.last_frame_delta = (game.current_game_time - last_time_ran);
    
    if (curr.focus != null) {
      curr.focus.game_tick();
    }
    
    
    if (curr.running){
      requestAnimationFrame(curr.render_loop);    
    }
  } 
}




