

function FledWarGame() {
    
    var curr = this;
    
    this.bg_render = new RenderTarget("gamebackgroundcanvas");
    this.bg_render.clear_color = "#272727";
    this.bg_render.clear();
    
    this.fg_render = new RenderTarget("gameforegroundcanvas");
    this.fg_render.clear_color = "rgba(0, 0, 0, 0)";
    this.fg_render.clear();
    
    this.mouse = new MouseActivity("gameforegroundcanvas");
    this.mouse.render_target = this.bg_render;
    this.mouse.rdrag = function(mouse) {
        console.log("rdrag()");
        
        if (mouse.start_x == undefined ||
            mouse.start_y == undefined) {
            mouse.origin_x = curr.bg_render.user_center_x;
            mouse.origin_y = curr.bg_render.user_center_y;
            mouse.start_x = curr.bg_render.pixels_to_game_length(
                mouse.drag_start_x);
            mouse.start_y = curr.bg_render.pixels_to_game_length(
                mouse.drag_start_y);
//            console.log("origin_x: " + mouse.origin_x);
//            console.log("origin_y: " + mouse.origin_y);
//            console.log("start_x: " + mouse.start_x);
//            console.log("start_y: " + mouse.start_y);
        }
        
        var new_x = curr.bg_render.pixels_to_game_length(mouse.x);
        var new_y = curr.bg_render.pixels_to_game_length(mouse.y);
        var new_dx = (mouse.start_x - new_x) * 0.5;
        var new_dy = (mouse.start_y - new_y) * 0.5;
        
        curr.bg_render.user_center_x = (mouse.origin_x - new_dx);
        curr.bg_render.user_center_y = (mouse.origin_y - new_dy);
        
//        console.log("new user center x: " + curr.bg_render.user_center_x);
//        console.log("new user center y: " + curr.bg_render.user_center_y);
        
        curr.render_game();
    }
    this.mouse.renddrag = function(mouse) {
        console.log("renddrag()");
        mouse.start_x = undefined;
        mouse.start_y = undefined;
        mouse.origin_x = undefined;
        mouse.origin_y = undefined;
    }
    this.mouse.ldrag = function(mouse) {
        console.log("ldrag()");
    }
    this.mouse.lenddrag = function(mouse) {
        console.log("lenddrag()");
    }
    this.mouse.rclick = function(mouse) {
        console.log("click x: " + mouse.x);
        console.log("click y: " + mouse.y);
        console.log("game  x: " + curr.bg_render.pixel_x_to_game_x(mouse.x));
        console.log("game  y: " + curr.bg_render.pixel_y_to_game_y(mouse.y));
    }
    
    
    
    this.circles = [];
    for(var i = 0; i < 6; i++) {
        var circle = new Circle();
        circle.x = AU * i;
        circle.y = 0;
        circle.color = "#ffff00";
        circle.radius = 6.955e4;
        circle.index = i;
        circle.lclick = function() {
            console.log("clicked! " + this.index);
        }
        this.circles.push(circle);
        this.mouse.lclick_registry.push(circle);
    }
    
    
    
    
    this.init = function() {
        
        this.bg_render.user_view_width = AU * 10;
        this.bg_render.user_center_x = 0;
        this.bg_render.user_center_y = 0;
        
        this.render_game();
    }
    
    this.close_all = function() {
        
    }
    
    this.render_game = function() {
        
        this.fg_render.clear();
        var grid = new CanvasGrid(50, 
                "rgba(0, 0, 0, .2)",
                this.fg_render.screen_width,
                this.fg_render.screen_height);
        grid.render(this.fg_render.context);
        
        this.bg_render.clear();
        this.bg_render.ready_transform();
        for(var i = 0; i < this.circles.length; i++) {
            this.circles[i].render(this.bg_render.context);
        }
    }
    
}


var game = new FledWarGame();



