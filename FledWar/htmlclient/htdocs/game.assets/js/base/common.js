
if (!console) {
  console = {};
}
if (!console.log) {
  console.log = function(){};
}

var get_time = function() {
  var date = new Date();
  var result = date.getTime();
  delete date;
  return result;
}

var set_object_path = function(object, path, value) {
  var pointer = object;
  for(var i = 0; i < path.length-1; i++) {
    pointer = pointer[path[i]];
  }
  pointer[ path[path.length-1] ] = value;
}

var proxy_object = function(object, scope) {
  var result = $.extend({}, object);
  for(var prop in object) {
    if (typeof(object[prop]) == "function") {
      result[prop] = $.proxy(result[prop], scope, result);
    }
  }
  return result;
}

var resolve_object = function(object, input) {
  for(var prop in object) {
    if (typeof(object[prop]) == "function") {
      object[prop] = object[prop](input);
    }
  }
  return object;
}

var resolve_value = function(value, input) {
  if (typeof(value) == "function") {
    return value(input);
  }
  return value;
}

var counter = {
  curr : 0,
  get : function() {
    counter.curr++;
    return counter.curr;
  }
}

var dialog = {
  obj : $("#dialog_div").dialog(),
  html : function(msg) {
    if (!dialog.obj.dialog("isOpen")) {
      dialog.obj.dialog("open");
    }
    dialog.obj.html(msg);
  },
  hide : function() {
    if (dialog.obj.dialog("isOpen")) {
      dialog.obj.dialog("close");
    }
  }
}
// initial msg
dialog.html("loading, please wait...");


// Setup requestAnimationFrame and cancelAnimationFrame for use in the game code
(function() {
  var lastTime = 0;
  var vendors = ['ms', ';', 'webkit', 'o'];
  for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
    window.requestAnimationFrame = window[vendors[x]+'RequestAnimationFrame'];
    window.cancelAnimationFrame = 
    window[vendors[x]+'CancelAnimationFrame'] || 
    window[vendors[x]+'CancelRequestAnimationFrame'];
  }
 
  if (!window.requestAnimationFrame) {
    window.requestAnimationFrame = function(callback, element) {
      var currTime = new Date().getTime();
      var timeToCall = Math.max(0, 16 - (currTime - lastTime));
      var id = window.setTimeout(
        function() {
          callback(currTime + timeToCall);
        }, 
        timeToCall);
      lastTime = currTime + timeToCall;
      return id;
    };
  }
 
  if (!window.cancelAnimationFrame)
    window.cancelAnimationFrame = function(id) {
      clearTimeout(id);
    };
}());

