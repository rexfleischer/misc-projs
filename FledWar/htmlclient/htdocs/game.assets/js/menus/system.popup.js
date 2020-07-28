
var system_over_json_main = {
  id : "menu-main-random",
  type : "static",
  animate : true,
  start_css : {
    top : "10%",
    left : "-90%",
    width : "80%",
    height : "80%"
  },
  enter_css : {
    left : "08%"
  },
  destroy : function() {
    $("#"+this.id).button("enable");
  },
  content : [ {
      id : "main-menu-random-solarnav",
      type : "button",
      input : {
        label : "solar-nav"
      },
      click : function() {
        game.focus_on_system_nav();
      }
    }
  ]
};


var popup_menu_toolbar = {
  id : "main-toolbar",
  type : "toolbar",
  classes : "ui-widget-header ui-dialog-titlebar ui-corner-all ui-helper-clearfix",
  content : [ {
    id : "main-toolbar-space",
    type : "text",
    value : "&nbsp;",
    classes : "ui-dialog-title"
  }, {
      id : "main-toolbar-exit",
      type : "button",
      classes : "ui-button-icon-only ui-dialog-titlebar-close ",
      input : {
        text : false,
        label : "&nbsp;",
        icons : {
          primary : "ui-icon-closethick"
        }
      },
      click : function(json, event) {
        menus.exit(this.menu_id, true);
        $("#menu-top-left-mainmenu").button("enable");
      }
    }
  ]
};
