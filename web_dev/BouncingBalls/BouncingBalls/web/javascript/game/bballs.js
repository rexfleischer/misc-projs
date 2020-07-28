
var CANVAS_ID       = "game_canvas";
var CANVAS_DIV_ID   = "canvas_div";
var FPS             = 25;
var WIDTH           = 1400;
var HEIGHT          = 800;

function CanvasApp(game)
{
  /**
    * game init
    */
  game.render.set_background_color("grey");
  var width  = game.render.get_width();
  var height = game.render.get_height();
  var text_status_batch;
  var key_notice_batch;
  var balls;
  var maxBallSize = 12;
  var minBallSize = 3;
  var maxSpeed = maxBallSize + 5;
  var friction = 0.000;
  var render_color_wheels = true;

  /**
    * setup inputs
    */
  game.keys.register(KEY_ARROW_UP, function(){
    add_random_ball();
  });
  game.keys.register(KEY_ARROW_DOWN, function(){
    balls.pop();
  });
  game.keys.register(KEY_ARROW_RIGHT, function(){
    friction += 0.001;
  });
  game.keys.register(KEY_ARROW_LEFT, function(){
    friction -= 0.001;
  });
  game.keys.register(KEY_K, function(){
    var rendering = !key_notice_batch.get_render();
    key_notice_batch.set_render(rendering);
  });
  game.keys.register(KEY_C, function(){
    render_color_wheels = !render_color_wheels;
  });
  

  /**
   * setup render device
   */
  {
    text_status_batch = new StatusBatch(game, KEY_T);
    text_status_batch.text_batch.set_pos(20, 20);
    text_status_batch.text_batch.set_deltas(0, 20);
    var ball_count = new Text();
    var friction_text = new Text();
    text_status_batch.text_batch.batch.rendering.push(ball_count);
    text_status_batch.text_batch.batch.rendering.push(friction_text);
    game.render.batches.add("text_status", text_status_batch);
    
    key_notice_batch = new TextLineBatcher();
    var k_notice = new Text();
    k_notice.text = "K -> toggle key help";
    var t_notice = new Text();
    t_notice.text = "T -> toggle status help";
    var c_notice = new Text();
    c_notice.text = "C -> toggle color wheels";
    var up_notice = new Text();
    up_notice.text = "UP ARROW -> add a ball";
    var down_notice = new Text();
    down_notice.text = "DOWN ARROW -> remove a ball";
    var right_notice = new Text();
    right_notice.text = "RIGHT ARROW -> add friction";
    var left_notice = new Text();
    left_notice.text = "LEFT ARROW -> remove friction";
    key_notice_batch.batch.rendering.push(k_notice);
    key_notice_batch.batch.rendering.push(t_notice);
    key_notice_batch.batch.rendering.push(c_notice);
    key_notice_batch.batch.rendering.push(up_notice);
    key_notice_batch.batch.rendering.push(down_notice);
    key_notice_batch.batch.rendering.push(right_notice);
    key_notice_batch.batch.rendering.push(left_notice);
    key_notice_batch.set_pos(20, 200);
    key_notice_batch.set_deltas(0, 20);
    key_notice_batch.set_render(true);
    game.render.batches.add("key_notices", key_notice_batch);
    
    /**
     * setup the color menu
     */
    var colorwheel = new FledImage("assets/colorwheel.png");//BuildColorWheelBitmap(game, 150);
    var menupage = new MenuPage();
    var i;
    for(i = 0; i < 5; i++)
    {
      var option = new MenuOption();
      option.width = colorwheel.image.width;
      option.height = colorwheel.image.height;
      option.posy = i * colorwheel.image.height;
      option.props["color"] = "white";
      option.props["x"] = 0;
      option.props["y"] = 0;
      option.render = function(context, basex, basey)
      {
        if (!render_color_wheels)
        {
          return true;
        }
        colorwheel.x = basex + this.posx;
        colorwheel.y = basey + this.posy;
        colorwheel.render(context);
        var radius = 3;
        context.fillStyle = "black";
        context.beginPath();
        context.arc(colorwheel.x + this.props["x"] + radius, 
                    colorwheel.y + this.props["y"] + radius, 
                    radius, 
                    0, 
                    Math.PI * 2, 
                    true);
        context.closePath();
        context.fill();
      };
      option.onclick = function(x, y, basex, basey)
      {
        if (!render_color_wheels)
        {
          return true;
        }
        var radius = colorwheel.image.height / 2;
        var centerx = this.posx + basex;
        var centery = this.posy + basey;
        var dx = x - centerx;
        var dy = y - centery;
        var distance = Math.pow(dx*dx + dy*dy, 0.5);
        if (distance >= radius)
        {
          return false;
        }
        
        var pointx = dx + (this.width / 2);
        var pointy = dy + (this.height / 2);
        var index = (pointx + pointy * this.width) * 4;
        var red   = colorwheel.image.data[index + 0];
        var green = colorwheel.image.data[index + 1];
        var blue  = colorwheel.image.data[index + 2];
        this.props["color"] = "rgb("+red+","+green+","+blue+")";
        this.props["x"] = dx;
        this.props["y"] = dy;
        
        return true;
      }
      menupage.options.push(option);
    }
   
    var colormenu = new MenuHelper(game, "colorchooser", 2);
    
    colormenu.register_page("start", menupage);
    colormenu.set_position(width - 100, 100);
    
    game.render.batches.add("default", rdh_get_default_batcher(true));
    
    game.render.batches.add("balls", new BatchRender(
      function(context)
      {
        context.save();
      }, 
      function(context, object)
      {
        object.x = object.nextx;
        object.y = object.nexty;

        context.fillStyle = object.color;
        context.beginPath();
        context.arc(object.x, object.y, object.radius, 0, Math.PI*2, true);
        context.closePath();
        context.fill();
      }, 
      function(context)
      {
        context.restore();
      }, 
      false));
      
    balls = game.render.batches.get("balls").rendering;
      
  }
  
  
  /**
    * update impl
    */
  this.update = function(game)
  {
    text_status_batch.update(game);
    update_balls();
    test_wall_collisions();
    test_ball_collisions();
    ball_count.text = "ball count: " + balls.length;
    friction_text.text = "friction: " + friction;
  }

  /**
    * render impl
    */
  this.render = function(game)
  {
    game.render.clear_screen();
    
//    game.render.render(colorwheel);
    
    game.render.flush();
  }
  
  
    
  var update_balls = function()
  {
    for(var i = 0; i < balls.length; i++)
    {
      balls[i].dx -= (balls[i].dx * friction);
      balls[i].dy -= (balls[i].dy * friction);

      balls[i].nextx = (balls[i].x += balls[i].dx);
      balls[i].nexty = (balls[i].y += balls[i].dy);
    }
  }
  
  var test_wall_collisions = function()
  {
    for(var i = 0; i < balls.length; i++)
    {
      var ball = balls[i];
      if (ball.nextx + ball.radius > width)
      {
        if (ball.dx > 0)
        {
          ball.dx *= -1;
        }
        ball.nextx = width - ball.radius;
        ball.color = "black";
      }
      else if(ball.nextx - ball.radius < 0)
      {
        if (ball.dx < 0)
        {
          ball.dx *= -1;
        }
        ball.nextx = ball.radius;
        ball.color = "black";
      }
      if (ball.nexty + ball.radius > height)
      {
        if (ball.dy > 0)
        {
          ball.dy *= -1;
        }
        ball.nexty = height - ball.radius;
        ball.color = "black";
      }
      else if(ball.nexty - ball.radius < 0)
      {
        if (ball.dy < 0)
        {
          ball.dy *= -1;
        }
        ball.nexty = ball.radius;
        ball.color = "black";
      }
    }
  }

  var test_ball_collisions = function()
  {
    for(var i = 0; i < balls.length; i++)
    {
      for(var j = (i + 1); j < balls.length; j++)
      {
        if (are_balls_colliding(balls[i], balls[j]))
        {
          collide_balls(balls[i], balls[j]);
        }
      }
    }
  }
    
  var are_balls_colliding = function(ball1, ball2)
  {
    return Math.sqrt(Math.pow(ball1.x - ball2.x, 2) + 
                     Math.pow(ball1.y - ball2.y, 2)) 
           <= (ball1.radius + ball2.radius);
  }
  
    
  var collide_balls = function(ball1, ball2)
  {
    var collisionAngle = Math.atan2((ball1.nexty - ball2.nexty), 
                                    (ball1.nextx - ball2.nextx));

    var speed1 = Math.sqrt(ball1.dx * ball1.dx + ball1.dy * ball1.dy);
    var speed2 = Math.sqrt(ball2.dx * ball2.dx + ball2.dy * ball2.dy);

    var dir1 = Math.atan2(ball1.dy, ball1.dx);
    var dir2 = Math.atan2(ball2.dy, ball2.dx);

    var vx1 = speed1 * Math.cos(dir1 - collisionAngle);
    var vy1 = speed1 * Math.sin(dir1 - collisionAngle);
    var vx2 = speed2 * Math.cos(dir2 - collisionAngle);
    var vy2 = speed2 * Math.sin(dir2 - collisionAngle);

    var fvx1 = ((ball1.mass - ball2.mass) * vx1 + (2 * ball2.mass) * vx2) / (ball1.mass + ball2.mass);
    var fvx2 = ((2 * ball1.mass) * vx1 + (ball2.mass - ball1.mass) * vx2) / (ball1.mass + ball2.mass);

    var fvy1 = vy1;
    var fvy2 = vy2;

    ball1.dx = Math.cos(collisionAngle) * fvx1 + Math.cos(collisionAngle + (Math.PI/2)) * fvy1;
    ball1.dy = Math.sin(collisionAngle) * fvx1 + Math.sin(collisionAngle + (Math.PI/2)) * fvy1;
    ball2.dx = Math.cos(collisionAngle) * fvx2 + Math.cos(collisionAngle + (Math.PI/2)) * fvy2;
    ball2.dy = Math.sin(collisionAngle) * fvx2 + Math.sin(collisionAngle + (Math.PI/2)) * fvy2;

    ball1.nextx = (ball1.nextx += ball1.dx);
    ball1.nexty = (ball1.nexty += ball1.dy);
    ball2.nextx = (ball2.nextx += ball2.dx);
    ball2.nexty = (ball2.nexty += ball2.dy);
    
    create_new_colors(ball1, ball2);
  }
  
  var add_random_ball = function()
  {
    var placeOk = false;
    var newBall = new ball();
    newBall.color = "black";
    newBall.radius = Math.floor(Math.random() * maxBallSize) + minBallSize;
    var speed = maxSpeed - newBall.radius;
    while(!placeOk)
    {
      newBall.x = newBall.radius * 3 + (Math.floor(Math.random() * width) - newBall.radius * 3);
      newBall.y = newBall.radius * 3 + (Math.floor(Math.random() * height) - newBall.radius * 3);
      var angle = Math.random() * 2 * Math.PI;
      newBall.dx = Math.cos(angle) * speed;
      newBall.dy = Math.sin(angle) * speed;
      newBall.mass = newBall.radius * 8;

      placeOk = can_start_here(newBall);
    }
    balls.push(newBall);
  }
  
  
  var can_start_here = function(ball)
  {
    for(var i = 0; i < balls.length; i++)
    {
      if (are_balls_colliding(ball, balls[i]))
      {
        return false;
      }
    }
    return true;
  }
  
  var create_new_colors = function(ball1, ball2)
  {
    /**
     * we want to do it like this so if we wanted
     * to change how balls change color (like basing
     * the color change on each other) we can
     * easily implement it here.
     */
    ball1.color = create_random_color();
    ball2.color = create_random_color();
  }
  
  var create_random_color = function()
  {
//    var chars = "0123456789abcdef";
//    var result = "";
//    for(var i = 0; i < 6; i++)
//    {
//      var index = Math.floor(Math.random() * chars.length);
//      result += chars.substring(index, index + 1);
//    }
//    return "#" + result;
    var index = Math.floor(Math.random() * menupage.options.length);
    var color = menupage.options[index].props["color"];
    return color;
  }
  
  function ball()
  {
    var color;
    var x;
    var y;
    var dx;
    var dy;
    var radius;
    var mass;
    var nextx;
    var nexty;
  }
  
  for(var i = 0; i < 50; i++)
  {
    add_random_ball();
  }
}

/**
 * algorithm from http://www.briangrinstead.com/blog/colorwheel-1k 
 */
function BuildColorWheelBitmap(game, size)
{
  var image = game.render.create_empty_image(size, size);

  var pos = 0;
  var x, y;
  var radius = size / 2;
  var radius2 = radius * radius;
  for (y = 0; y < size; y++) {
    for (x = 0; x < size; x++) {
      var rx = x - radius,
          ry = y - radius,
          d = rx * rx + ry * ry,
          rgb = hsvToRgb(
              (Math.atan2(ry, rx) + Math.PI) / (Math.PI * 2), // Hue
              Math.sqrt(d) / radius, // Saturation
              1 // Value
          );

      // Print current color, but hide if outside the area of the circle
      image.data[pos++] = rgb[0];
      image.data[pos++] = rgb[1];
      image.data[pos++] = rgb[2];
      image.data[pos++] = d > radius2 ? 0 : 255;
    }
  }
  return new ImageWraper(image);
}

function hsvToRgb(h, s, v) {
  h*=6;
  var i = ~~h,
      f = h - i,
      p = v * (1 - s),
      q = v * (1 - f * s),
      t = v * (1 - (1 - f) * s),
      mod = i % 6,
      r = [v, q, p, p, t, v][mod] * 255,
      g = [t, v, v, q, p, p][mod] * 255,
      b = [p, p, t, v, v, q][mod] * 255;

  return [r, g, b, "rgb("+ ~~r + "," + ~~g + "," + ~~b + ")"];
}
