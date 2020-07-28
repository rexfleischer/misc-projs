
var systemnav_main_json_br = {
  id : "menu-bottom-right",
  type : "static",
  animate : true,
  start_css : {
    right : 0,
    bottom : "-100%",
    width : "35%", 
    height : "15%" 
  },
  enter_css : {
    "background-color" : "rgba(255,255,255,0.1)",
    bottom : 0
  },
  classes : "ui-corner-tl", 
  create : function() {
    if (this.extra.type == ST_CLUSTER) {
      $("#menu-bottom-right-follow-"+this.extra.name).button("disable");
    }
  },
  content : [ {
      id : function(parent) {
        return "menu-bottom-right-name-"+parent.extra.name;
      },
      type : "text",
      value : function(parent) {
        return "<p>"+parent.extra.name+": "+parent.extra.scope_type+"</p>";
      }
    }, {
      id : function(parent) {
        return "menu-bottom-right-follow-"+parent.extra.name;
      },
      type : "button",
      input : {
        label : "view"
      },
      click : function(json) {
        if (this.extra.scope_type == ST_USER) {
          game.focus_on_system(this.extra._id);
        }
      }
    }
  ]
};
