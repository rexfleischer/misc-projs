
/**
 * global configuration
 * 
 * @TODO: put in another file?
 */
var CANVAS_ID       = "game_canvas";
var CANVAS_DIV_ID   = "canvas_div";
var FPS             = 25;

/**
 * this is the class that 
 */
function FledApp()
{
    const FRAME_RATE = 1000 / FPS;
    
    var canvas = $("#" + CANVAS_ID).get(0);
    var context = canvas.getContext("2d");
    var width = $("#" + CANVAS_ID).css("width").match("(.*?)[^0-9]")[1];
    var height = $("#" + CANVAS_ID).css("height").match("(.*?)[^0-9]")[1];
    
    var timer_id;
    var game;
    var app;
    var game_tick = function()
    {
        game.timer.tick();
        app.update(game);
        app.render(game, context);
    }
    
    this.pause = function()
    {
        if (timer_id)
        {
            clearInterval(timer_id);
            timer_id = 0;
        }
        else
        {
            timer_id = setInterval(game_tick, FRAME_RATE);
        }
    }
    
    this.start = function()
    {
        if (!width || !height)
        {
            return;
        }
        context.fillStyle = "#ddeeff";
        context.fillRect(0, 0, width, height);
        
        game = new GameContext(width, height, CANVAS_ID);
        app = new CanvasApp(game);
        
        if (timer_id)
        {
            clearInterval(timer_id);
        }
        timer_id = setInterval(game_tick, FRAME_RATE);
    }
    
    this.stop = function()
    {
        clearInterval(timer_id);
        timer_id = 0;
    }
}

var canvas_app;

$(function(){
    canvas_app = new FledApp()
    canvas_app.start();
});
