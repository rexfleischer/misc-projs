
var KEY_BACKSPACE = 8;
var KEY_TAB = 9;
var KEY_ENTER = 13;
var KEY_ESC = 27;

var KEY_SHIFT = 16;
var KEY_CTRL = 17;
var KEY_ALT = 18;

var KEY_LEFT = 37;
var KEY_UP = 38;
var KEY_RIGHT = 39;
var KEY_DOWN = 40;

var KEY_0	= 48;
var KEY_1	= 49;
var KEY_2	= 50;
var KEY_3	= 51;
var KEY_4	= 52;
var KEY_5	= 53;
var KEY_6	= 54;
var KEY_7	= 55;
var KEY_8	= 56;
var KEY_9	= 57;

var KEY_A	= 65;
var KEY_B	= 66;
var KEY_C	= 67;
var KEY_D	= 68;	 
var KEY_E	= 69;
var KEY_F	= 70;
var KEY_G	= 71;
var KEY_H	= 72;
var KEY_I	= 73;
var KEY_J	= 74;
var KEY_K	= 75;
var KEY_L	= 76;
var KEY_M	= 77;
var KEY_N	= 78;
var KEY_O	= 79;
var KEY_P	= 80;
var KEY_Q	= 81;
var KEY_R	= 82;
var KEY_S	= 83;
var KEY_T	= 84;
var KEY_U	= 85;
var KEY_V	= 86;
var KEY_W	= 87;
var KEY_X	= 88;
var KEY_Y	= 89;
var KEY_Z	= 90;

var keyboard = {
  
  keyboard_obj : $("body"),
  
  shift : false,
  ctrl : false,
  alt : false,
  
  key_events : {},
  
  saved : undefined,
  
  save : function() {
    if (!keyboard.saved) {
      keyboard.saved = keyboard.key_events;
      keyboard.key_events = {};
    }
  },
  
  restore : function() {
    if (keyboard.saved) {
      keyboard.key_events = keyboard.saved;
      keyboard.saved = undefined;
    }
  },
  
  reset : function() {
    keyboard.key_events = {};
  },
  
  register : function(key_code, callback) {
    keyboard.key_events["_"+key_code] = callback;
  },
  
  unregister : function(key_code) {
    if (keyboard.key_events["_"+key_code]) {
      delete keyboard.key_events["_"+key_code];
    }
  }
  
};

keyboard.keyboard_obj.keydown(function(event){
  if (KEY_SHIFT == event.which) {
    keyboard.shift = true;
    event.preventDefault();
  }
  else if (KEY_CTRL == event.which) {
    keyboard.ctrl = true;
    event.preventDefault();
  }
  else if (KEY_ALT == event.which) {
    keyboard.alt = true;
    event.preventDefault();
  }
  else if (keyboard.key_events["_"+event.which]) {
    var do_default = keyboard.key_events["_"+event.which]();
    if (!do_default) {
      event.preventDefault();
    }
  }
});

keyboard.keyboard_obj.keyup(function(event){
  if (KEY_SHIFT == event.which) {
    keyboard.shift = false;
    event.preventDefault();
  }
  else if (KEY_CTRL == event.which) {
    keyboard.ctrl = false;
    event.preventDefault();
  }
  else if (KEY_ALT == event.which) {
    keyboard.alt = false;
    event.preventDefault();
  }
});
