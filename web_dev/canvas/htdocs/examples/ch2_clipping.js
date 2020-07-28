
function canvasApp()
{
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    
    drawScreen();
    
    function drawScreen()
    {
        context.fillSytle = "black";
        context.fillRect(10, 10, 200, 200);
        context.save();
        context.beginPath();
        
        context.rect(0, 0, 50, 50);
        context.clip();
        
        context.beginPath();
        context.strokeStyle = "red";
        context.lineWidth = 5;
        context.arc(100, 100, 100, 0, 2 * Math.PI, false);
        context.stroke();
        context.closePath();
        
        context.restore();
        
        context.beginPath();
        context.rect(0, 0, 500, 500);
        context.clip();
        
        context.beginPath();
        context.strokeStyle = "blue";
        context.lineWidth = 5;
        context.arc(100, 100, 50, 0, 2 * Math.PI, false);
        context.stroke();
        context.closePath();
    }
}