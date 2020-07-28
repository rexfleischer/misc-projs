
var mouse = {
    
  mouse_obj : $("#" + RENDER_DEVICE_FG),
    
  x : 0,
  y : 0,
  mouse_inside : false,
    
  drag_start_x : undefined,
  drop_start_y : undefined,
    
  dragging : false,
  right_down : false,
  left_down : false,
    
  rdrag : function(){},
  renddrag : function(){},
  ldrag : function(){},
  lenddrag : function(){},
  
  wheel_up : function(){},
  wheel_down : function(){},
    
  rclick_registry : [],
  rclick : function(){},
  rclick_miss : function(){},
    
  lclick_registry : [],
  lclick : function(){},
  lclick_miss : function(){},
  
  
  reset : function() {
    
    mouse.rdrag     = function(){};
    mouse.renddrag  = function(){};
    mouse.ldrag     = function(){};
    mouse.lenddrag  = function(){};
    
    mouse.wheel_up  = function(){};
    mouse.wheel_down= function(){};
    
    mouse.rclick_registry = [];
    mouse.rclick_miss     = function() {}
    mouse.rclick          = function(){
      var x = render.pixel_x_to_game_x(mouse.x);
      var y = render.pixel_y_to_game_y(mouse.y);
      for(var i = 0; i < mouse.rclick_registry.length; i++) {
        var check = mouse.rclick_registry[i];
        if (check.clicked(x, y)) {
          check.rclick(x, y);
          return;
        }
      }

      this.rclick_miss();
    }
    
    mouse.lclick_registry = [];
    mouse.lclick_miss     = function() {}
    mouse.lclick          = function(){
      var x = render.pixel_x_to_game_x(mouse.x);
      var y = render.pixel_y_to_game_y(mouse.y);
      console.log("lclick at: physical("
        +mouse.x+","+mouse.y+"), game("
        +x+","+y+")");
      for(var i = 0; i < mouse.lclick_registry.length; i++) {
//        var check = this.lclick_registry[i];
//        console.log("lclick check: "+check.circle.x+","+
//                      check.circle.y+", rad: "+check.circle.radius);
        if (this.lclick_registry[i].clicked(x, y)) {
          console.log("lclick hit");
          mouse.lclick_registry[i].lclick(x, y);
          return;
        }
      }

      mouse.lclick_miss();
    }
  }
};


mouse.mouse_obj.mousemove(function(event){
  var offset = mouse.mouse_obj.offset();
  mouse.x = event.pageX - offset.left;
  mouse.y = event.pageY - offset.top;  
  if (mouse.right_down) {
    if (mouse.drag_start_y == undefined ||
      mouse.drag_start_x == undefined) {
      mouse.drag_start_x = mouse.x;
      mouse.drag_start_y = mouse.y;
    }
    mouse.dragging = true;
    mouse.rdrag();
  }
  else if (mouse.left_down) {
    if (mouse.drag_start_y == undefined ||
      mouse.drag_start_x == undefined) {
      mouse.drag_start_x = mouse.x;
      mouse.drag_start_y = mouse.y;
    }
    mouse.dragging = true;
    mouse.ldrag();
  }
});
    
mouse.mouse_obj.mousedown(function(event){
  event.preventDefault();
  switch (event.which) {
    case 1:
      mouse.left_down = true;
      mouse.right_down = false;
      break;
    case 2:
                
      break;
    case 3:
      mouse.left_down = false;
      mouse.right_down = true;
      break;
    default:
      alert('You have a strange mouse');
  }

  mouse.dragging = false;
  return false;
});
    
mouse.mouse_obj.mouseup(function(event){
  event.preventDefault();
  if (mouse.dragging) {
    if (mouse.left_down) {
      mouse.lenddrag();
    }
    else {
      mouse.renddrag();
    }
  }
  else {
    if (mouse.left_down) {
      mouse.lclick();
    }
    else {
      mouse.rclick();
    }
  }
    
  mouse.drag_start_x = undefined;
  mouse.drag_start_y = undefined;
  mouse.left_down = false;
  mouse.right_down = false;
  mouse.dragging = false;
  return false;
});

mouse.mouse_obj.bind("contextmenu", function(event) {
  return false;
});

mouse.mouse_obj.mouseenter(function(event) {
  mouse.left_down = false;
  mouse.right_down = false;
  mouse.dragging = false;
  mouse.drag_start_x = undefined;
  mouse.drag_start_y = undefined;
  mouse.mouse_inside = true;
  return false;
});

mouse.mouse_obj.mouseleave(function(event) {
  mouse.mouse_inside = false;
  if (mouse.dragging) {
    if (mouse.left_down) {
      mouse.lenddrag();
    }
    else if (mouse.right_down) {
      mouse.renddrag();
    }
  }
        
  mouse.left_down = false;
  mouse.right_down = false;
  mouse.dragging = false;
  mouse.drag_start_x = undefined;
  mouse.drag_start_y = undefined;
  return false;
});

mouse.mouse_obj.mousewheel(function(event, delta) {
  if (delta > 0) {
    mouse.wheel_up();
  }
  else if (delta < 0) {
    mouse.wheel_down();
  }
});
