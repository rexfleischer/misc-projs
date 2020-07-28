

/**
 * key helper. basically you put in the function that you want 
 * to be called when a key is hit. for instance, if you wanted
 * something to happen on the a, w, s, d keys. then it would look like this:
 * 
 * var key_input = new KeyActivity();
 * key_input.register(KEY_A, function(event){...});
 * key_input.register(KEY_W, function(event){...});
 * key_input.register(KEY_S, function(event){...});
 * key_input.register(KEY_D, function(event){...});
 */
function KeyActivity()
{
  var inputs = new Array();
  
  var shitf = false;
  var control = false;
  var alt = false;

  $("body").keydown(function(event)
  {
    if (event.keyCode == KEY_SHIFT)
    {
      shitf = true;
    }
    if (event.keyCode == KEY_CTRL)
    {
      control = true;
    }
    if (event.keyCode == KEY_ALT)
    {
      alt = true;
    }
    if (typeof inputs[event.keyCode] != "undefined")
    {
      var func = inputs[event.keyCode];
      func(event, shitf, control, alt);
    }
  });
  
  $("body").keyup(function(event)
  {
    if (event.keyCode == KEY_SHIFT)
    {
      shitf = false;
    }
    if (event.keyCode == KEY_CTRL)
    {
      control = false;
    }
    if (event.keyCode == KEY_ALT)
    {
      alt = false;
    }
  });

  this.register = function(code, func)
  {
      inputs[code] = func;
  }
}

var KEY_BACKSPACE   = 8;
var KEY_TAB         = 9;
var KEY_ENTER       = 13;
var KEY_SHIFT       = 16;
var KEY_CTRL        = 17;
var KEY_ALT         = 18;
var KEY_PAUSE       = 19;
var KEY_ESCAPE      = 20;
var KEY_PAGE_UP     = 33;
var KEY_PAGE_DOWN   = 34;
var KEY_END         = 35;
var KEY_HOME        = 36;
var KEY_ARROW_LEFT  = 37;
var KEY_ARROW_UP    = 38;
var KEY_ARROW_RIGHT = 39;
var KEY_ARROW_DOWN  = 40;
var KEY_DELETE      = 46;
var KEY_0           = 48;
var KEY_1           = 49;
var KEY_2           = 50;
var KEY_3           = 51;
var KEY_4           = 52;
var KEY_5           = 53;
var KEY_6           = 54;
var KEY_7           = 55;
var KEY_8           = 56;
var KEY_9           = 57;
var KEY_A           = 65;
var KEY_B           = 66;
var KEY_C           = 67;
var KEY_D           = 68;
var KEY_E           = 69;
var KEY_F           = 70;
var KEY_G           = 71;
var KEY_H           = 72;
var KEY_I           = 73;
var KEY_J           = 74;
var KEY_K           = 75;
var KEY_L           = 76;
var KEY_M           = 77;
var KEY_N           = 78;
var KEY_O           = 79;
var KEY_P           = 80;
var KEY_Q           = 81;
var KEY_R           = 82;
var KEY_S           = 83;
var KEY_T           = 84;
var KEY_U           = 85;
var KEY_V           = 86;
var KEY_W           = 87;
var KEY_X           = 88;
var KEY_Y           = 89;
var KEY_Z           = 90;

/**
 * takes care of the mouse activity 
 */
function MouseActivity(target_id)
{
  var x = 0;
  var y = 0;

  var moved = false;
  var down  = false;

  var clicks   = new Object();
  var drags    = new Object();
  var enddrags = new Object();

  this.getX = function()
  {
    return x;
  }

  this.getY = function()
  {
    return y;
  }

  this.registerDrag = function(name, func)
  {
    drags[name] = func;
  }

  this.registerEndDrag = function(name, func)
  {
    enddrags[name] = func;
  }

  this.registerClick = function(name, func)
  {
    clicks[name] = func;
  }
  
  this.removeDrag = function(name)
  {
    delete drags[name];
  }
  
  this.removeEndDrag = function(name)
  {
    delete enddrags[name];
  }
  
  this.removeClick = function(name)
  {
    delete clicks[name];
  }

  $("#" + target_id).mousemove(function(event){
    x = event.pageX - this.offsetLeft;
    y = event.pageY - this.offsetTop;
    moved = true;
    if (down)
    {
      var keys = drags.keys();
      for(var i = 0; i < keys.length; i++)
      {
        var func = drags[ keys[i] ];
        func(x, y);
      }
    }
  });

  $("#" + target_id).mousedown(function(event){
    event.preventDefault();
    moved = false;
    down = true;
  });

  $("#" + target_id).mouseup(function(event){
    event.preventDefault();
    down = false;
    var i, func;
    if (!moved)
    {
      var clickkeys = clicks.keys();
      for(i = 0; i < clickkeys.length; i++)
      {
        func = clicks[ clickkeys[i] ];
        func(x, y);
      }
    }
    else
    {
      var dragkeys = enddrags.keys();
      for(i = 0; i < dragkeys.length; i++)
      {
        func = enddrags[ dragkeys[i] ];
        func(x, y);
      }
    }
  });
}

