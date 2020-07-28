
/**
 * this is the class that 
 */
function FledApp()
{
  var FRAME_RATE = 1000 / FPS;

  var timer_id  = 0;
  var game      = null;
  var app       = null;
  var game_tick = function()
  {
    game.frames++;
    game.timer.tick();
    app.update(game);
    app.render(game);
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
    if (game == null || app == null)
    {
      game = new GameContext(CANVAS_ID);
      app = new CanvasApp(game);
    }

    if (timer_id)
    {
      clearInterval(timer_id);
    }
    timer_id = setInterval(game_tick, FRAME_RATE);
  }

  this.stop = function()
  {
    clearInterval(timer_id);
    timer_id  = 0;
    game      = null;
    app       = null;
  }
}

var canvas_app;

$(function(){
  canvas_app = new FledApp()
  canvas_app.start();
});
