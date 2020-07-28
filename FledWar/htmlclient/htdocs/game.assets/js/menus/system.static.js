
var system_main_json_bl = {
  id : "menu-bottom-left",
  type : "static",
  animate : true,
  start_css : {
    left : 0,
    bottom : "-100%",
    width : "35%", 
    height : "15%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    bottom : 0
  },
  classes : "ui-corner-tr",
  content : [ {
      type : "slider",
      id : "menu-bottom-left-widthslider",
      input : {
        range : "min",
        max : 1000.0,
        min : 0.0,
        value : 500.0,
        slide : function(json, event, ui) {
          render.user_view_width = 
            (game.focus.system_max_zoom - game.focus.system_min_zoom) *
            Math.pow(ui.value / 1000.0, 3) +
            game.focus.system_min_zoom;
        },
        change : function(json, event, ui) {
          ui.value = Math.pow(
            (render.user_view_width - 
              game.focus.system_min_zoom) / 
            (game.focus.system_max_zoom - game.focus.system_min_zoom), 
            1/3);
        }
      },
      update : function(json) {
        $("#"+json.id).slider("value", Math.pow(
          (render.user_view_width - game.focus.system_min_zoom) / 
          (game.focus.system_max_zoom - game.focus.system_min_zoom), 
          1/3) * 1000);
      }
    } , {
      id : "menu-bottom-left-viewreset",
      type : "button",
      input : {
        label : "reset-view"
      },
      click : function(json) {
        game.focus.reset_view();
      }
    }
  ]
};

var system_main_json_tl = {
  id : "menu-top-left",
  type : "static",
  animate : true,
  start_css : {
    left : 0,
    top : "-15%",
    width : "25%", 
    height : "15%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    top : 0
  },
  classes : "ui-corner-br",
  content : [ {
      id : "menu-top-left-mainmenu",
      type : "button",
      input : {
        label : "solar-nav"
      },
      click : function(json) {
        game.focus_on_system_nav();

      }
    }, {
      id : "menu-top-left-stats",
      type : "button",
      input : {
        label : "stats"
      },
      click : function(json) {
        if (menus.contains(system_main_json_nav_status.id)) {
          menus.exit(system_main_json_nav_status.id, true);
        }
        else {
          menus.enter(system_main_json_nav_status, null, true);
        }
      }
    }
  ]
};

var system_main_point_json_br = {
  id : "menu-bottom-right",
  type : "static",
  animate : true,
  start_css : {
    right : 0,
    bottom : "-15%",
    width : "35%", 
    height : "15%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    bottom : 0
  },
  classes : "ui-corner-tl",
  create : function() {
    var stats_menu = menus.get(system_main_point_json_tr.id);
    if (stats_menu !== null) {
      stats_menu.extra = this.extra;
    }
    else if (system_main_point_json_tr.triggered) {
      menus.enter(system_main_point_json_tr, this.extra, true);
    }
  },
  destroy : function() {
    menus.exit(system_main_unit_command.id, true);
  },
  content : [ {
      id : function(parent) {
        return "menu-bottom-right-name";
      },
      type : "text",
      value : function(parent) {
        var data = parent.extra.server_data;
        return "<p>"+data.name+": "+data.type+"</p>";
      }
    }, {
      id : function(parent) {
        return "menu-bottom-right-follow";
      },
      type : "button",
      input : {
        label : "follow"
      },
      click : function(json) {
        menus.enter(system_main_json_follow, this.extra, true);
      }
    }, {
      id : function(parent) {
        return "menu-bottom-right-stats-"+parent.extra.name;
      },
      type : "button",
      input : {
        label : "point-stats"
      },
      click : function(json) {
        if (menus.contains(system_main_point_json_tr.id)) {
          menus.exit(system_main_point_json_tr.id, true);
          system_main_point_json_tr.triggered = false;
        }
        else {
          menus.enter(system_main_point_json_tr, this.extra, true);
          system_main_point_json_tr.triggered = true;
        }
      }
    }, {
      id : function(parent) {
        return "menu-top-left-command";
      },
      type : "button",
      condition : function(parent){
        return (parent.extra !== null && parent.extra.view_type === "unit");
      },
      input : {
        label : "command-unit"
      },
      click : function(json) {
        menus.toggle(system_main_unit_command, this.extra, true);
      }
    }
  ]
};

var system_main_point_json_tr = {
  triggered : false,
  id : "menu-top-right",
  type : "static",
  animate : true,
  start_css : {
    right : 0,
    top : "-30%",
    width : "40%", 
    height : "30%" 
  },
  enter_css : {
    "background-color" : "rgba(0,0,0,0.3)",
    top : 0
  },
  classes : "ui-corner-bl",
  content : [ {
      id : "menu-top-right-details",
      type : "text",
      update : function(json) {

        var input = '';
        switch(this.extra.view_type) {

          case "unit":
            var unit = this.extra.server_data;
            input += "<p>" + unit.name + " stats:</p>";
            input += "<ul>";

            var alpha = unit.orientation.alpha;
            var dist = unit.orientation.distance;
            var x = (dist * Math.cos(alpha));
            var y = (dist * Math.sin(alpha));
            input += "<li>location (x,y) (" +
                    x.toPrecision(3)+", " +
                    y.toPrecision(3)+")</li>";
            input += "<li>location (alpha,dist) (" +
                    alpha.toPrecision(3)+", " +
                    dist.toPrecision(3)+")</li>";
            input += "<li>server updates: "+this.extra.server_updates+"</li>";

            input += "</ul>";
            break;

          case "point":
            var point = this.extra.server_data;
            input += "<p>" + point.name + " stats:</p>";
            input += "<ul>";
            input += "<li>type/mass: "+point.type+" / "+
            point.mass.toPrecision(5)+" kg</li>";
            input += "<li>orbit: " + 
            "[dist:"+point.orientation.distance.toPrecision(3)+","+
            " alpha:"+point.orientation.alpha.toPrecision(3)+","+
            " dalpha:"+point.orientation.dalpha.toPrecision(3)+"]</li>";
            if (point.children && point.children.length) {
              input += "<li>satellites: "+point.children.length+"</li>";
            }
            if (point.type === PT_PLANET) {
              input += "<li>planet type: "+point.planettype+"</li>";
            }
            input += "<li>server updates: "+point.server_updates+"</li>";

            input += "</ul>";
            break;

          default:
            log("unknown unit view_type: " + this.extra.view_type);
        }

        $("#"+json.id).html(input);
      }
    }
  ]
};

var system_main_json_follow = {
  id : "menu-following",
  type : "static",
  animate : true,
  start_css : {
    left : "42.5%",
    bottom : "-100%",
    width : "15%", 
    height : "10%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    bottom : 0
  },
  classes : "ui-corner-top",
  create : function() {
        
    this.following = this.extra;
    render.user_center_x = -(this.following.x * 0.5);
    render.user_center_y = -(this.following.y * 0.5);
    this.following.last_follow_x = this.following.x;
    this.following.last_follow_y = this.following.y;
        
    if (render.user_view_width > SYSTEM_AU * 0.3) {
      if (this.following.type === PT_PLANET &&
          this.following.children.length !== 0) {
        
        var point_hash = game.focus.point_handler.focus_data;
        var distance = 0;
        for(var index in this.following.children) {
          var child_id = this.following.children[index];
          var child = point_hash[child_id];
          if (child.orientation.distance > distance) {
            distance = child.orientation.distance;
          }
        }
              
        var new_width = (distance * 2);
        new_width *= render.screen_aspect;
        new_width *= SYSTEM_ZOOM_I;
                
        if (game.focus.system_min_zoom > new_width) {
          render.user_view_width = game.focus.system_min_zoom;
        }
        else {
          render.user_view_width = new_width;
        }
      }
      else {
        render.user_view_width = (SYSTEM_AU * 0.3);
      }
    }
  },
  update : function() {
    var dx = (this.following.x - this.following.last_follow_x) * 0.5;
    var dy = (this.following.y - this.following.last_follow_y) * 0.5;
    this.following.last_follow_x = this.following.x;
    this.following.last_follow_y = this.following.y;
    render.user_center_x -= dx;
    render.user_center_y -= dy;
  },
  content : [ {
      id : "menu-bottom-middle-button",
      type : "button",
      input : {
        label : "stop-following"
      },
      click : function(json) {
        menus.exit(this.menu_id, true);
      }
    } , {
      id : "menu-bottom-middle-name",
      type : "text",
      value : function(parent) {
        return parent.extra.name;
      }
    }
  ]
};

