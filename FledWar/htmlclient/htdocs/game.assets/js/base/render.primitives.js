
function IsoscelesTriangle() {
  
  this.base       = 0;
  this.line_width = 1;
  this.color      = "#000000";
  this.rotation   = 0;
  this.x          = 0;
  this.y          = 0;
  
  this.render = function (context) {
    context.save();
    
    context.rotate(this.rotation);
    context.lineWidth = this.line_width;
    var bottom_y = 
    context.moveTo(this.x, this.y + half_base);
    context.lineTo()
    
    context.restore();
  }
}


function Square(){
  this.width      = 0;
  this.height     = 0;
  this.color      = "#000000";
  this.rotation   = 0;
  this.x          = 0;
  this.y          = 0;
  this.alpha      = 1;
    
  this.render = function(context){
    context.save();
    context.globalAlpha = this.alpha;
    context.rotate(this.rotation);
    context.translate(
      this.x + 0.5 * this.width, 
      this.y + 0.5 * this.height);
    context.fillStyle = this.color;
    context.fillRect(
      -0.5 * this.width, 
      -0.5 * this.height, 
      this.width, 
      this.height);
    context.restore();
  }
}


function Circle() {
  var curr = this;
  this.color  = "#000000";
  this.x      = 0;
  this.y      = 0;
  this.radius = 1;
    
  this.render = function(context){
    context.save();
        
    context.translate(this.x, this.y);
    context.beginPath();
    context.fillStyle = this.color;
    context.arc(0, 0, this.radius, 0, Math.PI*2, true);
    context.closePath();
    context.fill();
        
    context.restore();
  }
    
  this.clicked = function(x, y) {
    var dx = (x - curr.x);
    var dy = (y - curr.y);
    var distance = Math.sqrt(dx * dx + dy * dy);
//    console.log("obj("+curr.x+","+curr.y+") "+distance);
    return (distance <= curr.radius);
  }
}


function Ring() {
  var curr = this;
  this.color  = "#000000";
  this.x      = 0;
  this.y      = 0;
  this.radius = 1;
  this.thickness = 1;
    
  this.render = function(context){
    context.save();
        
    context.translate(this.x, this.y);
    context.beginPath();
    context.lineWidth = this.thickness;
    context.strokeStyle = this.color;
    context.arc(0, 0, this.radius, 0, Math.PI*2, false);
    context.closePath();
    context.stroke();
        
    context.restore();
  }
    
  this.clicked = function(x, y) {
    var dx = (x - curr.x);
    var dy = (y - curr.y);
    var distance_from_center = Math.sqrt(dx * dx + dy * dy);
    var distance_from_radius = Math.abs(distance_from_center - curr.radius);
    return (distance_from_radius <= curr.thickness);
  }  
}


function FledImage(source) {
  this.image = new Image();
  this.image.src = source;
    
  this.x = 0;
  this.y = 0;
  this.alpha = 0;
  this.rotation = 0;
    
  this.render = function(context){
    context.save();
        
    context.setTransform(1, 0, 0, 1, 0, 0);
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.drawImage(this.image, 
      -(this.image.width * 0.5), 
      -(this.image.height * 0.5));
        
    context.restore();
  }
}


function CanvasGrid(pixel_size, color, screen_width, screen_height) {
  
  this.render = function(context) {
    context.save();
    context.setTransform(1, 0, 0, 1, 0, 0);
    context.strokeStyle = color;
    context.lineWidth = 1;
    context.beginPath();
        
    var center_x = screen_width * 0.5;
    var center_y = screen_height * 0.5;
        
    /**
         * first do vertical
         */
    for(var x = center_x, nx = center_x; 
        x < screen_width; 
        x += pixel_size, nx -= pixel_size) {
      context.moveTo(x, 0);
      context.lineTo(x, screen_height);
      if (nx != x) {
        context.moveTo(nx, 0);
        context.lineTo(nx, screen_height);
      }
    }
        
    /**
     * now we need to do the horrizontal
     */
    for(var y = center_y, ny = center_y; 
        y < screen_height; 
        y += pixel_size, ny -= pixel_size) {
      context.moveTo(0, y);
      context.lineTo(screen_width, y);
      if (ny != y) {
        context.moveTo(0, ny);
        context.lineTo(screen_width, ny);
      }
    }
        
    context.stroke();
    context.closePath();
    context.restore();
  }
}


