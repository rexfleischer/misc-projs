
var systemnav_main_json_nav_status = {
  id : "menu-nav-status",
  type : "static",
  animate : true,
  start_css : {
    left : "-100%",
    top : "45%",
    width : "25%", 
    height : "auto" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    left : 0
  },
  classes : "ui-corner-right",
  content : [ {
      id : "menu-nav-status-screenwidth",
      type : "text",
      value : "<p>screen width: N/A</p>",
      update : function(json) {
        $("#"+json.id).html(
          "<p>screen width: " + 
          render.user_view_width.toPrecision(2) + 
          "</p>");
      }
    }, {
      id : "menu-nav-status-center",
      type : "text",
      value : "<p>view-center: (0, 0)</p>",
      update : function(json) {
        var x = render.user_center_x.toPrecision(2);
        var y = render.user_center_y.toPrecision(2);
        $("#"+json.id).html("<p>view-center: ("+x+", "+y+")</p>");
      }
    }, {
      id : "menu-nav-status-runtime",
      type : "text",
      value : "<p>run time: 0</p>",
      update : function(json) {
        var time = (get_time() - game.start_time);
        $("#"+json.id).html("<p>run time: "+time+"</p>");
      }
    }, {
      id : "menu-nav-status-mouse",
      type : "text",
      value : "<p>mouse loc: (0, 0)</p>",
      update : function(json) {
        var x = mouse.x;
        var y = mouse.y;
        $("#"+json.id).html("<p>mouse loc: ("+x+", "+y+")</p>");
      }
    }
  ]
};

var systemnav_main_json_system_status = {
  id : "menu-nav-system-status",
  type : "static",
  animate : true,
  start_css : {
    right : "-100%",
    top : "25%",
    width : "50%", 
    height : "auto" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    right : 0
  },
  classes : "ui-corner-left",
  content : [ {
      id : "menu-nav-system-status-screenwidth",
      type : "text",
      value : "",
      update : function(json) {

        var report;
        if (!game.focus.focus_data) {
          report = "nothing to report";
        }
        else {
          report = "<ul>";

          for(var i = 0; i < game.focus.focus_data.length; i++) {
            var system = game.focus.focus_data[i];
            report += ("<li>"+system.name);

            report += (" ("+system.orientation.x+", "+system.orientation.y+")");

            var dx = (system.orientation.x - render.user_center_x);
            var dy = (system.orientation.y - render.user_center_y);
            var dd = Math.sqrt(dx*dx + dy*dy);
            report += (" " + (dd < render.user_view_width ? "hit" : "miss"));

            report += "</li>";
          }

          report += "</ul>";
        }

        $("#"+json.id).html(report);
      }
    }
  ]
};
