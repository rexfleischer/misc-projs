
function canvasApp()
{
    
    function gameTick()
    {
        update();
        testWallCollisions();
        testBallCollisions();
        render();
    }
    
    function update()
    {
        for(var i = 0; i < balls.length; i++)
        {
            balls[i].dx -= (balls[i].dx * friction);
            balls[i].dy -= (balls[i].dy * friction);
            
            balls[i].nextx = (balls[i].x += balls[i].dx);
            balls[i].nexty = (balls[i].y += balls[i].dy);
        }
    }
    
    function testWallCollisions()
    {
        for(var i = 0; i < balls.length; i++)
        {
            var ball = balls[i];
            if (ball.nextx + ball.radius > canvas.width)
            {
                if (ball.dx > 0)
                {
                    ball.dx *= -1;
                }
                ball.nextx = canvas.width - ball.radius;
            }
            else if(ball.nextx - ball.radius < 0)
            {
                if (ball.dx < 0)
                {
                    ball.dx *= -1;
                }
                ball.nextx = ball.radius;
            }
            if (ball.nexty + ball.radius > canvas.height)
            {
                if (ball.dy > 0)
                {
                    ball.dy *= -1;
                }
                ball.nexty = canvas.height - ball.radius;
            }
            else if(ball.nexty - ball.radius < 0)
            {
                if (ball.dy < 0)
                {
                    ball.dy *= -1;
                }
                ball.nexty = ball.radius;
            }
        }
    }
    
    function testBallCollisions()
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
    
    function render()
    {
        context.fillStyle = "#eeeeee";
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.strokeStyle = "#000000";
        context.strokeRect(1, 1, canvas.width - 2, canvas.height - 2);
        
        context.fillStyle = "#000000";
        for(var i = 0; i < balls.length; i++)
        {
            balls[i].x = balls[i].nextx;
            balls[i].y = balls[i].nexty;
            
            context.beginPath();
            context.arc(balls[i].x, balls[i].y, balls[i].radius, 0, Math.PI*2, true);
            context.closePath();
            context.fill();
        }
    }
    
    function collide_balls(ball1, ball2)
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
    }
    
    function are_balls_colliding(ball1, ball2)
    {
        return Math.sqrt(Math.pow(ball1.x - ball2.x, 2) + Math.pow(ball1.y - ball2.y, 2)) <= (ball1.radius + ball2.radius);
    }
    
    function init(numOfBalls)
    {
        for(var i = 0; i < numOfBalls; i++)
        {
            var placeOk = false;
            var newBall = new ball();
            newBall.radius = Math.floor(Math.random() * maxBallSize) + minBallSize;
            var speed = maxSpeed - newBall.radius;
            while(!placeOk)
            {
                newBall.x = newBall.radius * 3 + (Math.floor(Math.random() * canvas.width) - newBall.radius * 3);
                newBall.y = newBall.radius * 3 + (Math.floor(Math.random() * canvas.height) - newBall.radius * 3);
                var angle = Math.random() * 2 * Math.PI;
                newBall.dx = Math.cos(angle) * speed;
                newBall.dy = Math.sin(angle) * speed;
                newBall.mass = newBall.radius * 8;
                
                placeOk = can_start_here(newBall);
            }
            balls.push(newBall);
        }
    }
    
    function can_start_here(ball)
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
    
    function ball()
    {
        var x;
        var y;
        var dx;
        var dy;
        var radius;
        var mass;
        var nextx;
        var nexty;
    }
    
    var maxBallSize = 12;
    var minBallSize = 3;
    var maxSpeed = maxBallSize + 5;
    var friction = 0.001;
    var balls = new Array();
    
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    
    init(50);
    
    setInterval(gameTick, 33);
}