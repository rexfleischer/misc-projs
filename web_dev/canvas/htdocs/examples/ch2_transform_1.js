
function canvasApp()
{
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    
    drawScreen();
    
    function drawScreen()
    {
        drawSquare("red",   100, 100, 50, 50, 0, .75);
        drawSquare("blue",  100, 100, 50, 50, Math.PI / 4, .75);
        drawSquare("blue",  200, 100, 50, 50, 0, .75);
        drawSquare("red",   200, 100, 50, 50, Math.PI / 4, .75);
        drawSquare("blue",  300, 100, 50, 50, 0, .5);
        drawSquare("red",   300, 100, 50, 50, Math.PI / 4, .5);
        drawSquare("red",   400, 100, 50, 50, 0, .5);
        drawSquare("blue",  400, 100, 50, 50, Math.PI / 4, .5);
        
        drawSquare("black",  50, 200, 40, 40, Math.PI / 4, .75);
        drawSquare("black", 100, 200, 40, 40, Math.PI / 3, .75);
        drawSquare("black", 150, 200, 40, 40, Math.PI / 2, .75);
        drawSquare("black", 200, 200, 40, 40, Math.PI / 3 * 2, .75);
        
        context.setTransform(1, 0, 0, 1, 0, 0);
        
        var gradient = context.createLinearGradient(0, 0, 100, 0);
        gradient.addColorStop(0, 'rgb(255, 0, 0)');
        gradient.addColorStop(.5,'rgb(0, 255, 0)');
        gradient.addColorStop(1, 'rgb(255, 0, 0)');
        context.globalAlpha = .5;
        context.fillStyle = gradient;
        context.fillRect(0, 0, 100, 100);
        context.fillRect(0, 100, 50, 100);
        context.fillRect(0, 200, 200, 100);
        
        gradient = context.createRadialGradient(300, 300, 25, 350, 350, 100);
        gradient.addColorStop(0, 'rgb(255, 0, 0)');
        gradient.addColorStop(.5,'rgb(0, 255, 0)');
        gradient.addColorStop(1, 'rgb(255, 0, 0)');
        context.fillStyle = gradient;
        context.arc(350, 350, 100, 0, 2*Math.PI, false);
        context.fill();
    }
    
    function drawSquare(color, x, y, width, height, rotation, alpha)
    {
        context.globalAlpha = alpha;
        context.setTransform(1, 0, 0, 1, 0, 0);
        context.translate(x + 0.5 * width, y + 0.5 * height);
        context.rotate(rotation);
        context.fillStyle = color;
        context.fillRect(-0.5 * width, -0.5 * height, width, height);
    }
}