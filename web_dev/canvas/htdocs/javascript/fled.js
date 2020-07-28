
var Debugger = function(){}
Debugger.log = function(message)
{
    try
    {
        console.log(message);
    }
    catch(exception)
    {
        alert("error while logging a debug message: " + exception);
    }
}

var eventWindowLoader = function()
{
    if (Modernizr.canvas)
    {
        canvasApp();
    }
}

window.addEventListener('load', eventWindowLoader, false);