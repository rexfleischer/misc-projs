
Object.prototype.keys = function()
{ 
  if (this !== Object(this)) 
  {
    throw new TypeError('Object.keys called on non-object');
  }
  var result = [];
  var p; 
  for (p in this) 
  {
    if (Object.prototype.hasOwnProperty.call(this, p)) 
    {
      result.push(p); 
    }
  }
  return result; 
}

/**
 * 
 */
function OrderedHashMap()
{
  var _keys = [];
  var _vals = {};
  
  this.add_at_index = function(index, key, value)
  {
    if (key in _vals)
    {
      this.remove(key);
    }
    _keys.splice(index, 0, key);
    _vals[key] = value;
  }
  
  this.add = function(key, value)
  {
    if(key in _vals)
    {
      _vals[key] = value;
    }
    else 
    {
      _keys.push(key);
      _vals[key] = value;
    }
  }
  
  this.remove = function(key)
  {
    var index = _keys.indexOf(key);
    if(index == -1) 
    {
      throw new Error('key does not exist');
    }
    _keys.splice(index, 1);
    var result = _vals[key];
    delete _vals[key];
    return result;
  }
  
  this.get = function(key)
  {
    if (_vals.hasOwnProperty(key))
    {
      return _vals[key];
    }
    else
    {
      return null;
    }
  }
  
  this.get_index = function(index)
  {
    if (0 <= index && 
        index <= _keys.length && 
        _vals.hasOwnProperty(_keys[index]))
    {
      return _vals[ _keys[index] ];
    }
    return null;
  }
  
  this.size = function()
  {
    return _keys.length;
  }
}

function TextLineBatcher()
{
  var _posx = 0;
  var _posy = 0;
  var _deltax = 0;
  var _deltay = 0;
  var _font = "20px _sans";
  var _color = "#000000";
  var _render = true;
  
  this.set_render = function(render)
  {
    _render = render;
  }
  
  this.get_render = function()
  {
    return _render;
  }
  
  this.set_pos = function(x, y)
  {
    _posx = x;
    _posy = y;
  }
  
  this.set_deltas = function(dx, dy)
  {
    _deltax = dx;
    _deltay = dy;
  }
  
  this.set_color = function(color)
  {
    _color = color;
  }
  
  this.set_font = function(font)
  {
    _font = font;
  }
  
  this.batch = new BatchRender(
    function(context, objects, props)
    {
      context.save();
      context.fillStyle = _color;
      context.font      = _font;
    },
    function(context, object, props, iter)
    {
      context.fillText(object.text, 
                       _posx + iter*_deltax, 
                       _posy + iter*_deltay);
    },
    function(context, objects, props)
    {
      context.restore();
    },
    false
  );
  
  this.flush = function(context)
  {
    if (_render)
    {
      this.batch.flush(context);
    }
  }
}

function StatusBatch(game, toggle_key)
{
  var _render = true;
  var _clicks = 0;
  var _dragging = false;
  game.mouse.registerClick("metric", function(x, y){
    _clicks++;
  });
  game.mouse.registerDrag("metric", function(){
    _dragging = true;
  });
  game.mouse.registerEndDrag("metric", function(){
    _dragging = false;
  });
  
  /**
   * grid setup
   */
  this.grid = new CanvasGrid(game.render.get_width(), game.render.get_height(), "#000000", 40);

  /**
  * setup text
  */
  this.text_batch = new TextLineBatcher();
  var game_fames    = new Text();
  var text_counter  = new Text();
  var text_mouse    = new Text();
  var text_clicks   = new Text();
  var text_dragging = new Text();
  
  this.text_batch.batch.render(game_fames);
  this.text_batch.batch.render(text_counter);
  this.text_batch.batch.render(text_mouse);
  this.text_batch.batch.render(text_dragging);
  this.text_batch.batch.render(text_clicks);
  
  if (toggle_key != 0)
  {
    game.keys.register(toggle_key, function(){
      _render = !_render;
    });
  }
  
  this.update = function(game)
  {
    if (_render)
    {
      game_fames.text   = "FPS: " + game.timer.last_frame_count;
      text_mouse.text   = "Mouse: x=" + game.mouse.getX() + ", y=" + game.mouse.getY();
      text_clicks.text  = "# of clicks: " + _clicks;
      text_dragging.text= "dragging: " + _dragging;
      text_counter.text = "total frames: " + game.frames;
    }
  }
  
  this.flush = function(context)
  {
    if (_render)
    {
      this.grid.render(context);
      this.text_batch.flush(context);
    }
  }
}

function FadingAlpha()
{
  this.c1 = 1;
  this.c2 = 1;
  var start_time = 0;
  
  this.start = function()
  {
    start_time = get_time();
  }
  
  /**
   * the function assumed is based in sections:
   * alpha = Math.min(1, -(t / c1)^2 + c2)
   * once alpha is less than 0, then the repeating
   * function will stop updating
   */
  this.get_alpha = function()
  {
    var t = get_time() - start_time;
    var alpha = Math.min(1, - Math.pow((t / this.c1), 2) + this.c2);
    return alpha;
  }
  
  var get_time = function()
  {
    var date = new Date();
    var time = date.getTime();
    delete date;
    return time;
  }
  
}
