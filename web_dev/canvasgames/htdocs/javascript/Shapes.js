/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function Square()
{
    this.width      = 0;
    this.height     = 0;
    this.color      = "#000000";
    this.rotation   = 0;
    this.x          = 0;
    this.y          = 0;
    this.alpha      = 1;
    
    this.render = function(renderer)
    {
        renderer.save();
        renderer.globalAlpha = this.alpha;
        renderer.setTransform(1, 0, 0, 1, 0, 0);
        renderer.translate(this.x + 0.5 * this.width, this.y + 0.5 * this.height);
        renderer.rotate(this.rotation);
        renderer.fillStyle = this.color;
        renderer.fillRect(-0.5 * this.width, -0.5 * this.height, this.width, this.height);
        renderer.restore();
    }
}


function CanvasGrid(width, height, color, amount)
{
    var _width  = width;
    var _height = height;
    var _color  = color;
    var _amount = amount;
    
    var _verticals = new Array();
    var _horizontals = new Array();
    for(var i = 0; i <= _amount; i++)
    {
        var percentage = i / (_amount + 1);
        
        _verticals[i] = percentage * _width;
        _horizontals[i] = percentage * _height;
    }
    
    this.render = function(renderer)
    {
        renderer.save();
        renderer.strokeStyle = _color;
        renderer.beginPath();
        
        var i;
        /**
         * first do vertical
         */
        for(i = 0; i <= _amount; i++)
        {
            renderer.moveTo(_verticals[i], 0);
            renderer.lineTo(_verticals[i], _height);
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
    this.alpha = 0;
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
