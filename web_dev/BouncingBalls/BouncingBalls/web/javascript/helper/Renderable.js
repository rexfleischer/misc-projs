
function Circle()
{
  this.x = 0;
  this.y = 0;
  this.radius = 1;
  this.alpha = 1;
  this.color = "#000000";
  
  this.render = function(context)
  {
    context.globalAlpha = this.alpha;
    context.fillStyle = this.color;
    context.beginPath();
    context.arc(this.x + this.radius, this.y + this.radius, this.radius, 0, Math.PI * 2, true);
    context.closePath();
    context.fill();
  }
}

function Square()
{
  this.width      = 0;
  this.height     = 0;
  this.color      = "#000000";
  this.rotation   = 0;
  this.x          = 0;
  this.y          = 0;
  this.alpha      = 1;

  this.render = function(context)
  {
    context.save();
    context.globalAlpha = this.alpha;
    context.setTransform(1, 0, 0, 1, 0, 0);
    context.translate(this.x + 0.5 * this.width, this.y + 0.5 * this.height);
    context.rotate(this.rotation);
    context.fillStyle = this.color;
    context.fillRect(-0.5 * this.width, -0.5 * this.height, this.width, this.height);
    context.restore();
  }
}

function Text()
{
  this.x = 0;
  this.y = 0;
  this.color = "black";
  this.font = "10px _sans";
  this.text = "";
  
  this.render = function(context)
  {
    context.fillStyle = this.color;
    context.font = this.font;
    context.fillText(this.text, this.x, this.y);
  }
  
  this.width = function(context)
  {
    context.font = this.font;
    return context.measureText(this.text).width;
  }
}

function CanvasGrid(width, height, color, size)
{
  var _width  = width;
  var _height = height;
  var _color  = color;
  var _size = size;

  var _verticals = new Array();
  var _horizontals = new Array();
  var i, j;
  for(i = 0, j = 0; j <= _width; i++, j += _size)
  {
    _verticals[i] = j;
  }
  for(i = 0, j = 0; j < _height; i++, j += _size)
  {
    _horizontals[i] = j;
  }

  this.render = function(renderer)
  {
    renderer.save();
    renderer.lineWidth = 0.25;
    renderer.strokeStyle = _color;
    renderer.beginPath();

    var i;
    /**
      * first do vertical
      */
    for(i = 0; i <= _verticals.length; i++)
    {
      renderer.moveTo(_verticals[i], 0);
      renderer.lineTo(_verticals[i], _height);
    }
    
    for(i = 0; i <= _horizontals.length; i++)
    {
      renderer.moveTo(0, _horizontals[i]);
      renderer.lineTo(_width, _horizontals[i]);
    }

    renderer.stroke();
    renderer.closePath();
    renderer.restore();
  }
}

function FledImage(source)
{
  this.image = new Image();
  this.image.src = source;
  this.x = 0;
  this.y = 0;
  this.rotation = 0;

  this.render = function(renderer)
  {
    renderer.save();

    renderer.setTransform(1, 0, 0, 1, 0, 0);
    renderer.translate(this.x, this.y);
    renderer.rotate(this.rotation);
    renderer.drawImage(this.image, -(this.image.width * 0.5), -(this.image.height * 0.5));

    renderer.restore();
  }
}

function ImageWraper(image)
{
  this.image = image;

  this.x = 0;
  this.y = 0;
  this.rotation = 0;

  this.render = function(renderer)
  {
    renderer.save();

    renderer.setTransform(1, 0, 0, 1, 0, 0);
//    renderer.translate(this.x, this.y);
    renderer.rotate(this.rotation);
    renderer.putImageData(this.image, this.x-(this.image.width * 0.5), this.y-(this.image.height * 0.5));
//    renderer.drawImage(this.image, -(this.image.width * 0.5), -(this.image.height * 0.5));

    renderer.restore();
  }
}
