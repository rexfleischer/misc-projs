

json_over_admin_command = {
  id : "admin-command",
  type : "static",
  animate : true,
  start_css : {
    top : "35%",
    left : "23%",
    width : "50%",
    height : "25%"
  },
  content : [ {
      id : "main-menu-random-solarnav",
      type : "button",
      input : {
        label : "run-command"
      },
      click : function() {
        main_socket.send_message({
          action_type : "command", 
          script : $("#admin-command-script").val(),
          input : JSON.parse($("#admin-command-input").val())
        }, 
        function(success) {
          console.log("success: ");
//          console.log(JSON.stringify(success));
        }, 
        function(failed) {
          console.log("failed: ");
          console.log(JSON.stringify(failed));
        });
      }
    }, {
      id : "admin-command-script",
      type : "textarea",
      rows : 2,
      cols : 60,
      value : "bbc/action/unit.impulse.groovy",
      input : {
        resizeable : false
      },
      focus : function() {
        keyboard.save();
      },
      blur : function() {
        keyboard.restore();
      }
    }, {
      id : "admin-command-input",
      type : "textarea",
      rows : 8,
      cols : 60,
      value : function() {
        var scope_id = (game.userdata ? game.userdata.start_system : "");
        var unit_id = (function(){
          var focus_units = game.focus.unit_handler.focus_units;
          if (!focus_units) {
            return "";
          }
          for(var id in focus_units) {
            return id;
          }
          return "";
        })();
        var alpha = 0;
        var distance = 0;
        if (unit_id) {
          var unit = game.focus.unit_handler.focus_units[unit_id];
          alpha = unit.server_data.orientation.alpha;
          distance = unit.server_data.orientation.distance;
        }
        var result = '{"action":{\n';
        result += '"scope":"'+scope_id+'",\n';
        result += '"unit_id":"'+unit_id+'",\n';
        result += '"end_alpha":'+alpha+',\n';
        result += '"end_dist":'+distance+'\n';
        result += '}}';
        return result;
      },
      input : {
        resizeable : false
      },
      focus : function() {
        keyboard.save();
      },
      blur : function() {
        keyboard.restore();
      }
    }
  ]
}

keyboard.reset = function() {
  keyboard.key_events = {};
  keyboard.register(KEY_C, function() {
    menus.toggle(json_over_admin_command, null, false);
  });
}

