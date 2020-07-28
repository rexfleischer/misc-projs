
var main_socket = {
    
  sync_timeout : 5000,
  
  username : undefined,
  
  session_id : undefined,
  
  open_callback : undefined,

  socket : undefined,

  callback_count : 0,
  
  callbacks : {},

  focuses : {},

  server_time_delta : 0,

  get_server_time : function() {
      return (get_time() + main_socket.server_time_delta);
  },
  
  
  
  init : function(url, username, session_id, open_callback) {
    console.log("connecting to: " + ("ws://"+url));
    var WebSocketObject = window.WebSocket || window.MozWebSocket;
    if (!WebSocketObject){
        dialog.html("<p>Your browser does not support WebSocket.</p>");
        return;
    }
    main_socket.username = username;
    main_socket.session_id = session_id;
    main_socket.open_callback = open_callback;
    main_socket.socket = new WebSocketObject("ws://"+url);
    main_socket.socket.onopen     = main_socket.socket_onopen;
    main_socket.socket.onmessage  = main_socket.socket_onmessage;
    main_socket.socket.onclose    = main_socket.socket_onclose;
    main_socket.socket.onerror    = main_socket.socket_onerror;
  },
  socket_onopen : function(){

    main_socket.send_message({
      session_id : main_socket.session_id,
      username : main_socket.username
    },
    function(success){
      console.log("time sync with server");
      main_socket.sync_with_server();
    });

    if (main_socket.open_callback) {
      main_socket.open_callback();
      delete main_socket.open_callback;
    }
  },
  socket_onmessage : function(event) {
    
    var message = JSON.parse(event.data);
    if (!message.ok) {
      console.log("unsuccessful message: " + event.data);
    }
    
    if (message.close) {
      // tell it to redirect to itself to end the simulation
      window.location.href = INVALIDATE_URL;
      return;
    }
    
    if (message.original && message.original.callback_id) {
//      console.log("callback: " + event.data);
      if (!main_socket.callbacks["_"+message.original.callback_id]) {
        console.log("unable to find callback: "+message.original.callback_id);
      }
      else {
        main_socket.callbacks["_"+message.original.callback_id](message);
        delete main_socket.callbacks["_"+message.original.callback_id];
      }
      return;
    }
    
    if (message.focus) {
//      console.log("focus with id: " + message.focus);
      if (!main_socket.focuses[message.focus]) {
        console.log("unable to find focus callback for "+message.focus);
        return;
      }
      
      main_socket.focuses[message.focus](message);
    }
  },
  socket_onclose : function(event) {
    // event.wasClean
    // event.code
    // event.reason
    dialog.html("<p>lost connection [1] ("+event.reason+")</p>");
  },
  socket_onerror : function() {
    dialog.html("<p>lost connection [2]</p>");
  },
  
  
  
  send_message : function(object, success, failed, after) {
    if (success != null || failed != null || after != null) {
      object.callback_id = (++main_socket.callback_count);
      main_socket.callbacks["_"+object.callback_id] = function(response) {
        if (!response.ok) {
          if (failed) {
            failed(response);
          }
        }
        else if (success) {
          success(response);
        }
                
        if (after) {
          after(response);
        }
      };
    }
        
    var message = JSON.stringify(object);
//    console.log(message);
    main_socket.socket.send(message);
  },
  user_action : function(action, scope_id, action_input, 
                         success, failed, after) {
    console.log("user performing action: "+action);
    console.log("action input: "+JSON.stringify(action_input));
    var input = $.extend({
      action_type : action,
      scope : scope_id
    }, action_input);
    this.send_message(input, success, failed, after);
  },
  query_system : function(scope_id, 
                          scope, points, units, unit_actions,
                          success, failed, after) {
    console.log("querying for system: "+scope_id);
    var query = {query_type : "system", scope_id : scope_id};
    if (scope) query.scope = scope;
    if (points) query.points = points;
    if (units) query.units = units;
    if (unit_actions) query.unit_actions = unit_actions;
    main_socket.send_message(query, success, failed, after);
  },
  query_system_layout : function(options, success, failed, after) {
    console.log("querying for system layout: "+JSON.stringify(options));
    main_socket.send_message(
      $.extend({query_type : "system_layout"}, options), 
      success, failed, after
    );
  },
  set_user_setting : function(key, value, success, failed, after) {
    console.log("set client var: "+key+"="+value);
    main_socket.send_message(
      { action_type : "set_user_setting", key : key, value : value}, 
      success, failed, after
    );
  },
  set_session_var : function(key, value, success, failed, after) {
    console.log("set session var: "+key+"="+value);
    main_socket.send_message(
      { action_type : "set_session_var", key : key, value : value}, 
      success, failed, after
    );
  },
  focus_on_system : function(scope_id, success, failed, update) {
    console.log("focusing on system: "+scope_id);
    main_socket.send_message(
      {focus_type : "galaxy_scope", scope_id : scope_id},
      function(_success) {
        console.log("successful focusing on system: " + scope_id);
        var drop_id = _success.response;
        if (success) {
          success(_success);
        }
        main_socket.focuses[drop_id] = update;
      },
      failed
    );
  },
  end_focus : function(focus_id, success, failed, after) {
    console.log("ending focus: " + focus_id);
    delete main_socket.focuses[focus_id];
    this.send_message(
      {action_type : "end_focus", drop_key : focus_id}, 
      success, failed, after
    );
  },
  
  
  
  refresh : function() {
    for(var prop in main_socket.focuses) {
      main_socket.end_focus(prop,
        function(success){
          console.log("end focus successful: "+success.response.dropped);
        }, 
        function(failed){
          console.log("end focus failed: "+failed.response.dropped);
        }
      );
    }
  },
  close : function() {
    main_socket.socket.close();
  },
  sync_with_server : function() {
        
    var ping_start = get_time();
        
    main_socket.send_message({
        action_type : "ping"
      }, 
      function(success) {
        var server_time = success.response.time;
        var ping_end = get_time();
        var latency = ((ping_end - ping_start) * 0.5);
        main_socket.server_time_delta = (server_time - ping_end + latency);
//        console.log("client time: "+ping_end);
//        console.log("server time: "+success.response.time);
//        console.log("net latency: "+latency);
//        console.log("server_time_delta="+main_socket.server_time_delta);
      },
      function(failed) {
        var reason = JSON.stringify(failed);
        console.log("failed to time sync: \n" + reason);
        console.log("trying again soon...");
      },
      function(after) {
        setTimeout(function(){
          main_socket.sync_with_server();
        }, main_socket.sync_timeout);
      }
    );
  }
};

