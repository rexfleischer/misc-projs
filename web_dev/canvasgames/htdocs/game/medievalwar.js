
function CanvasApp(game)
{
    /**
     * game init
     */
    var width  = game.view.client_width;
    var height = game.view.client_height;
    var status = new StatusHelper(100, "black", "20px _sans", width / 2, height / 2);
    var grid   = new CanvasGrid(width, height, "#000000", 10);
    var clicks = 0;
    var dragging = false;
    var renderPoint = { square: new Square(), render: false };
    
    game.keys.register(KEY_ARROW_UP, function(){
        if (status.finished_work >= 100)
        {
            status.update(0);
        }
        else 
        {
            status.update(status.finished_work + 1);
        }
    });
    game.keys.register(KEY_ARROW_DOWN, function(){
        if (status.finished_work <= 0)
        {
            status.update(100);
        }
        else 
        {
            status.update(status.finished_work - 1);
        }
    });

    game.mouse.registerClick(function(x, y){
        clicks++;
        if (300 < x && x < 400)
        {
            renderPoint.square.x = x;
            renderPoint.square.y = y;
            renderPoint.render = true;
        }
        else
        {
            renderPoint.render = false;
        }
    });
    game.mouse.registerDrag(function(){
        dragging = true;
    });
    game.mouse.registerEndDrag(function(){
        dragging = false;
    });
    
    
    /**
     * update impl
     */
    this.update = function(game)
    {
        
    }
    
    /**
     * render impl
     */
    this.render = function(game, context)
    {
        context.fillStyle = "#ddeeff";
        context.fillRect(0, 0, width, height);
        
        grid.render(context);
        game.timer.render(context);
        
        
        status.render(context);
        context.fillText("Mouse: x="+game.mouse.getX()+", y="+game.mouse.getY(), 20, 40);
        context.fillText("# of clicks: "+clicks, 20, 60);
        context.fillText("dragging: "+dragging, 20, 80);
        
        if (renderPoint.render)
        {
            renderPoint.square.render(context);
        }
    }
}
