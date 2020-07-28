
function Mouse(target_selector, camera, scene) {
  if (!target_selector) target_selector = "body";
  this.target = $(target_selector);
  
  if (camera) {
    this._camera = camera;
    this._scene = scene;
    this._vector = new THREE.Vector3();
    this._projector = new THREE.Projector();
    this._raycaster = new THREE.Raycaster();
  }
  else {
    this._camera = false;
  }
  
  // mouse positions on the screen in pixels
  this.x = 0;
  this.y = 0;
  
  // is the mouse pointer in the selected object
  this.inside = false;
  
  // the start of when the mouse was draggedS
  this.drag_start_x = undefined;
  this.drag_start_y = undefined;
  
  // is the mouse in the state of dragging?
  this.dragging = false;
  
  // is the right mouse button down?
  this.right_down = false;
  
  // is the left mouse button down?
  this.left_down = false;
  
  // right click events
  this.rdrag = function(){};
  this.renddrag = function(){};
  this.rclick = function(){};
  
  // left click events
  this.ldrag = function(){};
  this.lenddrag = function(){};
  this.lclick = function(){};
  
  // wheel events
  this.wheel_up = function(){};
  this.wheel_down = function(){};
}


Mouse.prototype.objects_at_pointer = function() {
  if (!this._camera) {
    console.log("unable to find objects at because camera is not set");
    return false;
  }
  var window_x = (this.x / DEFAULT_WIDTH) * 2 - 1;
  var window_y = -(this.y / DEFAULT_HEIGHT) * 2 + 1;
  
  this._vector.set(window_x, window_y, 0.5);
  this._projector.unprojectVector(this._vector, this._camera);
  this._vector.sub(this._camera.position).normalize();
  this._raycaster.set(this._camera.position, this._vector);
  
  return this._raycaster.intersectObjects(this._scene.children, true);
//  for(var i = 0; i < )
};


Mouse.prototype.reset = function() {
  this.rdrag = function(){};
  this.renddrag = function(){};
  this.rclick = function(){};
  
  this.ldrag = function(){};
  this.lenddrag = function(){};
  this.lclick = function(){};
  
  this.wheel_up = function(){};
  this.wheel_down = function(){};
};


Mouse.prototype.unbind = function() {
  this.target.unbind("mousewheel");
  this.target.unbind("mousemove");
  this.target.unbind("mousedown");
  this.target.unbind("mouseup");
  this.target.unbind("mouseleave");
  this.target.unbind("mouseenter");
  this.target.unbind("contextmenu");
  this.target.unbind("mousewheel");
};


Mouse.prototype.bind = function() {
  this.unbind();
  
  var self = this;
  this.target.mousemove(function(event){
    var offset = self.target.offset();
    self.x = event.pageX - offset.left;
    self.y = event.pageY - offset.top;  
    if (self.right_down) {
      if (self.drag_start_y === undefined ||
          self.drag_start_x === undefined) {
        self.drag_start_x = self.x;
        self.drag_start_y = self.y;
      }
      self.dragging = true;
      self.rdrag();
    }
    else if (self.left_down) {
      if (self.drag_start_y === undefined ||
          self.drag_start_x === undefined) {
        self.drag_start_x = self.x;
        self.drag_start_y = self.y;
      }
      self.dragging = true;
      self.ldrag();
    }
  });
    
  this.target.mousedown(function(event){
    event.preventDefault();
    switch (event.which) {
      case 1:
        self.left_down = true;
        self.right_down = false;
        break;
      case 2:

        break;
      case 3:
        self.left_down = false;
        self.right_down = true;
        break;
      default:
        alert('You have a strange mouse');
    }
    self.dragging = false;
    return false;
  });
  
  this.target.mouseup(function(event){
    event.preventDefault();
    if (self.dragging) {
      if (self.left_down) {
        self.lenddrag();
      }
      else {
        self.renddrag();
      }
    }
    else {
      if (self.left_down) {
        self.lclick();
      }
      else if (self.right_down) {
        self.rclick();
      }
    }
    self.drag_start_x = undefined;
    self.drag_start_y = undefined;
    self.left_down = false;
    self.right_down = false;
    self.dragging = false;
    return false;
  });
  
  this.target.bind("contextmenu", function(event) {
    return false;
  });
  
  this.target.mouseenter(function(event) {
    self.mouse_inside = true;
    self.left_down = false;
    self.right_down = false;
    self.dragging = false;
    self.drag_start_x = undefined;
    self.drag_start_y = undefined;
    return false;
  });
  
  this.target.mouseleave(function(event) {
    if (self.dragging) {
      if (self.left_down) {
        self.lenddrag();
      }
      else if (self.right_down) {
        self.renddrag();
      }
    }
    
    self.mouse_inside = false;
    self.left_down = false;
    self.right_down = false;
    self.dragging = false;
    self.drag_start_x = undefined;
    self.drag_start_y = undefined;
    return false;
  });
  
  this.target.mousewheel(function(event, delta) {
    if (delta > 0) {
      self.wheel_up();
    }
    else if (delta < 0) {
      self.wheel_down();
    }
  });
};


