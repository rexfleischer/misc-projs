

function SystemOrbitControls(camera, keyboard, mouse) {
  
	this.looking_at = new THREE.Vector3();
  this.offset = undefined;

	this.zoom_speed = 1.0;
	this.rotate_speed = 1.0;
  
	this.min_distance = 0;
	this.max_distance = Infinity;
  
	this.auto_rotate = false;
	this.auto_rotate_speed = 2.0;

	////////////
	// internals

	var curr = this;

	var EPS = 0.000001;

	var delta_up = 0;
	var delta_left = 0;
	var scale = 1;
  
  var prev_dragging_x = undefined;
  var prev_dragging_y = undefined;
  
  var key_state = {
    d_in      : false,
    d_out     : false,
    r_left    : false,
    r_right   : false,
    r_up      : false,
    r_down    : false,
    speed     : false,
    dragging  : false
  };


	this.rotateLeft = function ( angle ) {
		if ( angle === undefined ) {
			angle = getAutoRotationAngle();
		}
    
		delta_left -= angle;
	};
  
	this.rotateUp = function ( angle ) {
		if ( angle === undefined ) {
			angle = getAutoRotationAngle();
		}
    
		delta_up -= angle;
	};
  
	this.dollyIn = function ( dollyScale ) {
		if ( dollyScale === undefined ) {
			dollyScale = getZoomScale();
		}
    
		scale /= dollyScale;
	};
  
	this.dollyOut = function ( dollyScale ) {
		if ( dollyScale === undefined ) {
			dollyScale = getZoomScale();
		}
    
		scale *= dollyScale;
	};
  
	this.update = function () {
    if (!this.offset) {
      this.offset = camera.position.clone().sub(this.looking_at);
    }
    var left = Math.atan2(this.offset.x, this.offset.y);
		var up = Math.atan2( 
        Math.sqrt(this.offset.x*this.offset.x + this.offset.y*this.offset.y), 
        this.offset.z
    );
    
    if (key_state.dragging) {
      var delta_x = (prev_dragging_x - mouse.x);
      var delta_y = (prev_dragging_y - mouse.y);
      
      // rotating across whole screen goes 360 degrees around
      left -= (2 * Math.PI * delta_x / game.viewport_width * curr.rotate_speed);
      // rotating up and down along whole screen attempts to 
      // go 360, but limited to 180
      up += (2 * Math.PI * delta_y / game.viewport_height * curr.rotate_speed);
      
      prev_dragging_x = mouse.x;
      prev_dragging_y = mouse.y;
    }
    else {
      if (key_state.d_in) { this.dollyIn(); }
      if (key_state.d_out) { this.dollyOut(); }
      if (key_state.r_left) { this.rotateLeft(-getAutoRotationAngle()); }
      if (key_state.r_right) { this.rotateLeft(getAutoRotationAngle()); }
      if (key_state.r_up) { this.rotateUp(getAutoRotationAngle()); }
      if (key_state.r_down) { this.rotateUp(-getAutoRotationAngle()); }
      if (keyboard.shift) {
        delta_left *= 5;
        delta_up *= 5;
      }
      left += delta_left;
      up += delta_up;
    }
    
		// restrict phi to be betwee EPS and PI-EPS
		up = Math.max(EPS, Math.min(Math.PI - EPS, up));
    left %= (Math.PI * 2);
    if (left < 0) { left += (Math.PI * 2); }
    
    
		// find radius and restrict it to be between desired limits
		var radius = this.offset.length() * scale;
		radius = Math.max(this.min_distance, Math.min(this.max_distance, radius));
    
    this.offset.z = (radius * Math.cos(up));
    this.offset.y = (radius * Math.sin(up) * Math.cos(left));
    this.offset.x = (radius * Math.sin(up) * Math.sin(left));
    
		camera.position.copy(this.looking_at).add(this.offset);
		camera.lookAt(this.looking_at);
    
		delta_left = 0;
		delta_up = 0;
		scale = 1;
	};
  
  
	function getAutoRotationAngle() {
		return 2 * Math.PI / 60 / 60 * curr.auto_rotate_speed;
	}

	function getZoomScale() {
		return Math.pow(0.95, curr.zoom_speed);
	}

  mouse.ldrag      = function() { 
    key_state.dragging = true; 
    if (prev_dragging_x === undefined ||
        prev_dragging_y === undefined) {
      prev_dragging_x = mouse.drag_start_x;
      prev_dragging_y = mouse.drag_start_y;
    }
  };
  mouse.lenddrag   = function() { 
    key_state.dragging = false;
    prev_dragging_x = undefined;
    prev_dragging_y = undefined;
  };
  mouse.wheel_up   = function() { curr.dollyOut(); };
  mouse.wheel_down = function() { curr.dollyIn(); };
  
  keyboard.register(KEY_Q, 
      function() { key_state.d_out = true; },
      function() { key_state.d_out = false; });
  keyboard.register(KEY_A, 
      function() { key_state.d_in = true; },
      function() { key_state.d_in = false; });
  keyboard.register(KEY_UP, 
      function() { key_state.r_up = true; }, 
      function() { key_state.r_up = false; });
  keyboard.register(KEY_DOWN, 
      function() { key_state.r_down = true; }, 
      function() { key_state.r_down = false; });
  keyboard.register(KEY_LEFT, 
      function() { key_state.r_left = true; }, 
      function() { key_state.r_left = false; });
  keyboard.register(KEY_RIGHT, 
      function() { key_state.r_right = true; }, 
      function() { key_state.r_right = false; });
};

