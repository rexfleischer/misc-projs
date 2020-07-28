
function canvasApp()
{
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    
    drawScreen();
    
    function drawScreen()
    {
        // first example
        context.fillStyle = "#aaaaaa";
        context.fillRect(0, 0, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas!", 0, 0);
        
        // second example
        context.fillStyle = "#999999";
        context.fillRect(150, 0, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas2!", 150, 0);
        
        context.fillSytle = "#000000";
        context.strokeStyle = '#ff00ff';
        context.lineWidth = 2;
        context.fillRect(205, 55, 40, 40);
        context.strokeRect(195, 45, 60, 60);
        context.clearRect(215, 65, 20, 20);
        
        // third example
        context.fillStyle = "#aaaaaa";
        context.fillRect(300, 0, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas3!", 300, 0);
        
        context.strokeStyle = "black";
        context.lineWidth = 10;
        context.lineCap = 'square';
        context.beginPath();
        context.moveTo(335, 70);
        context.lineTo(415, 70);
        context.stroke();
        context.closePath();
        
        // forth example
        context.fillStyle = "#999999";
        context.fillRect(0, 150, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas4!", 0, 150);
        
        context.beginPath();
        context.strokeStyle = "black";
        context.lineWidth = 5;
        context.arc(75, 225, 20, 0, Math.PI * 2, false);
        context.stroke();
        context.closePath();
        
        // fifth example
        context.fillStyle = "#aaaaaa";
        context.fillRect(150, 150, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas5!", 150, 150);
        
        context.beginPath();
        context.strokeStyle = "black";
        context.lineWidth = 5;
        context.arc(225, 225, 20, 0, Math.PI / 2, false);
        context.stroke();
        context.closePath();
        
        // six example
        context.fillStyle = "#999999";
        context.fillRect(300, 150, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas6!", 300, 150);
        
        context.beginPath();
        context.strokeStyle = "black";
        context.lineWidth = 5;
        context.arc(375, 225, 20, 0, Math.PI / 2, true);
        context.stroke();
        context.closePath();
        
        // seventh example
        context.fillStyle = "#aaaaaa";
        context.fillRect(0, 300, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas7!", 0, 300);
        
        context.beginPath();
        context.strokeStyle = "black";
        context.lineWidth = 5;
        context.moveTo(10, 330);
        context.lineTo(30, 400);
        context.arcTo(70, 430, 100, 100, 20);
        context.stroke();
        context.closePath();
        
        // eigth example
        context.fillStyle = "#999999";
        context.fillRect(150, 300, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas8!", 150, 300);
        
        context.beginPath();
        context.strokeStyle = "black";
        context.lineWidth = 5;
        context.moveTo(200, 350);
        context.quadraticCurveTo(300, 375, 200, 400);
        context.stroke();
        context.closePath();
        
        // eigth example
        context.fillStyle = "#aaaaaa";
        context.fillRect(300, 300, 150, 150);
        context.fillStyle = "#000000";
        context.font = "20px _sans";
        context.textBaseline = 'top';
        context.fillText("Canvas9!", 300, 300);
        
        context.beginPath();
        context.strokeStyle = "black";
        context.lineWidth = 5;
        context.moveTo(450, 300);
        context.bezierCurveTo(300, 425, 500, 475, 450, 500);
        context.stroke();
        context.closePath();
    }
}