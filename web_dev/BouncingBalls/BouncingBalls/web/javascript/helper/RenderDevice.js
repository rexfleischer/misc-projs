
/**
 * this is used to take case of the common structures
 * of rendering objects.
 */
function RenderDevice(target_id)
{
  if (target_id == null)
  {
    throw "target_id must be defined";
  }
    
  var canvas = $("#" + CANVAS_ID).get(0);
  canvas.width = WIDTH;
  canvas.height = HEIGHT;
  var _context = canvas.getContext("2d");
  var _background = new Square();
  _background.width = canvas.width;
  _background.height = canvas.height;
  
  this.batches = new OrderedHashMap();
      
  this.get_background_color = function()
  {
    return _background.color;
  }
  
  this.set_background_color = function(color)
  {
    _background.color = color;
  }
  
  this.get_width = function()
  {
    return _background.width;
  }
  
  this.get_height = function()
  {
    return _background.height;
  }
  
  this.render = function(object)
  {
    this.render_with_batch("default", object);
  }
  
  this.render_with_batch = function(batch_name, object)
  {
    var batch = this.batches.get(batch_name);
    if (batch != null)
    {
      batch.render(object);
    }
    else
    {
      throw batch_name + " is an unknown batch";
    }
  }
  
  this.clear_screen = function()
  {
    _background.render(_context);
  }
  
  this.flush = function()
  {
    for(var i = 0; i < this.batches.size(); i++)
    {
      var batch = this.batches.get_index(i);
      batch.flush(_context);
    }
  }
  
  this.create_empty_image = function(width, height)
  {
    return _context.createImageData(width, height);
  }
}

function BatchRender(begin, render, finish, clear)
{
  if (render == null)
  {
    throw "render function must be defined";
  }
  
  this.rendering = new Array();
  this.begin = begin;
  this.render = render;
  this.finish = finish;
  this.clear = clear;
  
  this.props = new Array();
  
  this.render = function(rendering)
  {
    this.rendering.push(rendering);
  }
  
  this.flush = function(context)
  {
    if (this.begin != null)
    {
      this.begin(context, this.rendering, this.props);
    }
    
    for(var i = 0; i < this.rendering.length; i++)
    {
      render(context, this.rendering[i], this.props, i);
    }
    
    if (this.finish != null)
    {
      this.finish(context, this.rendering, this.props);
    }
    
    if (this.clear)
    {
      this.rendering = new Array();
    }
  }
}