

var system_main_unit_command = {
  id : "menu-unit-command",
  type : "static",
  start_css : {
    right : "-35%",
    bottom : "17%",
    width : "35%", 
    height : "10%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    right : 0
  },
  classes : "ui-corner-left",
  create : function() {
  },
  destroy : function() {
    menus.exit(system_main_unit_command_move.id, true);
  },
  content : [ {
      id : "menu-unit-command-button-move",
      type : "button",
      input : {
        label : "move"
      },
      click : function(json) {
        menus.toggle(system_main_unit_command_move, this.extra, true);
      }
    }
  ]
};

var system_main_unit_command_move = {
  id : "menu-unit-command-move",
  type : "static",
  start_css : {
    right : "-35%",
    bottom : "29%",
    width : "35%", 
    height : "4%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    right : 0
  },
  classes : "ui-corner-left",
  create : function() { 
    this.move_stack = [];
  },
  destroy : function() { },
  content : [ {
      id : "menu-unit-command-move-button-start",
      type : "button",
      input : {
        label : "start"
      },
      click : function(json) {
        
      }
    }, {
      id : "menu-unit-command-move-button-reset",
      type : "button",
      input : {
        label : "reset"
      },
      click : function(json) {
        
      }
    }, {
      id : "menu-unit-command-move-button-cancel",
      type : "button",
      input : {
        label : "cancel"
      },
      click : function(json) {
        
      }
    }
  ]
};
