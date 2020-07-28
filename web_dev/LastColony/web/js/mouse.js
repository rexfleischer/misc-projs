
var mouse = {
  // x, y coordinates of mouse relative to top left corner of canvas
  x: 0,
  y: 0,
  
  // x, y coordinates of mouse relative to top left corner of game map
  gamex: 0,
  gamey: 0,
  
  // game grid x, y coordinates of mouse
  gridx: 0,
  gridy: 0,
  
  // whether or not the left mouse button is currently pressed
  buttonPressed: false,
  
  // whether or not the player is dragging and selecting
  // with the left mouse button pressed
  dragSelect: false,
  dragx: 0,
  dragy: 0,
  
  // whether or not the mouse is inside the canvas region
  insideCanvas: false,
  
  click: function(ev, rightClick) {
    
  },
  
  draw: function() {
    if (this.dragSelect) {
      var x = Math.min(this.gamex, this.dragx);
      var y = Math.min(this.gamey, this.dragy);
      var width = Math.abs(this.gamex - this.dragx);
      var height = Math.abs(this.gamey - this.dragy);
      game.foregroundContext.strokeStyle = "white";
      game.foregroundContext.strokeRect(x-game.offsetX, y-game.offsetY, width, height);
    }
  },
  
  calculateGameCoordinates: function() {
    mouse.gamex = mouse.x + game.offsetX;
    mouse.gamey = mouse.y + game.offsetY;
    
    mouse.gridx = Math.floor((mouse.gamex) / game.gridsize);
    mouse.gridy = Math.floor((mouse.gamey) / game.gridsize);
  },
  
  init: function() {
    var $mouseCanvas = $("#gameforegroundcanvas");
    
    $mouseCanvas.mousemove(function(ev) {
      var offset = $mouseCanvas.offset();
      mouse.x = ev.pageX - offset.left;
      mouse.y = ev.pageY - offset.top;
      
      mouse.calculateGameCoordinates();
      
      if (mouse.buttonPressed) {
        if (Math.abs(mouse.dragx - mouse.gamex) > 4 || 
            Math.abs(mouse.dragy - mouse.gamey) > 4) {
          mouse.dragSelect = true;
        }
      } else {
        mouse.dragSelect = false;
      }
    });
    
    $mouseCanvas.click(function(ev) {
      mouse.click(ev, false);
      mouse.dragSelect = false;
      return false;
    });
    
    $mouseCanvas.mousedown(function(ev){
      if (ev.which == 1) {
        mouse.buttonPressed = true;
        mouse.dragx = mouse.gamex;
        mouse.dragy = mouse.gamey;
        ev.preventDefault();
      }
      return false;
    });
    
    $mouseCanvas.bind('contextmenu', function(ev){
      mouse.click(ev, true);
      return false;
    });
    
    $mouseCanvas.mouseup(function(ev) {
      var shiftPressed = ev.shiftKey;
      if (ev.which == 1) {
        mouse.buttonPressed = false;
        mouse.dragSelect = false;
      }
      return false;
    });
    
    $mouseCanvas.mouseleave(function(ev) {
      mouse.insideCanvas = false;
    });
    
    $mouseCanvas.mouseenter(function(ev) {
      mouse.buttonPressed = false;
      mouse.insideCanvas = true;
    });
  }
}