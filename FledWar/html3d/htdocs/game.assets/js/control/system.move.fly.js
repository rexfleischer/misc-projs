
function SystemFlyControls(target, keyboard) {
  var self = this;
  
  this.move_state = { 
    up        : 0, 
    down      : 0, 
    left      : 0, 
    right     : 0, 
    forward   : 0, 
    back      : 0, 
    pitch_up  : 0, 
    pitch_down: 0, 
    yaw_left  : 0, 
    yaw_right : 0, 
    roll_left : 0, 
    roll_right: 0 
  };
  this.move_vector = { x : 0, y : 0, z : 0 };
  this.rotation_vector = { x : 0, y : 0, z : 0 };
  this.worker = new THREE.Quaternion();
  this.movement_speed = AU / 1000;
  this.roll_speed = .0005;
  this.speed_boost = 10;
  this.rotation_boost = 2;
  
  
  this.update = function( delta ) {
    if (!this.update_movement_vector() && !this.update_rotation_vector()) {
      return;
    }
    
    var move_multiplier = delta * this.movement_speed * SYSTEM_ZOOM_I;
    var rotation_multiplier = delta * this.roll_speed;
    if (keyboard.shift) {
      move_multiplier *= this.speed_boost;
      rotation_multiplier *= this.rotation_boost;
    }
    
    target.translateX(this.move_vector.x * move_multiplier);
    target.translateY(this.move_vector.y * move_multiplier);
    target.translateZ(this.move_vector.z * move_multiplier);
    
    this.worker.set(
            this.rotation_vector.x * rotation_multiplier,
            this.rotation_vector.y * rotation_multiplier,
            this.rotation_vector.z * rotation_multiplier, 1).normalize();
    target.quaternion.multiply(this.worker);
    
    // expose the rotation vector for convenience
    target.rotation.setFromQuaternion(target.quaternion, target.rotation.order);
  };
  
  
  this.update_movement_vector = function() {
    this.move_vector.x = 
            (this.move_state.right  - this.move_state.left);
    this.move_vector.y = 
            (this.move_state.up     - this.move_state.down);
    this.move_vector.z = 
            (this.move_state.back   - this.move_state.forward);
    
    return this.move_vector.x !== 0 ||
           this.move_vector.y !== 0 || 
           this.move_vector.z !== 0;
  };
  
  
  this.update_rotation_vector = function() {
    this.rotation_vector.x = 
            (this.move_state.pitch_up   - this.move_state.pitch_down);
    this.rotation_vector.y = 
            (this.move_state.yaw_left   - this.move_state.yaw_right);
    this.rotation_vector.z = 
            (this.move_state.roll_left  - this.move_state.roll_right);
    
    return this.rotation_vector.x !== 0 ||
           this.rotation_vector.y !== 0 || 
           this.rotation_vector.z !== 0;
  };
  
  
  keyboard.register(KEY_W, 
    function() { self.move_state.forward = 1; },
    function() { self.move_state.forward = 0; });
  keyboard.register(KEY_S, 
    function() { self.move_state.back = 1; },
    function() { self.move_state.back = 0; });
  keyboard.register(KEY_A, 
    function() { self.move_state.left = 1; },
    function() { self.move_state.left = 0; });
  keyboard.register(KEY_D, 
    function() { self.move_state.right = 1; },
    function() { self.move_state.right = 0; });
  keyboard.register(KEY_R, 
    function() { self.move_state.up = 1; },
    function() { self.move_state.up = 0; });
  keyboard.register(KEY_F, 
    function() { self.move_state.down = 1; },
    function() { self.move_state.down = 0; });
  keyboard.register(KEY_UP, 
    function() { self.move_state.pitch_up = 1; },
    function() { self.move_state.pitch_up = 0; });
  keyboard.register(KEY_DOWN, 
    function() { self.move_state.pitch_down = 1; },
    function() { self.move_state.pitch_down = 0; });
  keyboard.register(KEY_LEFT, 
    function() { self.move_state.yaw_left = 1; },
    function() { self.move_state.yaw_left = 0; });
  keyboard.register(KEY_RIGHT, 
    function() { self.move_state.yaw_right = 1; },
    function() { self.move_state.yaw_right = 0; });
  keyboard.register(KEY_Q, 
    function() { self.move_state.roll_left = 1; },
    function() { self.move_state.roll_left = 0; });
  keyboard.register(KEY_E, 
    function() { self.move_state.roll_right = 1; },
    function() { self.move_state.roll_right = 0; });
  
}
